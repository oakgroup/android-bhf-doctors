package com.active.orbit.baseapp.design.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.SeverityDialogAdapter
import com.active.orbit.baseapp.design.recyclers.adapters.SymptomsDialogAdapter
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SelectSymptomDialog : DialogFragment() {

    private var adapter: SymptomsDialogAdapter? = null
    var listener: SelectSymptomDialogListener? = null
    private var symptoms = ArrayList<String>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_selection, view as ViewGroup?, false)
        setup(view)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(Constants.EMPTY)
        builder.setView(view)
        return builder.create()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setup(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val title = view.findViewById<BaseTextView>(R.id.title)

        title.text = getString(R.string.select_symptom)

        val symptomsList: Array<String> = resources.getStringArray(R.array.symptoms_options)
        for (symptom in symptomsList) {
            symptoms.add(symptom)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        adapter = SymptomsDialogAdapter(view.context)
        recyclerView.adapter = adapter

        adapter?.listener = listener
        adapter?.symptoms = ArrayList(symptoms)
        adapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

package com.active.orbit.baseapp.design.dialogs

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
import com.active.orbit.baseapp.design.dialogs.listeners.SelectProgrammeDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.ProgrammesDialogAdapter
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SelectProgrammeDialog : DialogFragment() {

    private var adapter: ProgrammesDialogAdapter? = null
    var listener: SelectProgrammeDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_selection, view as ViewGroup?, false)
        setup(view)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(Constants.EMPTY)
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val title = view.findViewById<BaseTextView>(R.id.title)

        title.text = getString(R.string.program_select)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        adapter = ProgrammesDialogAdapter(requireActivity() as BaseActivity, listener)
        recyclerView.adapter = adapter

        adapter?.refresh(requireActivity())
    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

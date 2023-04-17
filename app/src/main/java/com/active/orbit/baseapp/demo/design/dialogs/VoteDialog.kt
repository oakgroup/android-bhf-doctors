package com.active.orbit.baseapp.demo.design.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.widgets.BaseButton
import com.active.orbit.baseapp.design.widgets.BaseSpinner

class VoteDialog : DialogFragment() {

    companion object {
        const val ARGUMENT_VOTE = "vote_argument"
    }

    private var spinner: BaseSpinner? = null

    var listener: VoteDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_vote, view as ViewGroup?, false)
        setup(view, arguments?.getInt(ARGUMENT_VOTE))

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(Constants.EMPTY)
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View, oldVote: Int?) {
        spinner = view.findViewById(R.id.dialogVoteSpinner)
        val confirmButton = view.findViewById<BaseButton>(R.id.dialogVoteButtonConfirm)
        val cancelButton = view.findViewById<BaseButton>(R.id.dialogVoteButtonCancel)

        val entries = arrayListOf<String>()
        for (i in 0..9) {
            entries.add(i.toString())
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, entries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter

        if (oldVote != null && oldVote != Constants.INVALID) {
            spinner?.setSelection(oldVote, false)
        }

        confirmButton.setOnClickListener {
            val vote = spinner!!.selectedItemPosition
            listener?.onConfirm(vote)
            dismiss()
        }

        cancelButton.setOnClickListener {
            listener?.onCancel()
            dismiss()
        }
    }
}

package com.active.orbit.baseapp.design.dialogs.engine

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.active.orbit.baseapp.core.utils.Logger

open class BaseDialogFragment : DialogFragment() {

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: IllegalStateException) {
            Logger.w("Exception on fragment ${e.localizedMessage}")
        }
    }
}

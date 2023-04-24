package com.active.orbit.baseapp.design.dialogs.listeners

import com.active.orbit.baseapp.core.database.models.DBSeverity

interface SelectSeverityDialogListener {

    fun onSeveritySelected(severity: DBSeverity)
}
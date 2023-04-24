package com.active.orbit.baseapp.design.dialogs.listeners

import com.active.orbit.baseapp.core.database.models.DBProgram

interface SelectProgrammeDialogListener {

    fun onProgrammeSelect(programme: DBProgram)
}
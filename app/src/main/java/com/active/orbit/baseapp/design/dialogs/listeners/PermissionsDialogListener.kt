package com.active.orbit.baseapp.design.dialogs.listeners

interface PermissionsDialogListener {

    fun onShowPermission(requestCode: Int)

    fun onCancel() {}
}

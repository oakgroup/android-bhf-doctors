package com.active.orbit.baseapp.core.preferences.engine

object PreferencesKeys {

    private const val prefix = "base.app."
    const val filename = "base.app.preferences.filename"

    // backend preferences
    const val preference_backend_base_url = prefix + "backend.base.url"

    // user preferences
    const val preference_user_id = prefix + "user.id"
    const val preference_user_name = prefix + "user.name"

    // lifecycle preferences
    const val preference_lifecycle_first_install = prefix + "lifecycle.first.install"
}
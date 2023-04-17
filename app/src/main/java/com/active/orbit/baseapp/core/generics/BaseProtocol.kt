package com.active.orbit.baseapp.core.generics

import android.text.TextUtils
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.demo.core.database.models.DBDemo

/**
 * Base protocol that should be implemented by all the models
 *
 * @author omar.brugna
 */
interface BaseProtocol : Comparable<BaseProtocol?> {

    fun isValid(): Boolean

    fun identifier(): String

    fun sameOf(other: BaseProtocol?): Boolean {
        if (!TextUtils.isEmpty(identifier()) && !TextUtils.isEmpty(other?.identifier()))
            return identifier() == other?.identifier()
        return false
    }

    fun sameContentOf(other: BaseProtocol?): Boolean {
        // override to customize
        return false
    }

    override fun compareTo(other: BaseProtocol?): Int {
        when {
            // DEMO_CODE!
            this is DBDemo && other is DBDemo -> {
                return demoId.compareTo(other.demoId, true)
            }
        }
        return Constants.PRIORITY_ZERO
    }
}
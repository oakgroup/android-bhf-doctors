package com.active.orbit.baseapp.core.database.engine

import com.active.orbit.baseapp.core.generics.BaseProtocol
import com.active.orbit.baseapp.core.utils.BaseException
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger

/**
 * Base database model that should be extended from other database models
 */
open class DBModel : BaseProtocol {

    override fun isValid(): Boolean {
        throw BaseException("Is valid method must never be called on the base class")
    }

    override fun identifier(): String {
        Logger.e("Called base identifier method, this should never happen")
        return Constants.EMPTY
    }
}
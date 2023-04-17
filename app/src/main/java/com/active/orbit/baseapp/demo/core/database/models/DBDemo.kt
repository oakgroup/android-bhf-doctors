package com.active.orbit.baseapp.demo.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.active.orbit.baseapp.core.database.engine.DBModel
import com.active.orbit.baseapp.core.utils.Constants

/**
 * Class that represent a demo database model
 *
 * @author omar.brugna
 */
@Entity(tableName = "demo")
class DBDemo : DBModel() {

    @PrimaryKey
    var demoId: String = Constants.EMPTY
    var demoVote: Int = Constants.INVALID

    override fun identifier(): String {
        return demoId
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(demoId)
    }

    fun description(): String {
        return "[$demoId - $demoVote]"
    }
}
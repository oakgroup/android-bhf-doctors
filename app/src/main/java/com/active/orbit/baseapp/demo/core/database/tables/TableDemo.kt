package com.active.orbit.baseapp.demo.core.database.tables

import androidx.room.*
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.demo.core.database.models.DBDemo

/**
 * Utility class used to manage database images
 *
 * @author omar.brugna
 */
@Dao
interface TableDemo {

    @Query("SELECT * FROM demo")
    fun getAll(): List<DBDemo>

    @Query("SELECT * FROM demo WHERE demoVote <> :statusInvalid")
    fun getAllVoted(statusInvalid: Int = Constants.INVALID): List<DBDemo>

    @Query("SELECT * FROM demo WHERE demoId == :demoId LIMIT 1")
    fun getById(demoId: String): DBDemo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBDemo>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBDemo>)

    fun upsert(model: DBDemo) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBDemo>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM demo")
    fun truncate()
}
package com.active.orbit.baseapp.core.database.queries

import androidx.room.*
import com.active.orbit.baseapp.core.database.models.DBProgram

@Dao
interface Programs {

    @Query("SELECT * FROM programs ORDER BY position")
    fun getAll(): List<DBProgram>

    @Query("SELECT * FROM programs WHERE idProgram == :idProgram LIMIT 1")
    fun getById(idProgram: String): DBProgram?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBProgram>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBProgram>)

    fun upsert(model: DBProgram) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBProgram>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM programs WHERE idProgram == :idProgram")
    fun delete(idProgram: Long)

    @Query("DELETE FROM programs")
    fun truncate()
}
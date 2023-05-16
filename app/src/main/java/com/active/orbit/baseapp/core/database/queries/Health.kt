package com.active.orbit.baseapp.core.database.queries

import androidx.room.*
import com.active.orbit.baseapp.core.database.models.DBHealth

@Dao
interface Health {

    @Query("SELECT * FROM health")
    fun getAll(): List<DBHealth>

    @Query("SELECT * FROM health WHERE healthID == :healthID LIMIT 1")
    fun getById(healthID: String): DBHealth?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBHealth>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBHealth>)

    fun upsert(model: DBHealth) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBHealth>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM health WHERE healthID == :healthID")
    fun delete(healthID: Long)

    @Query("DELETE FROM health")
    fun truncate()
}
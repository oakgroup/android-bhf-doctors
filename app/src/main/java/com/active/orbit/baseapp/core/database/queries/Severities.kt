package com.active.orbit.baseapp.core.database.queries

import androidx.room.*
import com.active.orbit.baseapp.core.database.models.DBSeverity

@Dao
interface Severities {

    @Query("SELECT * FROM severities WHERE idSymptom = :idSymptom ORDER BY position")
    fun getAllForSymptom(idSymptom: String): List<DBSeverity>

    @Query("SELECT * FROM severities WHERE idSymptom = :idSymptom AND idSeverity == :idSeverity LIMIT 1")
    fun getByIdForSymptom(idSymptom: String, idSeverity: String): DBSeverity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBSeverity>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBSeverity>)

    fun upsert(model: DBSeverity) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBSeverity>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM severities WHERE idSeverity == :idSeverity")
    fun delete(idSeverity: Long)

    @Query("DELETE FROM severities")
    fun truncate()
}
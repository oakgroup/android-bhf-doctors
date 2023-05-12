package com.active.orbit.baseapp.core.database.queries

import androidx.room.*
import com.active.orbit.baseapp.core.database.models.DBReportSymptom

@Dao
interface SymptomsReported {

    @Query("SELECT * FROM reported_symptoms")
    fun getAll(): List<DBReportSymptom>

    @Query("SELECT * FROM reported_symptoms WHERE symptomId == :symptomId LIMIT 1")
    fun getById(symptomId: String): DBReportSymptom?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBReportSymptom>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBReportSymptom>)

    fun upsert(model: DBReportSymptom) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBReportSymptom>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM reported_symptoms WHERE symptomId == :symptomId")
    fun delete(symptomId: Long)

    @Query("DELETE FROM reported_symptoms")
    fun truncate()
}
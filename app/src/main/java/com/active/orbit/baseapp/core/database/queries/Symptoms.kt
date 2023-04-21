package com.active.orbit.baseapp.core.database.queries

import androidx.room.*
import com.active.orbit.baseapp.core.database.models.DBSymptom

@Dao
interface Symptoms {

    @Query("SELECT * FROM symptoms WHERE idProgram = :idProgram ORDER BY position")
    fun getAllForProgram(idProgram: String): List<DBSymptom>

    @Query("SELECT * FROM symptoms WHERE idProgram = :idProgram AND idSymptom == :idSymptom LIMIT 1")
    fun getByIdForProgram(idProgram: String, idSymptom: String): DBSymptom?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBSymptom>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBSymptom>)

    fun upsert(model: DBSymptom) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBSymptom>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM symptoms WHERE idSymptom == :idSymptom")
    fun delete(idSymptom: Long)

    @Query("DELETE FROM symptoms")
    fun truncate()
}
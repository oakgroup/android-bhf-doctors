package com.active.orbit.baseapp.core.database.tables

import android.content.Context
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.utils.Logger

object TableSymptoms {

    @WorkerThread
    fun getAll(context: Context, idProgram: String): List<DBSymptom> {
        try {
            return Database.getInstance(context).getSymptoms().getAllForProgram(idProgram)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting all symptoms for program $idProgram from database ${e.localizedMessage}")
        }
        return arrayListOf()
    }

    @WorkerThread
    fun getById(context: Context, idProgram: String, idSymptom: String): DBSymptom? {
        try {
            return Database.getInstance(context).getSymptoms().getByIdForProgram(idProgram, idSymptom)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting symptom by id $idSymptom for program $idProgram from database ${e.localizedMessage}")
        }
        return null
    }

    @WorkerThread
    fun upsert(context: Context, model: DBSymptom) {
        upsert(context, listOf(model))
    }

    @WorkerThread
    fun upsert(context: Context, models: List<DBSymptom>) {
        try {
            Database.getInstance(context).getSymptoms().upsert(models)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on upsert symptoms to database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun delete(context: Context, idSymptom: Long) {
        try {
            Database.getInstance(context).getSymptoms().delete(idSymptom)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on delete symptom with id $idSymptom from database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun truncate(context: Context) {
        try {
            Database.getInstance(context).getSymptoms().truncate()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on truncate symptoms from database ${e.localizedMessage}")
        }
    }
}
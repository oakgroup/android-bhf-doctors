package com.active.orbit.baseapp.core.database.tables

import android.content.Context
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.database.models.DBReportSymptom
import com.active.orbit.baseapp.core.utils.Logger

object TableReportedSymptoms {

    @WorkerThread
    fun getAll(context: Context): List<DBReportSymptom> {
        try {
            return Database.getInstance(context).getReportedSymptoms().getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting all reported symptoms from database ${e.localizedMessage}")
        }
        return arrayListOf()
    }

    @WorkerThread
    fun getById(context: Context, idSymptom: String): DBReportSymptom? {
        try {
            return Database.getInstance(context).getReportedSymptoms().getById(idSymptom)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting reported symptom by id $idSymptom${e.localizedMessage}")
        }
        return null
    }

    @WorkerThread
    fun upsert(context: Context, model: DBReportSymptom) {
        upsert(context, listOf(model))
    }

    @WorkerThread
    fun upsert(context: Context, models: List<DBReportSymptom>) {
        try {
            Database.getInstance(context).getReportedSymptoms().upsert(models)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on upsert reported symptoms to database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun delete(context: Context, idSymptom: Long) {
        try {
            Database.getInstance(context).getReportedSymptoms().delete(idSymptom)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on delete reported symptom with id $idSymptom from database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun truncate(context: Context) {
        try {
            Database.getInstance(context).getReportedSymptoms().truncate()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on truncate reported symptoms from database ${e.localizedMessage}")
        }
    }
}
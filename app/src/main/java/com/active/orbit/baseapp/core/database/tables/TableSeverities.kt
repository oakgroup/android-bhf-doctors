package com.active.orbit.baseapp.core.database.tables

import android.content.Context
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.core.utils.Logger

object TableSeverities {

    @WorkerThread
    fun getAll(context: Context, idSymptom: String): List<DBSeverity> {
        try {
            return Database.getInstance(context).getSeverities().getAllForSymptom(idSymptom)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting all options for symptom $idSymptom from database ${e.message}")
        }
        return arrayListOf()
    }

    @WorkerThread
    fun getById(context: Context, idSymptom: String, idSeverity: String): DBSeverity? {
        try {
            return Database.getInstance(context).getSeverities().getByIdForSymptom(idSymptom, idSeverity)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting option by id $idSeverity for symptom $idSymptom from database ${e.message}")
        }
        return null
    }

    @WorkerThread
    fun upsert(context: Context, model: DBSeverity) {
        upsert(context, listOf(model))
    }

    @WorkerThread
    fun upsert(context: Context, models: List<DBSeverity>) {
        try {
            Database.getInstance(context).getSeverities().upsert(models)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on upsert options to database ${e.message}")
        }
    }

    @WorkerThread
    fun delete(context: Context, idSeverity: Long) {
        try {
            Database.getInstance(context).getSeverities().delete(idSeverity)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on delete option with id $idSeverity from database ${e.message}")
        }
    }

    @WorkerThread
    fun truncate(context: Context) {
        try {
            Database.getInstance(context).getSeverities().truncate()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on truncate options from database ${e.message}")
        }
    }
}
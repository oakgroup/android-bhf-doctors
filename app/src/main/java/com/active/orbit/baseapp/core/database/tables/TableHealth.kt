package com.active.orbit.baseapp.core.database.tables

import android.content.Context
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.utils.Logger

object TableHealth {

    @WorkerThread
    fun getAll(context: Context): List<DBHealth> {
        try {
            return Database.getInstance(context).getHealth().getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting all reported symptoms from database ${e.localizedMessage}")
        }
        return arrayListOf()
    }

    @WorkerThread
    fun getById(context: Context, idHealth: String): DBHealth? {
        try {
            return Database.getInstance(context).getHealth().getById(idHealth)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting reported health by id $idHealth${e.localizedMessage}")
        }
        return null
    }

    @WorkerThread
    fun getNotUploaded(context: Context, uploaded: Boolean): List<DBHealth> {
        try {
            return Database.getInstance(context).getHealth().getNotUploaded(uploaded)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting reported health not uploaded ${e.localizedMessage}")
        }
        return arrayListOf()
    }


    @WorkerThread
    fun upsert(context: Context, model: DBHealth) {
        upsert(context, listOf(model))
    }

    @WorkerThread
    fun update(context: Context, model: DBHealth) {
        update(context, model)
    }

    @WorkerThread
    fun upsert(context: Context, models: List<DBHealth>) {
        try {
            Database.getInstance(context).getHealth().upsert(models)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on upsert health to database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun delete(context: Context, idSymptom: Long) {
        try {
            Database.getInstance(context).getHealth().delete(idSymptom)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on delete health with id $idSymptom from database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun truncate(context: Context) {
        try {
            Database.getInstance(context).getHealth().truncate()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on truncate health from database ${e.localizedMessage}")
        }
    }
}
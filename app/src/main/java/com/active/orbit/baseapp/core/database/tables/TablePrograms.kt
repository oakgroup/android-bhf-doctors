package com.active.orbit.baseapp.core.database.tables

import android.content.Context
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.database.models.DBProgram
import com.active.orbit.baseapp.core.utils.Logger

object TablePrograms {

    @WorkerThread
    fun getAll(context: Context): List<DBProgram> {
        try {
            return Database.getInstance(context).getPrograms().getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting all programs from database ${e.localizedMessage}")
        }
        return arrayListOf()
    }

    @WorkerThread
    fun getById(context: Context, idProgram: String): DBProgram? {
        try {
            return Database.getInstance(context).getPrograms().getById(idProgram)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting program by id $idProgram from database ${e.localizedMessage}")
        }
        return null
    }

    @WorkerThread
    fun upsert(context: Context, model: DBProgram) {
        upsert(context, listOf(model))
    }

    @WorkerThread
    fun upsert(context: Context, models: List<DBProgram>) {
        try {
            Database.getInstance(context).getPrograms().upsert(models)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on upsert programs to database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun delete(context: Context, idProgram: Long) {
        try {
            Database.getInstance(context).getPrograms().delete(idProgram)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on delete program with id $idProgram from database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun truncate(context: Context) {
        try {
            Database.getInstance(context).getPrograms().truncate()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on truncate programs from database ${e.localizedMessage}")
        }
    }
}
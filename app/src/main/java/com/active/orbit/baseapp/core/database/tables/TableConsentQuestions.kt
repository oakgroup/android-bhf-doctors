package com.active.orbit.baseapp.core.database.tables

import android.content.Context
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.database.models.DBConsentQuestion
import com.active.orbit.baseapp.core.utils.Logger

object TableConsentQuestions {

    @WorkerThread
    fun getAll(context: Context): List<DBConsentQuestion> {
        try {
            return Database.getInstance(context).getConsentQuestions().getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting all consent questions from database ${e.localizedMessage}")
        }
        return arrayListOf()
    }

    @WorkerThread
    fun getById(context: Context, questionID: String): DBConsentQuestion? {
        try {
            return Database.getInstance(context).getConsentQuestions().getById(questionID)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on getting question by id $questionID${e.localizedMessage}")
        }
        return null
    }


    @WorkerThread
    fun upsert(context: Context, model: DBConsentQuestion) {
        upsert(context, listOf(model))
    }

    @WorkerThread
    fun update(context: Context, model: DBConsentQuestion) {
        update(context, model)
    }

    @WorkerThread
    fun upsert(context: Context, models: List<DBConsentQuestion>) {
        try {
            Database.getInstance(context).getConsentQuestions().upsert(models)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on upsert consent question to database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun delete(context: Context, questionID: String) {
        try {
            Database.getInstance(context).getConsentQuestions().delete(questionID)
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on delete consent question with id $questionID from database ${e.localizedMessage}")
        }
    }

    @WorkerThread
    fun truncate(context: Context) {
        try {
            Database.getInstance(context).getConsentQuestions().truncate()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Error on truncate consent questions from database ${e.localizedMessage}")
        }
    }
}
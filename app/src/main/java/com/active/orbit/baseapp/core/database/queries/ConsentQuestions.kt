package com.active.orbit.baseapp.core.database.queries

import androidx.room.*
import com.active.orbit.baseapp.core.database.models.DBConsentQuestion

@Dao
interface ConsentQuestions {

    @Query("SELECT * FROM consent_questions")
    fun getAll(): List<DBConsentQuestion>

    @Query("SELECT * FROM consent_questions WHERE questionID == :questionID LIMIT 1")
    fun getById(questionID: String): DBConsentQuestion?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(models: List<DBConsentQuestion>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(models: List<DBConsentQuestion>)

    fun upsert(model: DBConsentQuestion) {
        upsert(listOf(model))
    }

    @Transaction
    fun upsert(models: List<DBConsentQuestion>) {
        insert(models)
        update(models)
    }

    @Query("DELETE FROM consent_questions WHERE questionID == :questionID")
    fun delete(questionID: String)

    @Query("DELETE FROM consent_questions")
    fun truncate()
}
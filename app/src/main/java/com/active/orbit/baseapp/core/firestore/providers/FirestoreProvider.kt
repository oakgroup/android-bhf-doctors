package com.active.orbit.baseapp.core.firestore.providers

import android.content.Context
import com.active.orbit.baseapp.core.firestore.enums.Collections
import com.active.orbit.baseapp.core.firestore.enums.Fields
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FirestoreProvider {

    private val database = FirebaseFirestore.getInstance()

    companion object {

        @Volatile
        private var instance: FirestoreProvider? = null

        @Synchronized
        fun getInstance(): FirestoreProvider {
            if (instance == null) {
                synchronized(FirestoreProvider::class.java) {
                    // double check locking
                    if (instance == null) {
                        instance = FirestoreProvider()

                        // enable offline cached data
                        val settings = FirebaseFirestoreSettings.Builder()
                            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                            .build()
                        instance!!.database.firestoreSettings = settings
                    }
                }
            }
            return instance!!
        }
    }

    fun updateUserDetails(context: Context) {
        if (Preferences.user(context).isUserRegistered()) {
            generateUserDetailsDocument(context)
                .addOnSuccessListener {
                    Logger.d("Firestore provider update user details write success")
                    Preferences.lifecycle(context).userDetailsUploaded = true
                }
                .addOnFailureListener {
                    Logger.e("Firestore provider update user details write failed ${it.localizedMessage}")
                }
        } else {
            Logger.e("Trying to update user details while the user is not registered")
        }
    }

    private fun generateUserDetailsDocument(context: Context): Task<Void> {
        val idUser = Preferences.user(context).idUser ?: Constants.EMPTY
        val hashMap = HashMap<String, Any>()
        hashMap[Fields.USER_ID.value] = idUser
        hashMap[Fields.USER_SEX.value] = Preferences.user(context).userSex ?: Constants.EMPTY
        hashMap[Fields.USER_AGE.value] = Preferences.user(context).userAge ?: Constants.EMPTY
        hashMap[Fields.USER_WEIGHT.value] = Preferences.user(context).userWeight ?: Constants.EMPTY
        hashMap[Fields.USER_HEIGHT.value] = Preferences.user(context).userHeight ?: Constants.EMPTY
        hashMap[Fields.PATIENT_ID.value] = Preferences.user(context).idPatient ?: Constants.EMPTY
        val level1 = database.collection(Collections.USERS.value)
        val level2 = level1.document(idUser)
        return level2.set(hashMap)
    }
}
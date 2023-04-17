package com.active.orbit.baseapp.core.database.engine

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.engine.encryption.DatabaseKeystore
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.demo.core.database.models.DBDemo
import com.active.orbit.baseapp.demo.core.database.tables.TableDemo
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Main database class
 *
 * @author omar.brugna
 */
@androidx.room.Database(entities = [DBDemo::class], version = 1, exportSchema = false)  // DEMO_CODE!
internal abstract class Database : RoomDatabase() {

    companion object {

        private const val encryptionEnabled = true

        @Volatile
        private var instance: Database? = null

        @Synchronized
        fun getInstance(context: Context): Database {
            if (instance == null) {
                synchronized(Database::class.java) {
                    // double check locking
                    if (instance == null) {
                        val builder = Room.databaseBuilder(context, Database::class.java, context.getString(R.string.database_name))
                        builder.fallbackToDestructiveMigration()

                        @Suppress("ConstantConditionIf")
                        if (encryptionEnabled) {
                            // encryption
                            val passphrase: ByteArray = SQLiteDatabase.getBytes(DatabaseKeystore.getSecretKey(context)?.toCharArray())
                            val factory = SupportFactory(passphrase)
                            builder.openHelperFactory(factory)
                        }

                        instance = builder.build()
                    }
                }
            }
            return instance!!
        }
    }

    fun logout() {
        backgroundThread {
            clearAllTables()
        }
    }

    abstract fun getDemo(): TableDemo // DEMO_CODE!
}
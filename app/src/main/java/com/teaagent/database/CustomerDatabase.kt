package com.teaagent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teaagent.domain.CustomerEntity

// 1
@Database(entities = [CustomerEntity::class], version = 4)

// 2
abstract class CustomerDatabase : RoomDatabase() {

    // 3
    abstract fun getTrackingDao(): CustomerDao

    companion object {
        // 4
        @Volatile
        private var INSTANCE: CustomerDatabase? = null


        fun getDatabase(context: Context): CustomerDatabase {

            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CustomerDatabase::class.java,
                    "run_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE!!
            }
        }
    }
}
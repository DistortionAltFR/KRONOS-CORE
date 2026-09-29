package com.kronos.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kronos.core.data.local.dao.AlertDao
import com.kronos.core.data.local.dao.ServerDao
import com.kronos.core.data.local.entity.AlertEntity
import com.kronos.core.data.local.entity.ServerEntity

@Database(
    entities = [ServerEntity::class, AlertEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KronosDatabase : RoomDatabase() {

    abstract fun serverDao(): ServerDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: KronosDatabase? = null

        fun getDatabase(context: Context): KronosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KronosDatabase::class.java,
                    "kronos_core_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

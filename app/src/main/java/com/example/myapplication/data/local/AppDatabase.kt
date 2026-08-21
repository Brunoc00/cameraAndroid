package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Foto::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun fotoDao(): FotoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fotos_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
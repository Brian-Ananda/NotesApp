package com.example.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Word::class),version = 1,exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao():WordDao

    companion object{
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            //checking if database is already created
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            //Creating database
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
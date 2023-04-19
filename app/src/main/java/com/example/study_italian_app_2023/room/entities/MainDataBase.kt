package com.example.study_italian_app_2023.room.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseDataEntityRoom::class, ExerciseDataEntityLayout::class], version = 1)
abstract class MainDataBase : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null

        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "exercisesData.db"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                instance
            }
        }
    }
}
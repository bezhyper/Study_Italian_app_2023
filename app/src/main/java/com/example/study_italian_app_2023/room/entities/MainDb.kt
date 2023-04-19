package com.example.study_italian_app_2023.room.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseDataEntityRoom::class,ExerciseDataEntityLayout::class], version = 1)
abstract class MainDb : RoomDatabase() {


    abstract fun getDao(): Dao

    companion object {

        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "exercisesData.db"
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        }

    }
}

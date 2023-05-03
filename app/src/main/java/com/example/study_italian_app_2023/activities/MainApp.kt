package com.example.study_italian_app_2023.activities

import android.app.Application
import com.example.study_italian_app_2023.retrofit.entities.Retrofit_Instance
import com.example.study_italian_app_2023.room.entities.MainDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainApp: Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
    val repository by lazy{ Retrofit_Instance.getInstance()}

//    retrofitApi.create(DataApi::class.java



    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            database.getDao().resetPrimaryKeyEntityRoom()
        }

        CoroutineScope(Dispatchers.IO).launch {
            database.getDao().resetPrimaryKeyEntityLayout()
        }

        CoroutineScope(Dispatchers.IO).launch {
            database.getDao().deleteAllInExercises()
        }

        CoroutineScope(Dispatchers.IO).launch {
            database.getDao().deleteAllInLayout()
        }
    }
}
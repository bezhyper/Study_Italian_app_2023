package com.example.study_italian_app_2023.activities

import android.app.Application
import com.example.study_italian_app_2023.retrofit.entities.Retrofit_Instance
import com.example.study_italian_app_2023.room.entities.MainDataBase

class MainApp: Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
    val repository by lazy{ Retrofit_Instance.getInstance()}

//    retrofitApi.create(DataApi::class.java
}
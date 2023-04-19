package com.example.study_italian_app_2023.retrofit.entities

import android.content.Context
import androidx.room.Room
import com.example.study_italian_app_2023.room.entities.MainDb
import com.example.study_italian_app_2023.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class Retrofit_Instance {

    companion object {

        val retrofitApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        }
}
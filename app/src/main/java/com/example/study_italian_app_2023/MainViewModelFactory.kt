package com.example.study_italian_app_2023

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.study_italian_app_2023.retrofit.entities.DataApi
//
//class MainViewModelFactory(private val exercisesFunctions: ExercisesFunctions, val retrofit: DataApi) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            //, database поместить в конструктор ниже
//            return MainViewModel(exercisesFunctions, retrofit) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModelClass")
//    }
//}

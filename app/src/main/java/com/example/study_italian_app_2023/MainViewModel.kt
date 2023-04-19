package com.example.study_italian_app_2023

import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.study_italian_app_2023.activities.MainApp
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom
import com.example.study_italian_app_2023.room.entities.MainDataBase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
//private val exercisesFunctions: ExercisesFunctions, database: MainDataBase - поместить в конструктор что ниже
class MainViewModel(private val exercisesFunctions: ExercisesFunctions) : ViewModel() {

//    val dao = database.getDao()

    private var _index = MutableLiveData<Int>()
    var index: LiveData<Int> = _index


    private var _loadedExercise = MutableLiveData<ExerciseDataEntityRoom>()
    var loadedExercise: LiveData<ExerciseDataEntityRoom> = _loadedExercise



    fun onButtonAnswerPressed(currentAnswerButton: Button){
     exercisesFunctions.onButtonAnswerPressed(currentAnswerButton)
    }

    fun onButtonNextExPressed(){
        exercisesFunctions.onButtonNextExPressed()

    }


    fun onButtonPrevExPressed(){
        exercisesFunctions.onButtonPrevExPressed()

    }
























//    val allExercises: LiveData<ExerciseDataEntityRoom> = dao.getAllData().asLiveData()
//    - написать получение одного экземпляра

//    fun insertExercise(exercise: ExerciseDataEntityRoom) = viewModelScope.launch {
//        dao.insertExerciseData(exercise)
//    }






//, val database: MainDataBase - поместить в конструктор ниже
    class MainViewModelFactory(private val exercisesFunctions: ExercisesFunctions) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                //, database поместить в конструктор ниже
                return MainViewModel(exercisesFunctions) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }


}
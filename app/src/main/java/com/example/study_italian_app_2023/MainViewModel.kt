package com.example.study_italian_app_2023

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.example.study_italian_app_2023.activities.MainActivity
import com.example.study_italian_app_2023.activities.ThirdActivity
import com.example.study_italian_app_2023.retrofit.entities.DataApi
import com.example.study_italian_app_2023.retrofit.entities.ExerciseDataEntityRetrofit
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityLayout
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom
import com.example.study_italian_app_2023.room.entities.MainDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//private val exercisesFunctions: ExercisesFunctions, database: MainDataBase - поместить в конструктор что ниже
class MainViewModel(database: MainDataBase, val repository: DataApi, private val exercisesFunctions: ExercisesFunctions) : ViewModel() {

    val dao = database.getDao()




    private var _index = MutableLiveData<Int>()
    var index: LiveData<Int> = _index


    private var _exercise = MutableLiveData<ExerciseDataEntityRetrofit>()
    var exercise: LiveData<ExerciseDataEntityRetrofit> = _exercise







    private val test =  _index.observeForever {

        viewModelScope.launch {
            val exerciseDataRepository =  repository.getRandomExercise(1)
            val exerciseDataRoom = exerciseDataRepository.toExerciseData()

            dao.insertExerciseData(exerciseDataRoom)
            _exercise.value = exerciseDataRepository
        }

    }






    fun onButtonAnswerPressed(currentAnswerButton: Button){
    exercisesFunctions.onButtonAnswerPressed(currentAnswerButton)

    }

    fun onButtonNextExPressed(){
        exercisesFunctions.onButtonNextExPressed(_index)

    }

    fun onButtonPrevExPressed(){
        exercisesFunctions.onButtonPrevExPressed(_index)


    }

    override fun onCleared() {
        CoroutineScope(Dispatchers.IO).launch {


            dao.resetPrimaryKeyEntityRoom()

        }

        CoroutineScope(Dispatchers.IO).launch {
            dao.resetPrimaryKeyEntityLayout()

        }


        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllInExercises()

        }
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllInLayout()
        }
    }

    fun getExercise() {
        viewModelScope.launch {
           val exerciseDataRepository =  repository.getRandomExercise(1)
            val exerciseDataRoom = exerciseDataRepository.toExerciseData()

            dao.insertExerciseData(exerciseDataRoom)
            _exercise.value = exerciseDataRepository

        }
    }

class MainViewModelFactory(val database: MainDataBase, val repository: DataApi, private val exercisesFunctions: ExercisesFunctions) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            //, database поместить в конструктор ниже
            return MainViewModel(database, repository, exercisesFunctions) as T
        }
        throw IllegalArgumentException("Unknown ViewModelClass")
    }
}


}
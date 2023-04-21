package com.example.study_italian_app_2023

import android.widget.Button
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExercisesFunctions {

    private var i = 1
   // как предварительно задать в  _index.value значение 1

    fun onButtonAnswerPressed(currentAnswerButton: Button) {

    }


    fun onButtonPrevExPressed(_index: MutableLiveData<Int>) {
        i++
        _index.value = i

    }


    fun onButtonNextExPressed(_index: MutableLiveData<Int>) {
        i--
        _index.value = i
    }


//    fun indexChanges(_index: MutableLiveData<Int>){
//        if (_index.value == 3){
//
//
//        }
//        else{
//
//
//        }
//
//    }

}






















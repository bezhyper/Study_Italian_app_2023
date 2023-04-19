package com.example.study_italian_app_2023.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.study_italian_app_2023.ExercisesFunctions
import com.example.study_italian_app_2023.MainViewModel
import com.example.study_italian_app_2023.R
import com.example.study_italian_app_2023.databinding.ActivityThirdBinding
import kotlinx.coroutines.*



import kotlin.random.Random

class ThirdActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityThirdBinding
    private val mainViewModel: MainViewModel by viewModels {
       MainViewModel.MainViewModelFactory(exercisesFunctions = ExercisesFunctions())
//       MainViewModel.MainViewModelFactory((context.applicationContext as MainApp).database)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityThirdBinding.inflate(layoutInflater).also { setContentView(it.root) }




        bindingClass.root.forEach {
            if (it is Button) it.setOnClickListener(buttonAnswerListener)
        }

//        mainViewModel.index.observe(this, Observer {
//            mainViewModel.index = it
//        })

    }


    private val buttonAnswerListener = View.OnClickListener() {

        when (it.id) {

            R.id.buttonAnswer1 -> {
                mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer1)
            }

            R.id.buttonAnswer2 ->
            {   mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer2) }

            R.id.buttonAnswer3 ->
            {   mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer3) }

            R.id.buttonAnswer4 ->
            {   mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer4) }

            R.id.buttonNextEx ->
            {   mainViewModel.onButtonNextExPressed()}

            R.id.buttonPrevEx -> {
                mainViewModel.onButtonPrevExPressed()}
        }
    }





}
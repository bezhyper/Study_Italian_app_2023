package com.example.study_italian_app_2023.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import com.example.study_italian_app_2023.ExercisesFunctions
import com.example.study_italian_app_2023.MainViewModel
import com.example.study_italian_app_2023.R
import com.example.study_italian_app_2023.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityThirdBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database,
            (applicationContext as MainApp).repository,
            exercisesFunctions = ExercisesFunctions()
        )
//        MainViewModel.MainViewModelFactory(exercisesFunctions = ExercisesFunctions(), database)
//       MainViewModel.MainViewModelFactory((context.applicationContext as MainApp).database)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityThirdBinding.inflate(layoutInflater).also { setContentView(it.root) }


        bindingClass.root.forEach {
            if (it is Button) it.setOnClickListener(buttonAnswerListener)
        }



        mainViewModel.exercise.observe(this, Observer {

            bindingClass.textSentence.text = mainViewModel.exercise.value?.sentens
            bindingClass.buttonAnswer1.text = mainViewModel.exercise.value?.wrong_1
            bindingClass.buttonAnswer2.text = mainViewModel.exercise.value?.wrong_2
            bindingClass.buttonAnswer3.text = mainViewModel.exercise.value?.wrong_3
            bindingClass.buttonAnswer4.text = mainViewModel.exercise.value?.correct





//            bindingClass.textSentence.text = mainViewModel.exercise.value?.sentens
//            bindingClass.buttonAnswer1.text = mainViewModel.exercise.value?.b1
//            bindingClass.buttonAnswer2.text = mainViewModel.exercise.value?.b2
//            bindingClass.buttonAnswer3.text = mainViewModel.exercise.value?.b3
//            bindingClass.buttonAnswer4.text = mainViewModel.exercise.value?.b4

//            if (mainViewModel.exercise.value.chosen_answer == mainViewModel.exercise.value.correct) {
//// ВЫДЕЛЯТЬ ПРАВИЛЬНЫЙ ЗЕЛЕНЫМ ЦВЕТОМ
//
//
//            } else {
//// ВЫДЕЛЯТЬ ПРАВИЛЬНЫЙ ПУНКТИРНОЙ ЗЕЛЕНОЙ ЛИНИЕЙ, НЕПРАВИЛЬНЫЙ КРАСНЫМ
//
//
//            }


        })


    }

    override fun onStart() {
        super.onStart()
        mainViewModel.getExercise()
    }


    private val buttonAnswerListener = View.OnClickListener() {

        when (it.id) {

            R.id.buttonAnswer1 -> {
                mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer1)
            }

            R.id.buttonAnswer2 -> {
                mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer2)
            }

            R.id.buttonAnswer3 -> {
                mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer3)
            }

            R.id.buttonAnswer4 -> {
                mainViewModel.onButtonAnswerPressed(bindingClass.buttonAnswer4)
            }

            R.id.buttonNextEx -> {
                mainViewModel.onButtonNextExPressed()
            }

            R.id.buttonPrevEx -> {
                mainViewModel.onButtonPrevExPressed()
            }

            R.id.buttonFromThirdToMain -> {
//                mainViewModel.onButtonFromThirdToMainPressed()
                val intent = Intent(this@ThirdActivity, MainActivity::class.java)
                startActivity(intent)
                // ПОКА ЧТО ИНТЕНТ БУДЕТ ЗДЕСЬ!
            }
        }
    }

}
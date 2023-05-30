package com.example.study_italian_app_2023.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStore
import com.example.study_italian_app_2023.ExercisesFunctions
import com.example.study_italian_app_2023.MainViewModel
import com.example.study_italian_app_2023.NetworkStateManager
import com.example.study_italian_app_2023.R
import com.example.study_italian_app_2023.databinding.ActivityThirdBinding
import com.example.study_italian_app_2023.retrofit.entities.DataApi
import com.example.study_italian_app_2023.retrofit.entities.MyNetworkRequest
import com.example.study_italian_app_2023.retrofit.entities.MyRepository


class ThirdActivity : AppCompatActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkStateManager: NetworkStateManager


    val myNetworkRequest: MyNetworkRequest
        get() = MyNetworkRequest((applicationContext as MainApp).repository)

    private lateinit var bindingClass: ActivityThirdBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory(
            (applicationContext as MainApp).database,
            (applicationContext as MainApp).databaseAssets,
            MyRepository(myNetworkRequest),
            exercisesFunctions = ExercisesFunctions(),
            networkStateManager = networkStateManager
        )
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityThirdBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateManager = NetworkStateManager(connectivityManager)


        bindingClass.root.forEach {
            if (it is Button) it.setOnClickListener(buttonAnswerListener)
        }



        mainViewModel.exerciseLayout.observe(this) {

            if (mainViewModel.exerciseLayout.value?.is_answer_correct == null){
                bindingClass.buttonAnswer1.isEnabled = true
                bindingClass.buttonAnswer2.isEnabled = true
                bindingClass.buttonAnswer3.isEnabled = true
                bindingClass.buttonAnswer4.isEnabled = true
            }

            bindingClass.textSentence.text = mainViewModel.exerciseLayout.value!!.sentens

            bindingClass.buttonAnswer1.text = mainViewModel.exerciseLayout.value!!.b1
            bindingClass.buttonAnswer2.text = mainViewModel.exerciseLayout.value!!.b2
            bindingClass.buttonAnswer3.text = mainViewModel.exerciseLayout.value!!.b3
            bindingClass.buttonAnswer4.text = mainViewModel.exerciseLayout.value!!.b4

        }



        mainViewModel.currentChosenAnswer.observe(this) {
            bindingClass.buttonAnswer1.setBackgroundColor(WHITE)
            bindingClass.buttonAnswer2.setBackgroundColor(WHITE)
            bindingClass.buttonAnswer3.setBackgroundColor(WHITE)
            bindingClass.buttonAnswer4.setBackgroundColor(WHITE)

//            it?.setBackgroundColor(BLUE)

            if (mainViewModel.exerciseLayout.value?.is_answer_correct == 0){
                it?.setBackgroundColor(RED)
                bindingClass.buttonAnswer1.isEnabled = false
                bindingClass.buttonAnswer2.isEnabled = false
                bindingClass.buttonAnswer3.isEnabled = false
                bindingClass.buttonAnswer4.isEnabled = false
            }
           else if (mainViewModel.exerciseLayout.value?.is_answer_correct == 1) {
                it?.setBackgroundColor(GREEN)
                bindingClass.buttonAnswer1.isEnabled = false
                bindingClass.buttonAnswer2.isEnabled = false
                bindingClass.buttonAnswer3.isEnabled = false
                bindingClass.buttonAnswer4.isEnabled = false
            }


        }





        mainViewModel.index.observe(this) {


            bindingClass.answerCheck.text = it.toString()


            if (it != 1) {
                bindingClass.buttonPrevEx.visibility = View.VISIBLE
            } else {
                bindingClass.buttonPrevEx.visibility = View.GONE
            }


        }




    }

    override fun onStart() {
        super.onStart()
        mainViewModel.startMonitoringNetworkState()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.stopMonitoringNetworkState()
    }




    private val buttonAnswerListener = View.OnClickListener {

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
                mainViewModel.onButtonFromThirdToMainPressed()
                val intent = Intent(this@ThirdActivity, MainActivity::class.java)
                startActivity(intent)
                // ПОКА ЧТО ИНТЕНТ БУДЕТ ЗДЕСЬ!
            }
        }
    }

}
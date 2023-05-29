package com.example.study_italian_app_2023.room.entities.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.forEach
import com.example.study_italian_app_2023.R
import com.example.study_italian_app_2023.databinding.ActivityMainBinding
import kotlinx.coroutines.*
class MainActivity : AppCompatActivity() {


    lateinit var bindingClass: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        bindingClass = ActivityMainBinding.inflate(layoutInflater)

        setContentView(bindingClass.root)




        bindingClass.buttonToThird.setOnClickListener {
            val intent = Intent(this@MainActivity, ThirdActivity::class.java)
            startActivity(intent)
        }

        bindingClass.buttonExit.setOnClickListener {
            finishAffinity()
        }




    }


}







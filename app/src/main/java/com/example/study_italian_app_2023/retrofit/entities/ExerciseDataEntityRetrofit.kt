package com.example.study_italian_app_2023.retrofit.entities

import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom

data class ExerciseDataEntityRetrofit(


    val correct: String,


    var id: Int? = null,


    var sentens: String,


    val type_task: String,


    val wrong_1: String,


    val wrong_2: String,


    val wrong_3: String,




    ) {
    fun toExerciseData(): ExerciseDataEntityRoom = ExerciseDataEntityRoom(

        correct = correct,


        id = id,


        sentens = sentens,


        type_task = type_task,


        wrong_1 = wrong_1,


        wrong_2 = wrong_2,


        wrong_3 = wrong_3
    )


}

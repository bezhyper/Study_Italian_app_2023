package com.example.study_italian_app_2023.room.entities

data class ExerciseDataInTuple(
    val count: Int?,

    val correct: String,


    var sentens: String,


    val wrong_1: String,


    val wrong_2: String,


    val wrong_3: String,


    val chosen_answer: String?,


    val is_answer_correct: Int? = null
)


data class UpdateExerciseDataInTuple(

    val count: Int? = null,

    val chosen_answer: String,


    val is_answer_correct: Int? = null

)



package com.example.study_italian_app_2023.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseDataEntityRoom(


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "count")
    val count: Int? = null,

    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "typeTask")
    val type_task: String,

    @ColumnInfo(name = "sentens")
    var sentens: String,

    @ColumnInfo(name = "correct")
    val correct: String,

    @ColumnInfo(name = "wrong_1")
    val wrong_1: String,

    @ColumnInfo(name = "wrong_2")
    val wrong_2: String,

    @ColumnInfo(name = "wrong_3")
    val wrong_3: String,

    @ColumnInfo(name = "chosen_answer")
    val chosen_answer: String? = null,

    @ColumnInfo(name = "is_answer_correct")
    val is_answer_correct: Int? = null


)



package com.example.study_italian_app_2023.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "layout")
class ExerciseDataEntityLayout(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "count")
    val count: Int? = null,

    @ColumnInfo(name = "sentens")
    val sentens: String? = null,

    @ColumnInfo(name = "b1")
    val b1: String? = null,

    @ColumnInfo(name = "b2")
    val b2: String? = null,

    @ColumnInfo(name = "b3")
    val b3: String? = null,

    @ColumnInfo(name = "b4")
    val b4: String? = null,

    @ColumnInfo(name = "correct")
    val correct: String? = null,

    @ColumnInfo(name = "chosen_answer")
    val chosen_answer: String? = null,

    @ColumnInfo(name = "is_answer_correct")
    val is_answer_correct: Int? = null
) {


}
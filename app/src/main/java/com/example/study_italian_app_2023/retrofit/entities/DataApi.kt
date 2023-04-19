package com.example.study_italian_app_2023.retrofit.entities

import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DataApi {
    @GET("random_task")
    suspend fun getRandomExercise(@Query("user_id") user_id : Int): ExerciseDataEntityRetrofit

    @POST("task_completed")
    suspend fun postRightCompletedExercise(@Query("user_id") user_id : Int, @Query("task_id") task_id : Int): ExerciseDataEntityRoom
}
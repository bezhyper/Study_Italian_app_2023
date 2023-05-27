package com.example.study_italian_app_2023.retrofit.entities

import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException

interface DataApi {
    @GET("random_task")
    suspend fun getRandomExercise(@Query("user_id") user_id : Int):  Response<ExerciseDataEntityRetrofit>

    @POST("task_completed")
    suspend fun postRightCompletedExercise(@Query("user_id") user_id : Int, @Query("task_id") task_id : Int): ExerciseDataEntityRoom
}

class MyNetworkRequest(val service: DataApi) {
    suspend fun getRandomExercise(): Response<ExerciseDataEntityRetrofit> {
        return withContext(Dispatchers.IO) {
            service.getRandomExercise(1)
        }
    }
}




class MyRepository(private val networkRequest: MyNetworkRequest) {
     suspend fun getRandomExercise(): ExerciseDataEntityRetrofit {
         val response = try {
             networkRequest.getRandomExercise()
         } catch (e: IOException) {
             throw IOException("Я БЫДЛО ЭТО ЧТО РАБОТАЕТ?!")
         }

         if (response.isSuccessful) {
             return response.body() ?: throw IOException("Empty response body")
         } else {
             throw HttpException(response)
         }
    }

    suspend fun postRightCompletedExercise(user_id: Int, task_id: Int){

            networkRequest.service.postRightCompletedExercise(user_id, task_id)

    }



}
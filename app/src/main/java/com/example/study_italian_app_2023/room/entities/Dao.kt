package com.example.study_italian_app_2023.room.entities

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert
    fun insertExerciseData(data: ExerciseDataEntityRoom)

    @Query("SELECT * FROM exercises")
    fun getAllData(): List<ExerciseDataEntityRoom>

    //    select *from getLastRecord ORDER BY id DESC LIMIT 1
    @Query("SELECT * FROM exercises ORDER BY count DESC LIMIT 1")
    fun getExerciseWithFlow(): Flow<ExerciseDataEntityRoom>
//    getExercise: ExerciseDataInTuple

    @Query("SELECT * FROM exercises ORDER BY count DESC LIMIT 1")
    fun getExerciseWithoutFlow(): ExerciseDataEntityRoom

    @Query("SELECT * FROM exercises  WHERE count = :i")
    fun getPrevExercise(i: Int): ExerciseDataInTuple


    @Query("SELECT * FROM layout  WHERE count = :i")
    fun getExerciseLayout(i: Int): ExerciseDataEntityLayout



    @Update(entity = ExerciseDataEntityRoom::class)
    fun updateAnswerDataRoom(updateData: UpdateExerciseDataInTuple)

    @Update(entity = ExerciseDataEntityLayout::class)
    fun updateAnswerDataLayout(updateData: UpdateExerciseDataInTuple)


    @Update
    fun updateLayoutData(data: ExerciseDataEntityLayout)

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'exercises'")
    fun resetPrimaryKeyEntityRoom()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'layout'")
    fun resetPrimaryKeyEntityLayout()

    @Insert
    fun insertExerciseDataLayout(data: ExerciseDataEntityLayout)



    @Query("DELETE FROM exercises")
    fun deleteAllInExercises()

    @Query("DELETE FROM layout")
    fun deleteAllInLayout()


}
package com.example.study_italian_app_2023

import android.widget.Button
import androidx.lifecycle.*
import com.example.study_italian_app_2023.retrofit.entities.DataApi
import com.example.study_italian_app_2023.retrofit.entities.ExerciseDataEntityRetrofit
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityLayout
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom
import com.example.study_italian_app_2023.room.entities.MainDataBase
import kotlinx.coroutines.*

//private val exercisesFunctions: ExercisesFunctions, database: MainDataBase - поместить в конструктор что ниже
class MainViewModel(
    database: MainDataBase,
    val repository: DataApi,
    private val exercisesFunctions: ExercisesFunctions
) : ViewModel() {

    val dao = database.getDao()


    private var _index = MutableLiveData<Int>(1)
    var index: LiveData<Int> = _index


    private var _exercise = MutableLiveData<ExerciseDataEntityRetrofit>()
    var exercise: LiveData<ExerciseDataEntityRetrofit> = _exercise

    private var _exerciseLayout = MutableLiveData<ExerciseDataEntityLayout>()
    var exerciseLayout: LiveData<ExerciseDataEntityLayout> = _exerciseLayout

//    private var _count = MutableLiveData<Int?>()
//    var count: LiveData<Int?> = _count

    private val test = _index.observeForever {

       val count = dao.getExerciseWithoutFlow().count

        if(_index.value == count!!.plus(1) )
        getAndLayoutNewExercise()
        else
            getAndLayoutExercise(_index.value!!)

    }

    private fun getAndLayoutExercise(_index: Int) {
        viewModelScope.launch {
            dao.getPrevExercise(_index)
        }
    }


    fun onButtonAnswerPressed(currentAnswerButton: Button) {
        exercisesFunctions.onButtonAnswerPressed(currentAnswerButton)

    }

    fun onButtonNextExPressed() {
        exercisesFunctions.onButtonNextExPressed(_index)

    }

    fun onButtonPrevExPressed() {
        exercisesFunctions.onButtonPrevExPressed(_index)


    }

    override fun onCleared() {
        CoroutineScope(Dispatchers.IO).launch {


            dao.resetPrimaryKeyEntityRoom()

        }

        CoroutineScope(Dispatchers.IO).launch {
            dao.resetPrimaryKeyEntityLayout()

        }


        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllInExercises()

        }
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllInLayout()
        }
    }

    fun getAndLayoutNewExercise() {
        viewModelScope.launch {
            val exerciseDataRepository = repository.getRandomExercise(1)
            val exerciseDataRoom = exerciseDataRepository.toExerciseData()



            dao.insertExerciseData(exerciseDataRoom)



            insertDataInEntityLayout(exerciseDataRoom)

        }
    }

    private fun insertDataInEntityLayout(exerciseDataRoom: ExerciseDataEntityRoom) {
        viewModelScope.launch {
            val listAnswers = createListAnswers(exerciseDataRoom)

            val b1 = getRandomAnswer(listAnswers)
            val b2 = getRandomAnswer(listAnswers)
            val b3 = getRandomAnswer(listAnswers)
            val b4 = getRandomAnswer(listAnswers)


            val exercise = ExerciseDataEntityLayout(
                sentens = exerciseDataRoom.sentens,
                b1 = b1,
                b2 = b2,
                b3 = b3,
                b4 = b4,
                correct = exerciseDataRoom.correct
            )

            dao.insertExerciseDataLayout(exercise)

            _exerciseLayout.value = exercise


        }

    }

    private fun createListAnswers(exerciseDataRoom: ExerciseDataEntityRoom) =
        mutableListOf(
            exerciseDataRoom.wrong_1,
            exerciseDataRoom.wrong_2,
            exerciseDataRoom.wrong_3,
            exerciseDataRoom.correct
        )


    private fun getRandomAnswer(listAnswers: MutableList<String>) = runBlocking {


        val answer = async {

            val randomAnswer = listAnswers.shuffled().last()

            listAnswers.remove(randomAnswer)
            return@async randomAnswer

        }.await()
        return@runBlocking answer

    }

    class MainViewModelFactory(
        val database: MainDataBase,
        val repository: DataApi,
        private val exercisesFunctions: ExercisesFunctions
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                //, database поместить в конструктор ниже
                return MainViewModel(database, repository, exercisesFunctions) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }


}
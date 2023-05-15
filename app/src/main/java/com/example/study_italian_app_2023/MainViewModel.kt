package com.example.study_italian_app_2023

import android.graphics.Color
import android.util.Log
import android.widget.Button
import androidx.lifecycle.*
import com.example.study_italian_app_2023.retrofit.entities.DataApi
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityLayout
import com.example.study_italian_app_2023.room.entities.ExerciseDataEntityRoom
import com.example.study_italian_app_2023.room.entities.MainDataBase
import com.example.study_italian_app_2023.room.entities.UpdateExerciseDataInTuple
import kotlinx.coroutines.*

//private val exercisesFunctions: ExercisesFunctions, database: MainDataBase - поместить в конструктор что ниже
class MainViewModel(
    database: MainDataBase,
    val repository: DataApi,
    private val exercisesFunctions: ExercisesFunctions
) : ViewModel() {

    private val dao = database.getDao()


    private var _currentChosenAnswer = MutableLiveData<Button?>()
    var currentChosenAnswer: LiveData<Button?> = _currentChosenAnswer

        var listOfButtons = mutableListOf<Button?>()



//    private var _chosenAnswersList = MutableLiveData<List<Button>>()
//    var chosenAnswerList : LiveData<List<Button>> = _chosenAnswersList


    private var _index = MutableLiveData<Int>().apply { value = 1 }
    var index: LiveData<Int> = _index


    private var _exerciseLayout = MutableLiveData<ExerciseDataEntityLayout>()
    var exerciseLayout: LiveData<ExerciseDataEntityLayout> = _exerciseLayout


    private val test = _index.observeForever {

        val count = dao.getExerciseWithoutFlow()?.count ?: 0

        if ((_index.value == count.plus(1))) {
            getAndLayoutNewExercise()
            listOfButtons.add(null)
        } else
            getAndLayoutExercise(_index.value!!)

        _currentChosenAnswer.value = listOfButtons[_index.value!!.minus(1)]


    }

    private fun getAndLayoutExercise(_index: Int) {
        viewModelScope.launch {
//            _chosenAnswersList?.value.

            val exercise = dao.getExercise(_index)

            _exerciseLayout.value = exercise

        }
    }


    fun onButtonAnswerPressed(currentAnswerButton: Button) {

        val deferred = CompletableDeferred<Unit>()





        viewModelScope.launch {

            checkAnswer(currentAnswerButton, _index.value!!, deferred)
            deferred.await()

            val exercise = dao.getExercise(_index.value!!)



            _exerciseLayout.setValue(exercise)


            listOfButtons.set(_index.value!!.minus(1), currentAnswerButton)

            _currentChosenAnswer.value = listOfButtons[_index.value!!.minus(1)]


            Log.d(
                "ADebugTag",
                "Value: ${_exerciseLayout.value?.is_answer_correct ?: "ало паказивай да ченить"}"
            )

            Log.d(
                "ADebugTag",
                "SIZE: ${listOfButtons.size} VALUES: ${listOfButtons}"
            )


        }


    }

    private fun checkAnswer(
        currentAnswerButton: Button,
        _index: Int,
        deferred: CompletableDeferred<Unit>
    ) {
        viewModelScope.launch {
            val updatedData: UpdateExerciseDataInTuple
            val exercise = dao.getExercise(_index)

            if (currentAnswerButton.text == exercise.correct) {

                updatedData = UpdateExerciseDataInTuple(
                    count = exercise.count,
                    chosen_answer = currentAnswerButton.text.toString(),
                    is_answer_correct = 1

                )

                val idTask = dao.getExerciseRoom(_index).id
                repository.postRightCompletedExercise(1, idTask!!)

            } else {
                updatedData = UpdateExerciseDataInTuple(
                    count = exercise.count,
                    chosen_answer = currentAnswerButton.text.toString(),
                    is_answer_correct = 0
                )
            }
            dao.updateAnswerDataRoom(updatedData)

            dao.updateAnswerDataLayout(updatedData)

            deferred.complete(Unit)
        }

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

    fun onButtonFromThirdToMainPressed() {
        onCleared()
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
package com.example.study_italian_app_2023

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Button
import androidx.lifecycle.*
import com.example.study_italian_app_2023.retrofit.entities.MyRepository
import com.example.study_italian_app_2023.room.entities.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.random.Random


class MainViewModel(
    database: MainDataBase,
    databaseAssets: DataBaseAssets,
    val repository: MyRepository,
    private val exercisesFunctions: ExercisesFunctions,
    private val networkStateManager: NetworkStateManager
) : ViewModel() {

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    init {
        networkStateManager.getNetworkStateLiveData().observeForever { networkState ->
            _isConnected.value = networkState.isConnected
        }
    }
    fun startMonitoringNetworkState() {
        networkStateManager.startMonitoring()
    }

    fun stopMonitoringNetworkState() {
        networkStateManager.stopMonitoring()
    }








    private val dao = database.getDao()
    private val daoAssets = databaseAssets.getDao()



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

            var exercise = dao.getExercise(_index)

            if (exercise.chosen_answer != null){
                exercise.sentens = exercise.sentens!!.replace("<пропуск>",exercise.chosen_answer!!)
            }else
            exercise.sentens = exercise.sentens!!.replace("<пропуск>","_______")


            _exerciseLayout.value = exercise

        }
    }


    fun onButtonAnswerPressed(currentAnswerButton: Button) {

        val deferred = CompletableDeferred<Unit>()


        viewModelScope.launch {

            checkAnswer(currentAnswerButton, _index.value!!, deferred)
            deferred.await()

            val exercise = dao.getExercise(_index.value!!)

            exercise.sentens = exercise.sentens!!.replace("<пропуск>",exercise.chosen_answer!!)



            _exerciseLayout.setValue(exercise)


            listOfButtons.set(_index.value!!.minus(1), currentAnswerButton)

            _currentChosenAnswer.value = listOfButtons[_index.value!!.minus(1)]


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

                if (_isConnected.value == true) {
                    val idTask = dao.getExerciseRoom(_index).id
                    repository.postRightCompletedExercise(1, idTask!!)
                }

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

            try {
                val exerciseDataRepository = repository.getRandomExercise()

                val exerciseDataRoom = exerciseDataRepository.toExerciseData()



                dao.insertExerciseData(exerciseDataRoom)


                insertDataInEntityLayout(exerciseDataRoom)

            } catch (e: IOException) {
                Log.e("Network Error", e.message, e)
                getExerciseFromAssets()

            } catch (e: HttpException) {
                Log.e("HTTP Error", e.message, e)
            }catch (e: Exception) {
                Log.e("Retrofit Error", e.message, e)
            }
        }
    }

    private fun getExerciseFromAssets(){
        viewModelScope.launch {



            val randomNumber =  Random.nextInt(1, 1858)

            val exercise = daoAssets.getExercise(randomNumber)

            val exerciseDataRoom = exercise.toExerciseData()

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


            var exercise = ExerciseDataEntityLayout(
                sentens = exerciseDataRoom.sentens,
                b1 = b1,
                b2 = b2,
                b3 = b3,
                b4 = b4,
                correct = exerciseDataRoom.correct
            )





            dao.insertExerciseDataLayout(exercise)




            exercise.sentens = exercise.sentens!!.replace("<пропуск>","_______")

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
        val databaseAssets: DataBaseAssets,
        val repository: MyRepository,
        private val exercisesFunctions: ExercisesFunctions,
        private val networkStateManager: NetworkStateManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")

                return MainViewModel(database, databaseAssets, repository, exercisesFunctions, networkStateManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }


}
package com.smolianinovasiuzanna.testapp.ui

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smolianinovasiuzanna.testapp.data.Course
import com.smolianinovasiuzanna.testapp.data.CourseRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoursesListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CourseRepository(application)

    private val coursesLiveData = MutableLiveData<List<Course>>()
    private val errorLiveData = MutableLiveData<String>()

    val courses: LiveData<List<Course>>
        get() = coursesLiveData
    val error: LiveData<String>
        get() = errorLiveData

    private var coursesList = listOf<Course>()

    private val scope = viewModelScope
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine Exception", "${throwable.message}", throwable)
        errorLiveData.postValue(throwable.message)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCourses(){
        Log.d("ViewModel", "getCourses")
        scope.launch(Dispatchers.IO + errorHandler){
            coursesList = repository.getCourses()
            coursesLiveData.postValue(coursesList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCourse(title: String){
        scope.launch(Dispatchers.IO + errorHandler) {
            Log.d("ViewModel", "addCourse: title = $title")
           launch { repository.addCourse(title)} .join()
            getCourses()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteCourse(id: Long){
        scope.launch(Dispatchers.IO + errorHandler) {
            Log.d("ViewModel", "deleteCourse: id = $id")
            launch{
                repository.deleteCourse(id)
            } .join()
             getCourses()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteAllCourses(){
        scope.launch(Dispatchers.IO + errorHandler) {
            var delete: Int? = null
            launch {
                delete = repository.deleteAllCourses()
            }.join()
            Log.d("DeleteAllCourses","Deleted")
            getCourses()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCourse(id: Long, title: String){
        scope.launch(Dispatchers.IO + errorHandler) {
            Log.d("ViewModel", "updateCourse: id = $id, title = $title")
            launch { repository.updateCourse(id, title) } .join()
            getCourses()
        }
    }

}
package com.smolianinovasiuzanna.testapp.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CourseRepository(private val context: Context) {

    // получить список курсов
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCourses():List<Course>{
       return withContext(Dispatchers.IO){
            context.contentResolver.query(
                CONTENT_URI_COURSES,
                null,
                null,
                null,
                null
            ) ?. use{
                Log.d("repo getCourses", "uri = $CONTENT_URI_COURSES")
                getCoursesFromCursor(it)
            }?: throw IllegalArgumentException("Failed to find info. Make sure that the required application is installed")
        }
    }

    // Получить информацию о курсах по курсору
    private fun getCoursesFromCursor(cursor: Cursor): List<Course>{
        Log.d("Repo", "getCoursesFromCursor")
        if(cursor.moveToFirst().not()) return emptyList()

        val coursesList = mutableListOf<Course>()
        do{
            val id = cursor.getColumnIndex(COLUMN_COURSE_ID)
                .run(cursor::getLong)
            val title = cursor.getColumnIndex(COLUMN_COURSE_TITLE)
                .run(cursor::getString)
            val course = Course(id, title)
            Log.d("AddCourse", "course =$course" )
            if(coursesList.contains(course).not()) { coursesList.add(course)}
        } while(cursor.moveToNext())
        Log.d("list of courses: ", "$coursesList")
        return coursesList
    }

    //  добавить курс
    suspend fun addCourse(title: String) = withContext(Dispatchers.IO){
        Log.d("Repo", "addCourse:  title = $title")
        val id = Random.nextLong(1, 1000000000000000)
        val value = ContentValues().apply{
            put(COLUMN_COURSE_ID, id)
            put(COLUMN_COURSE_TITLE, title)
        }
        val result = context.contentResolver.insert(CONTENT_URI_COURSES, value)
        Log.d("Add Course", "$result")
        result != null
    }

    //  удалить курс по ID
    suspend fun deleteCourse(id: Long)  = withContext(Dispatchers.IO){
        Log.d("Repo", "deleteCourse: id = $id")
        val courseUri = ContentUris.withAppendedId(CONTENT_URI_COURSES, id)
        val deleted = context.contentResolver.delete(courseUri, null, null)
        Log.d("DeleteCourse", "$deleted" )

    }

    //  удалить все курсы сразу
    suspend fun deleteAllCourses() = withContext(Dispatchers.IO){
        val result = context.contentResolver.delete(CONTENT_URI_COURSES,null, null)
        Log.d("repo Delete All Courses", "$result")
    }

    //  обновить курс по ID
    suspend fun updateCourse(id: Long, title: String)= withContext(Dispatchers.IO){
        Log.d("Repo", "updateCourse: id = $id, title = $title")
        val courseUri = ContentUris.withAppendedId(CONTENT_URI_COURSES, id)
        val values = ContentValues().apply {
            put(COLUMN_COURSE_TITLE, title)
        }
        context.contentResolver.update(courseUri, values, null, null)
    }

    companion object {
        // пакет для подключения
        private const val AUTHORITY = "com.smolianinovasiuzanna.hw25.provider"

        // uri для подключения к Users
        private const val PATH_USERS = "users"
        private val CONTENT_URI_USERS = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(PATH_USERS)
            .build()

        // поля для User
        private const val COLUMN_USER_ID = "user id"
        private const val COLUMN_USER_NAME = "user name"
        private const val COLUMN_USER_AGE = "user age"

        // uri для подключения к Courses
        private const val PATH_COURSES = "courses"
        private val CONTENT_URI_COURSES = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(PATH_COURSES)
            .build()

        // поля для Course
        private const val COLUMN_COURSE_ID = "course id"
        private const val COLUMN_COURSE_TITLE = "course title"
    }
}
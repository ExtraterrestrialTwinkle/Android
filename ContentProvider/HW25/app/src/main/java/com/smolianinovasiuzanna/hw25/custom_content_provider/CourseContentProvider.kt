package com.smolianinovasiuzanna.hw25.custom_content_provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.smolianinovasiuzanna.hw25.BuildConfig
import com.squareup.moshi.Moshi

class CourseContentProvider: ContentProvider() {

    private lateinit var userPreferences: SharedPreferences
    private lateinit var coursePreferences: SharedPreferences

    private val userAdapter = Moshi.Builder().build().adapter(User::class.java)
    private val courseAdapter = Moshi.Builder().build().adapter(Course::class.java)

    //uriMatcher определяет какой именно uri пришел на вход
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply{
        Log.d("CourseContentProvider", "uriMatcher ")
        addURI(AUTHORITIES, PATH_USERS, TYPE_USERS)
        addURI(AUTHORITIES, PATH_COURSES, TYPE_COURSES)
        addURI(AUTHORITIES, "$PATH_USERS/#", TYPE_USER_ID) // # - любое число. Uri пользователя с id = #
        addURI(AUTHORITIES, "$PATH_COURSES/#", TYPE_COURSE_ID)
    }


    // Инициализация основных ресурсов в ContentProvider (SharedPreferences)
    override fun onCreate(): Boolean {
        Log.d("CourseContentProvider", "onCreate, Thread = ${Thread.currentThread().name}")
        userPreferences = context!!.getSharedPreferences("user_shared_preferences", Context.MODE_PRIVATE)
        coursePreferences = context!!.getSharedPreferences("course_shared_preferences", Context.MODE_PRIVATE)
        Log.d("CourseContentProvider","userPreferences = ${userPreferences.all}")
        Log.d("CourseContentProvider","coursePreferences = ${coursePreferences.all}")

        return true // инициализация прошла успешно
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("content provider query", "uri = $uri, Thread = ${Thread.currentThread().name}")
        return when(uriMatcher.match(uri)){
            TYPE_USERS -> getAllUsersCursor()
            TYPE_COURSES -> getAllCoursesCursor()
            else -> null
        }
    }

    // Возвращает MIME-type по uri
    override fun getType(uri: Uri): String? {
        return null // нам не нужен тип
    }

    // Возвращает uri для добавленной записи
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d("CourseContentProvider", "insert, uri = $uri, values = $values, Thread = ${Thread.currentThread().name}")
        values ?: return null
        return when(uriMatcher.match(uri)){
            TYPE_USERS -> saveUser(values)
            TYPE_COURSES -> saveCourse(values)
            else -> null
        }
    }

    // Возвращает количество удаленных записей
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d("CourseContentProvider", "delete, uri = $uri, Thread = ${Thread.currentThread().name}")
        return when(uriMatcher.match(uri)){
            TYPE_USER_ID -> deleteUser(uri)
            TYPE_COURSE_ID -> deleteCourse(uri)
            TYPE_COURSES -> deleteAllCourses()
            TYPE_USERS -> deleteAllUsers()
            else -> 0
        }
    }

    // Возвращает количество измененных записей
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d("CourseContentProvider", "update, uri = $uri, values = $values Thread = ${Thread.currentThread().name}")
        values ?: return 0
        return when(uriMatcher.match(uri)){
            TYPE_USER_ID -> updateUser(uri, values)
            TYPE_COURSE_ID -> updateCourse(uri, values)
            else -> 0
        }
    }

    private fun getAllUsersCursor(): Cursor{
        Log.d("CourseContentProvider", "getAllUsersCursor, Thread = ${Thread.currentThread().name}")
        val allUsers = userPreferences.all.mapNotNull {
            val userJsonString = it.value as String
            userAdapter.fromJson(userJsonString)
        }
        //MatrixCursor принимает на вход массив из колонок. Порядок колонок важен!
        val cursor = MatrixCursor(arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_AGE))

        //Добавляем строки с данными. Порядок добавления - как задавались колонки в массиве!!!
        allUsers.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.name)
                .add(it.age)
        }
        return cursor
    }

    private fun getAllCoursesCursor(): Cursor{
        Log.d("CourseContentProvider", "dgetAllCoursesCursor, Thread = ${Thread.currentThread().name}")
        val allCourses = coursePreferences.all.mapNotNull {
            val coursesJsonString = it.value as String
            courseAdapter.fromJson(coursesJsonString)
        }

        val cursor = MatrixCursor(arrayOf(COLUMN_COURSE_ID, COLUMN_COURSE_TITLE))

        allCourses.forEach {
            cursor.newRow()
                .add(it.id)
                .add(it.title)
        }
        return cursor
    }

    private fun saveUser(contentValues: ContentValues): Uri? {

        val id = contentValues.getAsLong(COLUMN_USER_ID) ?: return null
        val name = contentValues.getAsString(COLUMN_USER_NAME) ?: return null
        val age = contentValues.getAsInteger(COLUMN_USER_AGE) ?: return null
        Log.d("CourseContentProvider", "save user, values = $contentValues, Thread = ${Thread.currentThread().name}")
        val user = User(id = id, name = name, age = age)
        userPreferences.edit()
            .putString(id.toString(), userAdapter.toJson(user))
            .commit()

        return Uri.parse("content://$AUTHORITIES/$PATH_USERS/$id")
    }

    private fun saveCourse(contentValues: ContentValues): Uri? {
        Log.d("content provider ", "save course, values = $contentValues, Thread = ${Thread.currentThread().name}")
        val id = contentValues.getAsLong(COLUMN_COURSE_ID) ?: return null
        val title = contentValues.getAsString(COLUMN_COURSE_TITLE) ?: return null

        val course = Course(id = id, title = title)
        coursePreferences.edit()
            .putString(id.toString(), courseAdapter.toJson(course))
            .commit()

        return Uri.parse("content://$AUTHORITIES/$PATH_COURSES/$id")
    }

    private fun deleteUser(uri: Uri): Int{
        val userId = uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        Log.d("CourseContentProvider", "deleteUser, id = $userId, Thread = ${Thread.currentThread().name}")
        return if(userPreferences.contains(userId)) {
            userPreferences.edit()
                .remove(userId)
                .commit()
            1
        } else 0
    }

    private fun deleteAllCourses(): Int{
        val entries = coursePreferences.all.size
        Log.d("CourseContentProvider", "deleteAllCourses = $entries")
        coursePreferences.edit()
            .clear()
            .commit()
        return entries
    }

    private fun deleteAllUsers(): Int{
        Log.d("CourseContentProvider", "deleteAllUses")
        val entries = userPreferences.all.size
        userPreferences.edit()
            .clear()
            .commit()
        return entries
    }

    private fun deleteCourse(uri: Uri): Int{
        val courseId = uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        Log.d("CourseContentProvider", "deleteCourse, id = $courseId, Thread = ${Thread.currentThread().name}")

        return if(coursePreferences.contains(courseId)) {
            coursePreferences.edit()
                .remove(courseId)
                .commit()
            1
        } else 0
    }

    private fun updateUser(uri: Uri, contentValues: ContentValues): Int{
        val userId= uri.lastPathSegment?.toLongOrNull()?.toString() ?: return 0
        Log.d("CourseContentProvider", "updateUser, id = $userId, Thread = ${Thread.currentThread().name}")
        return if(userPreferences.contains(userId)){
            val id = userId.toLong()
            val name = contentValues.getAsString(COLUMN_USER_NAME)
            val age = contentValues.getAsInteger(COLUMN_USER_AGE)
            Log.d("CourseContentProvider", "save user, values = $contentValues, Thread = ${Thread.currentThread().name}")
            val user = User(id = id, name = name, age = age)
            userPreferences.edit()
                .putString(userId, userAdapter.toJson(user))
                .commit()
            1
        } else 0
    }

    private fun updateCourse(uri: Uri, contentValues: ContentValues): Int{
        val courseId= uri.lastPathSegment?.toLongOrNull().toString() ?: return 0
        Log.d("CourseContentProvider", "updateCourse, id = $courseId, values = $contentValues, Thread = ${Thread.currentThread().name}")
        return if(coursePreferences.contains(courseId)){
            val title = contentValues.getAsString(COLUMN_COURSE_TITLE)
            val id = courseId.toLong()
            val course = Course(id = id, title = title)
            coursePreferences.edit()
                .putString(id.toString(), courseAdapter.toJson(course))
                .commit()
            1
        } else 0
    }

    companion object {
        private const val AUTHORITIES = "com.smolianinovasiuzanna.hw25.provider"

        private const val PATH_USERS = "users"
        private const val PATH_COURSES = "courses"

        private const val TYPE_USERS = 102938
        private const val TYPE_COURSES = 981756

        private const val TYPE_USER_ID = 12345
        private const val TYPE_COURSE_ID = 67890

        private const val COLUMN_USER_ID = "user id"
        private const val COLUMN_USER_NAME = "user name"
        private const val COLUMN_USER_AGE = "user age"

        private const val COLUMN_COURSE_ID = "course id"
        private const val COLUMN_COURSE_TITLE = "course title"
    }
}
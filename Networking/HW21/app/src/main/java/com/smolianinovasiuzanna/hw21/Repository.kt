package com.smolianinovasiuzanna.hw21

import android.util.Log
import com.smolianinovasiuzanna.hw21.Network.client
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class Repository(val errorCallback: (ImdbResponse.Error) -> Unit) {

    fun searchMovie(
        title: String,
        year: String,
        type: String,
        callback: (List<ImdbResponse.Movie>) -> Unit
    ): Call {
        return Network.searchMovieCall(title, year, type).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                   errorCallback(ImdbResponse.Error(e))
                    Log.e("Error", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("Repo", "onResponse")
                    if (response.isSuccessful) {
                        Log.d("Repo", "parseMovieResponse response isSuccessful")
                            val responseString = response.body?.string().orEmpty()
                            Log.d("Server", "responseString = $responseString")
                            val movies = parseMovieResponse(responseString)
                            callback(movies)
                    } else {
                        Log.d("Repo", "parseMovieResponse response is not Successful")
                        errorCallback(ImdbResponse.Error(Throwable("Response not Successful")))
                    }
                }
            })
        }
    }

    private fun parseMovieResponse(responseBodyString: String): List<ImdbResponse.Movie> {
        Log.d("Repo", "parseMovieResponse")
        return try {
            Log.d("Repo", "parseMovieResponse try")
            val jsonObject = JSONObject(responseBodyString)
                val movieArray = jsonObject.getJSONArray("Search")
                (0 until movieArray.length()).map {index -> movieArray.getJSONObject(index)}
                    .map{movieJsonObject ->
                        val title = movieJsonObject.getString("Title")
                        val year = movieJsonObject.getString("Year")
                        val type = movieJsonObject.getString("Type")
                        val id = movieJsonObject.getString("imdbID")
                        val posterLink = movieJsonObject.getString("Poster")
                        ImdbResponse.Movie(title = title, year = year, type = type, id = id, posterLink = posterLink)
                    }

        } catch (e: Throwable) {
            Log.e("Repo exception", e.toString())
            errorCallback(ImdbResponse.Error(e))
            return emptyList()
        }
    }
}
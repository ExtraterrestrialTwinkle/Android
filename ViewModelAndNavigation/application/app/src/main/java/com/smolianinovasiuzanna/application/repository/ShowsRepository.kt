package com.smolianinovasiuzanna.application.repository

import com.smolianinovasiuzanna.application.R
import com.smolianinovasiuzanna.application.Shows
import com.smolianinovasiuzanna.application.ShowsResourcesData
import com.smolianinovasiuzanna.application.Type
import kotlin.random.Random

class ShowsRepository {

    fun initShows(): List<Shows> {
        return listOf<Shows>(
            Shows.Film(
                id = 1,
                title = ShowsResourcesData.resources?.getString(R.string.the_shawshenk_redemption)
                    ?: "No res",
                year = "1994",
                posterLink = ShowsResourcesData.resources?.getString(R.string.shawshenkLink)
                    ?: "No res",
                director = ShowsResourcesData.resources?.getString(R.string.frank_darabont)
                    ?: "No res"
            ),
            Shows.Film(
                id = 2,
                title = ShowsResourcesData.resources?.getString(R.string.the_godfather) ?: "No res",
                year = "1972",
                posterLink = ShowsResourcesData.resources?.getString(R.string.godfatherLink)
                    ?: "No res",
                director = ShowsResourcesData.resources?.getString(R.string.francis_ford_coppola)
                    ?: "No res"
            ),
            Shows.Film(
                id = 3,
                title = ShowsResourcesData.resources?.getString(R.string.the_dark_knight)
                    ?: "No res",
                year = "2008",
                posterLink = ShowsResourcesData.resources?.getString(R.string.darkknightLink)
                    ?: "No res",
                director = ShowsResourcesData.resources?.getString(R.string.christopher_nolan)
                    ?: "No res"
            ),
            Shows.Film(
                id = 4,
                title = ShowsResourcesData.resources?.getString(R.string.schindlers_list)
                    ?: "No res",
                year = "1993",
                posterLink = ShowsResourcesData.resources?.getString(R.string.schindlerLink)
                    ?: "No res",
                director = ShowsResourcesData.resources?.getString(R.string.steven_spielberg)
                    ?: "No res"
            ),
            Shows.Film(
                id = 5,
                title = ShowsResourcesData.resources?.getString(R.string.pulp_fiction) ?: "No res",
                year = "1994",
                posterLink = ShowsResourcesData.resources?.getString(R.string.pulpLink) ?: "No res",
                director = ShowsResourcesData.resources?.getString(R.string.quentin_tarantino)
                    ?: "No res"
            ),
            Shows.Series(
                id = 6,
                title = ShowsResourcesData.resources?.getString(R.string.the_walking_dead)
                    ?: "No res",
                posterLink = ShowsResourcesData.resources?.getString(R.string.walkingLink)
                    ?: "No res",
                creators = ShowsResourcesData.resources?.getString(R.string.the_walking_dead_creators)
                    ?: "No res",
                seasons = "11"
            ),
            Shows.Series(
                id = 7,
                title = ShowsResourcesData.resources?.getString(R.string.friends) ?: "No res",
                posterLink = ShowsResourcesData.resources?.getString(R.string.friendsLink)
                    ?: "No res",
                creators = ShowsResourcesData.resources?.getString(R.string.friends_creators)
                    ?: "No res",
                seasons = "10"
            ),
            Shows.Series(
                id = 8,
                title = ShowsResourcesData.resources?.getString(R.string.greys_anatomy) ?: "No res",
                posterLink = ShowsResourcesData.resources?.getString(R.string.greyLink) ?: "No res",
                creators = ShowsResourcesData.resources?.getString(R.string.greys_anatomy_creators)
                    ?: "No res",
                seasons = "17"
            ),
            Shows.Series(
                id = 9,
                title = ShowsResourcesData.resources?.getString(R.string.house_m_d) ?: "No res",
                posterLink = ShowsResourcesData.resources?.getString(R.string.houseLink)
                    ?: "No res",
                creators = ShowsResourcesData.resources?.getString(R.string.house_m_d_creators)
                    ?: "No res",
                seasons = "8"
            ),
            Shows.Series(
                id = 10,
                title = ShowsResourcesData.resources?.getString(R.string.bones) ?: "No res",
                posterLink = ShowsResourcesData.resources?.getString(R.string.bonesLink)
                    ?: "No res",
                creators = ShowsResourcesData.resources?.getString(R.string.bones_creators)
                    ?: "No res",
                seasons = "12"
            )
        )
    }

// type и args передаются при создании шоу в диалоге
    fun createShows(type: Type, args: List<String>, shows: List<Shows>): List<Shows>{
        lateinit var newShows: List<Shows>
        if(type == Type.TYPE_FILM) {
            newShows = listOf(createFilm(
                id = Random.nextLong(),
                title = args[0],
                director = args[1],
                year = args[2],
                posterLink = args[3]
            )) + shows
        }
        else if (type == Type.TYPE_SERIES){
            newShows = listOf(createSeries(
                id = Random.nextLong(),
                title = args[0],
                creators = args[1],
                seasons = args[2],
                posterLink = args[3]
            )) + shows
        }
        return newShows
    }

    private fun createFilm (
        id: Long,
        title: String,
        year: String,
        posterLink: String,
        director: String
    ): Shows.Film {
        return Shows.Film(id, title, year, posterLink, director)
    }
    private fun createSeries (
        id: Long,
        title: String,
        seasons: String,
        posterLink: String,
        creators: String
    ): Shows.Series {
        return Shows.Series(id, title, seasons, posterLink, creators)
    }

    fun deleteShow(shows: List<Shows>, position: Int): List<Shows> {
        val newShows = shows.filterIndexed { index, show -> index != position }
        return newShows
    }

}
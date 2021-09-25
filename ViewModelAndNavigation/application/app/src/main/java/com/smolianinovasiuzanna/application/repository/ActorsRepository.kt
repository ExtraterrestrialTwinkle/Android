package com.smolianinovasiuzanna.application.repository

import android.util.Log
import com.smolianinovasiuzanna.application.Actors
import com.smolianinovasiuzanna.application.ActorsResourcesData
import com.smolianinovasiuzanna.application.R

class ActorsRepository {
    fun initActors(index: Long): ArrayList<Actors> {
        Log.d("ListFragment", "fun initShows")
        val shawshenk = ActorsResourcesData.resources?.getStringArray(R.array.shawshenk_actors)?: arrayOf("No Data")
        val shawshenkPhotos = ActorsResourcesData.resources?.getStringArray(R.array.shawshenk_photos)?: arrayOf("No Data")
        val godfather = ActorsResourcesData.resources?.getStringArray(R.array.godfather_actors)?: arrayOf("No Data")
        val godfatherPhotos = ActorsResourcesData.resources?.getStringArray(R.array.godfather_photos)?: arrayOf("No Data")
        val darkKnight = ActorsResourcesData.resources?.getStringArray(R.array.dark_knight_actors)?: arrayOf("No Data")
        val darkKnightPhotos = ActorsResourcesData.resources?.getStringArray(R.array.dark_knight_photos)?: arrayOf("No Data")
        val schindler = ActorsResourcesData.resources?.getStringArray(R.array.schindler_actors)?: arrayOf("No Data")
        val schindlerPhotos = ActorsResourcesData.resources?.getStringArray(R.array.schindler_actors)?: arrayOf("No Data")
        val pulpFiction = ActorsResourcesData.resources?.getStringArray(R.array.pulp_fiction_actors)?: arrayOf("No Data")
        val pulpFictionPhotos = ActorsResourcesData.resources?.getStringArray(R.array.pulp_fiction_photos)?: arrayOf("No Data")
        val walkingDead = ActorsResourcesData.resources?.getStringArray(R.array.walking_dead_actors)?: arrayOf("No Data")
        val walkingDeadPhotos = ActorsResourcesData.resources?.getStringArray(R.array.walking_dead_photos)?: arrayOf("No Data")
        val friends = ActorsResourcesData.resources?.getStringArray(R.array.friends_actors)?: arrayOf("No Data")
        val friendsPhotos = ActorsResourcesData.resources?.getStringArray(R.array.friends_photos)?: arrayOf("No Data")
        val greys = ActorsResourcesData.resources?.getStringArray(R.array.greys_anatomy_actors)?: arrayOf("No Data")
        val greysPhotos = ActorsResourcesData.resources?.getStringArray(R.array.greys_anatomy_photos)?: arrayOf("No Data")
        val house = ActorsResourcesData.resources?.getStringArray(R.array.house_actors)?: arrayOf("No Data")
        val housePhotos = ActorsResourcesData.resources?.getStringArray(R.array.house_photos)?: arrayOf("No Data")
        val bones = ActorsResourcesData.resources?.getStringArray(R.array.bones_actors)?: arrayOf("No Data")
        val bonesPhotos = ActorsResourcesData.resources?.getStringArray(R.array.bones_photos)?: arrayOf("No Data")
        val actors = arrayListOf<Actors>()
        when (index.toInt()) {
            1 -> {
                for (i in shawshenk.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = shawshenk[i],
                        photoLink = shawshenkPhotos[i]
                    )
                    actors.add(actor)
                }
            }

            2 -> {
                for (i in godfather.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = godfather[i],
                        photoLink = godfatherPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            3 -> {
                for (i in darkKnight.indices) {
                    val actor = Actors(
                        id = i + 1,
                        name = darkKnight[i],
                        photoLink = darkKnightPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            4 -> {
                for (i in schindler.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = schindler[i],
                        photoLink = schindlerPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            5 -> {
                for (i in pulpFiction.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = pulpFiction[i],
                        photoLink = pulpFictionPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            6 -> {
                for (i in walkingDead.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = walkingDead[i],
                        photoLink = walkingDeadPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            7 -> {
                for (i in friends.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = friends[i],
                        photoLink = friendsPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            8 -> {
                for (i in greys.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = greys[i],
                        photoLink = greysPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            9 -> {
                for (i in house.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = house[i],
                        photoLink = housePhotos[i]
                    )
                    actors.add(actor)
                }
            }
            10 -> {
                for (i in bones.indices) {
                    val actor = Actors(
                        id = i+1,
                        name = bones[i],
                        photoLink = bonesPhotos[i]
                    )
                    actors.add(actor)
                }
            }
            else -> listOf<Actors>()
        }
        return actors
    }

}
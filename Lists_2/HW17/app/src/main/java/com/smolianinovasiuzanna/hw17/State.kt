package com.smolianinovasiuzanna.hw17

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

@kotlinx.parcelize.Parcelize
data class State(
    var showList: List<Shows>,
    var isDialogOpen: Boolean
    ): Parcelable

@kotlinx.parcelize.Parcelize
data class FragmentMainState(
    var state: Boolean,
    var message: String
):Parcelable

@kotlinx.parcelize.Parcelize
data class PosterState(var posterList: List<Posters>): Parcelable

@kotlinx.parcelize.Parcelize
data class ActorsState(var actorsList: List<Actors>): Parcelable

@kotlinx.parcelize.Parcelize
data class PictureState(var picturesList: List<Pictures>): Parcelable




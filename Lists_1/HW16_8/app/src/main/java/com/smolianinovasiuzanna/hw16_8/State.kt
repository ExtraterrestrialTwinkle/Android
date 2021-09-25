package com.smolianinovasiuzanna.hw16_8

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

@kotlinx.parcelize.Parcelize
data class State(
    var showList: List<Shows>,
    var isDialogOpen: Boolean
    ): Parcelable

@kotlinx.parcelize.Parcelize
data class ActivityState(
    var state: Boolean,
    var message: String
):Parcelable





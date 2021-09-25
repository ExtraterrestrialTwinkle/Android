package com.smolianinovasiuzanna.hw18

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class LocationInfoState(var locationInfoList: List<LocationInfo>): Parcelable

package com.smolianinovasiuzanna.hw18

import android.os.Parcelable
import org.threeten.bp.Instant

@kotlinx.parcelize.Parcelize
data class LocationInfo(
    var id: Long,
    var info: String,
    var imageLink: String,
    var timestamp: Instant
): Parcelable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

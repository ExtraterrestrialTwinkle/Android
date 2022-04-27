package com.smolianinovasiuzanna.hw33.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.smolianinovasiuzanna.hw33.R

class LargeIcon (private val context: Context){

    fun loadLargeIcon(iconLink: String?): Bitmap?{
        var icon: Bitmap? = null

        Glide.with(context)
            .asBitmap()
            .load(iconLink)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_error)
            .into(object: CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    icon = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    icon = placeholder?.toBitmap()
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    icon = errorDrawable?.toBitmap()
                }
            })
        return icon
    }
}

package com.funnelmik.spreadly

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView

class Utilities {
    companion object {
        fun setImageWithBytes(view: ImageView, data: ByteArray?) {
            if (data != null) {
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                view.setImageBitmap(bitmap)
            }
        }
    }

}
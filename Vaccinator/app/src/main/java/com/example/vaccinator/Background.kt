package com.example.vaccinator

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import kotlin.math.truncate

class Background constructor(
    screenX: Int,
    screenY: Int,
    res: Resources
) : GameObject() {

    var bitmap: Bitmap

    init {
        this.bitmap = BitmapFactory.decodeResource(res, R.drawable.background)
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, screenX, screenY, false)
    }

    fun scroll(offset: Float) {
        this.translatePosition(PointF(offset, 0.0f))

        if (this.getPosition().x + this.bitmap.width < 0) {
            this.setPosition(PointF(truncate(this.bitmap.width.toFloat()), this.getPosition().y))
        }
    }

    fun render(canvas: Canvas) {
        super.render(canvas, this.bitmap)
    }
}
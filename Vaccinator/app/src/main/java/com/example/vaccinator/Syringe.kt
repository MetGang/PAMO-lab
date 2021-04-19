package com.example.vaccinator

import android.content.res.Resources
import android.graphics.*

class Syringe constructor(
    res: Resources
) : GameObject() {

    private var bitmap: Bitmap

    init {
        this.bitmap = BitmapFactory.decodeResource(res, R.drawable.syringe)
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.width, this.bitmap.height, false)
        this.setRadius(this.bitmap.width * 0.5f)
        this.setOrigin(PointF(this.getRadius(), this.getRadius()))
    }

    fun render(canvas: Canvas) {
        super.render(canvas, this.bitmap)
    }
}
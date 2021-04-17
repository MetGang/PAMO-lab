package com.example.vaccinator

import android.content.res.Resources
import android.graphics.*

class Gun constructor(
    position: PointF,
    res: Resources
) : GameObject() {

    enum class DIRECTION(val value: Int) {
        None(0),
        Upwards(-1),
        Downwards(1)
    }

    private var direction: DIRECTION
    private var bitmap: Bitmap

    init {
        this.direction = DIRECTION.None
        this.bitmap = BitmapFactory.decodeResource(res, R.drawable.gun)
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.width, this.bitmap.height, false)
        this.setPosition(position)
        this.setRadius(this.bitmap.width * 0.5f)
        this.setOrigin(PointF(this.getRadius(), this.getRadius()))
    }

    fun move(offset: Float) {
        this.translatePosition(PointF(0.0f, this.direction.value * offset))
    }

    fun setDirection(dir: DIRECTION) {
        this.direction = dir
    }

    fun render(canvas: Canvas) {
        super.render(canvas, this.bitmap)
    }
}
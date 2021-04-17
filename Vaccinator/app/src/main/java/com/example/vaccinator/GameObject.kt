package com.example.vaccinator

import android.content.res.Resources
import android.graphics.*
import kotlin.math.truncate

abstract class GameObject(
    private var position: PointF = PointF(0.0f, 0.0f),
    private var origin: PointF = PointF(0.0f, 0.0f),
    private var radius: Float = 0.0f
) {
    private var alive: Boolean

    init {
        this.alive = true
    }

    fun collides(other: GameObject): Boolean {
        val dx = this.position.x - other.position.x;
        val dy = this.position.y - other.position.y;
        val rr = this.radius + other.radius

        return dx * dx + dy * dy < rr * rr
    }

    fun getPosition(): PointF {
        return this.position
    }

    fun getOrigin(): PointF {
        return this.origin
    }

    fun setPosition(position: PointF) {
        this.position = position
    }

    fun setOrigin(origin: PointF) {
        this.origin = origin
    }

    fun translatePosition(offset: PointF) {
        this.position.offset(offset.x, offset.y)
    }

    fun translateOrigin(offset: PointF) {
        this.origin.offset(offset.x, offset.y)
    }

    fun moveHorizontally(offset: Float) {
        this.position.x += offset
    }

    fun moveVertically(offset: Float) {
        this.position.y += offset
    }

    fun adjustPosition(rect: RectF) {
        if (this.position.x - this.origin.x < rect.left) {
            this.position.x = rect.left + this.origin.x
        }
        else if (this.position.x + this.origin.x > rect.right) {
            this.position.x = rect.right - this.origin.x
        }

        if (this.position.y - this.origin.y < rect.top) {
            this.position.y = rect.top + this.origin.y
        }
        else if (this.position.y + this.origin.y > rect.bottom) {
            this.position.y = rect.bottom - this.origin.y
        }
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun getRadius(): Float {
        return this.radius
    }

    fun kill() {
        this.alive = false
    }

    fun isAlive(): Boolean {
        return this.alive
    }

    fun render(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(
            bitmap,
            truncate(this.position.x - this.origin.x),
            truncate(this.position.y - this.origin.y),
            Paint()
        )

        val paint = Paint()
        paint.color = Color.argb(0.3f, 1.0f, 0.0f, 1.0f)
        canvas.drawCircle(
            truncate(this.position.x),
            truncate(this.position.y),
            this.radius,
            paint
        )
    }
}
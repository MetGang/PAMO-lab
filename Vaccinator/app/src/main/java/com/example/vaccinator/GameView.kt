package com.example.vaccinator

import android.content.Context
import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.random.Random

class GameView constructor(
    context: Context,
    private var screenX: Int,
    private var screenY: Int,
    private var onGameOver: () -> Unit
) : SurfaceView(context), Runnable {

    private var thread: Thread?
    private var isRunning: Boolean
    private var gun: Gun
    private var shotCooldown: Long
    private var spawnCooldown: Long
    private var syringes: MutableList<Syringe>
    private var viruses: MutableList<Virus>
    private var firstBackground: Background
    private var secondBackground: Background

    init {
        thread = null
        isRunning = false
        gun = Gun(PointF(0.0f, screenY.toFloat() * 0.5f), resources)
        shotCooldown = System.currentTimeMillis()
        spawnCooldown = System.currentTimeMillis()
        syringes = mutableListOf()
        viruses = mutableListOf()
        firstBackground = Background(screenX, screenY, resources)
        secondBackground = Background(screenX, screenY, resources)

        secondBackground.setPosition(PointF(firstBackground.bitmap.width.toFloat(), 0.0f))
    }

    override fun run() {
        while (isRunning) {
            update()
            render()
        }
    }

    private fun update() {
        if (System.currentTimeMillis() - spawnCooldown > 2000) {
            val y = ((screenY * 0.2).toInt()..(screenY * 0.8).toInt()).random().toFloat()
            val virus = Virus(resources)
            virus.setPosition(PointF(screenX.toFloat() + virus.getRadius() * 4.0f, y))
            viruses.add(virus)
            spawnCooldown = System.currentTimeMillis()
        }

        firstBackground.scroll(-10.0f)
        secondBackground.scroll(-10.0f)

        gun.move(8.0f)
        gun.adjustPosition(RectF(0.0f, 0.0f, screenX.toFloat(), screenY.toFloat()))

        syringes.forEach { syringe ->
            syringe.moveHorizontally(9.0f)
        }

        viruses.forEach { virus ->
            virus.moveHorizontally(-5.0f)

            if (virus.getPosition().x < 0) {
                gameOver()
            }

            syringes.forEach { syringe ->
                run {
                    if (virus.collides(syringe)) {
                        virus.kill()
                        syringe.kill()
                    }
                }
            }
        }

        syringes.removeIf { !it.isAlive() }
        viruses.removeIf { !it.isAlive() }
    }

    private fun render() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()

            firstBackground.render(canvas)
            secondBackground.render(canvas)

            viruses.forEach { it.render(canvas) }

            syringes.forEach { it.render(canvas) }

            gun.render(canvas)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun gameOver() {
        onGameOver()
    }

    fun resume() {
        isRunning = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause() {
        isRunning = false
        thread!!.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < screenX / 2) {
                    if (event.y < gun.getPosition().y) {
                        gun.setDirection(Gun.DIRECTION.Upwards)
                    }
                    else {
                        gun.setDirection(Gun.DIRECTION.Downwards)
                    }
                }
                else {
                    if (System.currentTimeMillis() - shotCooldown > 500) {
                        val syringe = Syringe(resources)
                        syringe.setPosition(PointF(gun.getPosition().x + gun.getRadius() + 8.0f, gun.getPosition().y))
                        syringes.add(syringe)
                        shotCooldown = System.currentTimeMillis()
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }
}
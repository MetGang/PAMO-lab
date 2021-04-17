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
    private var screenY: Int
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

        secondBackground.setPosition(PointF(screenX.toFloat(), 0.0f))
    }

    override fun run() {
        while (isRunning) {
            update()
            render()
            sleep()
        }
    }

    private fun update() {
        if (System.currentTimeMillis() - spawnCooldown > 2000) {
            val y = ((screenY * 0.2).toInt()..(screenY * 0.8).toInt()).random().toFloat()
            val virus = Virus(resources)
            Log.v("xD", screenX.toString() + " " + virus.getRadius().toString())
            virus.setPosition(PointF(screenX.toFloat() + virus.getRadius() * 4.0f, y))
            viruses.add(virus)
            spawnCooldown = System.currentTimeMillis()
        }

        firstBackground.scroll(-10.0f)
        secondBackground.scroll(-10.0f)

        gun.move(8.0f)
        gun.adjustPosition(RectF(0.0f, 0.0f, screenX.toFloat(), screenY.toFloat()))

        syringes.forEach {
            it.moveHorizontally(9.0f)
        }

        viruses.forEach {
            it.moveHorizontally(-5.0f)
        }

        syringes.forEach {
            val syringe = it
            viruses.forEach {
                val virus = it

                if (syringe.collides(virus)) {
                    syringe.kill()
                    virus.kill()
                }

                if (virus.collides(gun)) {
                    virus.kill()
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

    private fun sleep() {
//        Thread.sleep(17)
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
                        syringes.add(Syringe(PointF(gun.getPosition().x + 100.0f, gun.getPosition().y), resources))
                        shotCooldown = System.currentTimeMillis()
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }
}
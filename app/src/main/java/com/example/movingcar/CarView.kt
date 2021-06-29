package com.example.movingcar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

private const val MARGIN = 100

class CarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var isRunning = false
    private var isVerticaMove = false   //Вертикальное направление движения
    private var prevVerticaMove = false
    private var cx = 0
    private var cy = 50
    private val d = 5
    //Дополнительные Bitmap и Canvas для поворота рисунка без поворота всей View
    private var bmp: Bitmap? = null
    private var cnv: Canvas? = null
    private var textPaint: Paint

    init {
        textPaint = Paint()
        textPaint.textSize = 50f
    }

    override fun onDraw(canvas: Canvas?) {
        if (bmp == null || cnv == null) {
            makeBitmapAndCanvas(0)
        }
        if (canvas != null) {
            canvas.drawText("Click anywhere to start/stop", 0f, MARGIN/2.toFloat(), textPaint)
            if (isRunning) {
                prevVerticaMove = isVerticaMove
                //В конце движемся прямо, иначе - рандом
                if (cx == width - MARGIN)
                    isVerticaMove = true
                else if (cy == height - MARGIN)
                    isVerticaMove = false
                else if (cx%50 == 0 && cy%50 == 0)
                    isVerticaMove = Math.random() > 0.5

                //Если нужно повернуть рисунок
                if (prevVerticaMove != isVerticaMove) {
                    makeBitmapAndCanvas(if (isVerticaMove) 90 else 0)
                }

                if (isVerticaMove)
                    cy = minOf(cy + d,  height - MARGIN)
                else
                    cx = minOf(cx + d, width - MARGIN)
            }
            if (cx == width - MARGIN && cy == height - MARGIN)
                isRunning = false

            canvas.drawBitmap(bmp!!, cx.toFloat(), cy.toFloat(), null)

            if (isRunning)
                invalidate()
        }
    }

    private fun makeBitmapAndCanvas(angle: Int) {
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_car_top_view)
        icon!!.setBounds(0, 0, MARGIN, MARGIN)
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        cnv = Canvas(bmp!!)
        cnv!!.rotate(angle.toFloat(), (MARGIN/2).toFloat(), (MARGIN/2).toFloat())
        icon.draw(cnv!!)
    }

    private val myListener =  object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

    private val detector: GestureDetector = GestureDetector(context, myListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event).let { result ->
            if (!result) {
                if (event.action == MotionEvent.ACTION_UP) {
                    isRunning = !isRunning
                    invalidate()
                    true
                } else false
            } else true
        }
    }
}
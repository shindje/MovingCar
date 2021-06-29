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
    private var m: Model
    private val d = 5
    //Дополнительные Bitmap и Canvas для поворота рисунка без поворота всей View
    private var bmp: Bitmap? = null
    private var cnv: Canvas? = null
    private var textPaint: Paint

    init {
        textPaint = Paint()
        textPaint.textSize = 50f
        textPaint.color = R.attr.colorPrimary
        m = Model()
    }

    fun setModel(mm: Model) {
        m = mm
    }

    override fun onDraw(canvas: Canvas?) {
        if (bmp == null || cnv == null) {
            makeBitmapAndCanvas()
        }
        if (canvas != null) {
            if (m.isRunning) {
                m.prevVerticaMove = m.isVerticaMove
                //В конце движемся прямо, иначе - рандом
                if (m.cx == width - MARGIN)
                    m.isVerticaMove = true
                else if (m.cy == height - MARGIN)
                    m.isVerticaMove = false
                else if (m.cx%50 == 0 && m.cy%50 == 0)
                    m.isVerticaMove = Math.random() > 0.5

                //Если нужно повернуть рисунок
                if (m.prevVerticaMove != m.isVerticaMove) {
                    makeBitmapAndCanvas()
                }

                if (m.isVerticaMove)
                    m.cy = minOf(m.cy + d,  height - MARGIN)
                else
                    m.cx = minOf(m.cx + d, width - MARGIN)
            }
            if (m.cx == width - MARGIN && m.cy == height - MARGIN) {
                m.isRunning = false
                m.isFinished = true
                canvas.drawText("Finished! Click to restart", 0f, MARGIN/2.toFloat(), textPaint)
            } else
                canvas.drawText("Click anywhere to start/stop", 0f, MARGIN/2.toFloat(), textPaint)

            canvas.drawBitmap(bmp!!, m.cx.toFloat(), m.cy.toFloat(), null)

            if (m.isRunning)
                invalidate()
        }
    }

    private fun makeBitmapAndCanvas() {
        val degree = if (m.isVerticaMove) 90 else 0
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_car_top_view)
        icon!!.setBounds(0, 0, MARGIN, MARGIN)
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        cnv = Canvas(bmp!!)
        cnv!!.rotate(degree.toFloat(), (MARGIN/2).toFloat(), (MARGIN/2).toFloat())
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
                    m.isRunning = !m.isRunning
                    if (m.isFinished) {
                        m = Model()
                        m.isRunning = true
                    }
                    invalidate()
                    true
                } else false
            } else true
        }
    }

    fun getModel() = m
}
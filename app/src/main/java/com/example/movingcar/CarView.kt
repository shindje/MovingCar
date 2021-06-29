package com.example.movingcar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

private const val MARGIN = 100

class CarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var isRunning = true
    private var isVerticaMove = false   //Вертикальное направление движения
    private var prevVerticaMove = false
    private var cx = MARGIN
    private var cy = MARGIN
    private val d = 5
    //Дополнительные Bitmap и Canvas для поворота рисунка без поворота всей View
    private var bmp: Bitmap? = null
    private var cnv: Canvas? = null

    override fun onDraw(canvas: Canvas?) {
        if (bmp == null || cnv == null)
            makeBitmapAndCanvas(0)
        if (canvas != null) {
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
}
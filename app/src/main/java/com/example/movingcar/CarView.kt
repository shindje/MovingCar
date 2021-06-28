package com.example.movingcar

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

private const val MARGIN = 100

class CarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var isRunning = true
    private var cx = MARGIN
    private var cy = MARGIN
    private var d = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_car_top_view)
        if (canvas != null && icon != null) {
            if (isRunning) {
                if (d == 0)
                    d = maxOf(width, height) / 500

                cx = minOf(cx + d, width - MARGIN)
                cy = minOf(cy + d,  height - MARGIN)
            }
            if (cx == width - MARGIN && cy == height - MARGIN)
                isRunning = false
            icon.setBounds(cx, cy, cx + MARGIN, cy + MARGIN)
            icon.draw(canvas)

            if (isRunning)
                invalidate()
        }
    }
}
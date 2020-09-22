package com.example.todotask.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet


class LinedEditText(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    private val mRect: Rect = Rect()
    private val mPaint: Paint = Paint()
    override fun onDraw(canvas: Canvas) {
        val count = lineCount
        val r: Rect = mRect
        val paint: Paint = mPaint
        for (i in 0 until count) {
            val baseline = getLineBounds(i, r)
            canvas.drawLine(r.left.toFloat(), (baseline + 1).toFloat(), r.right.toFloat(), (baseline + 1).toFloat(), paint)
        }
        super.onDraw(canvas)
    }

    // we need this constructor for LayoutInflater
    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = -0x7fffff01
    }
}
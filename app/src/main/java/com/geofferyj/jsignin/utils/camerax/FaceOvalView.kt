package com.geofferyj.jsignin.utils.camerax

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FaceOvalView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    private val pen = Paint().also { pen ->
        pen.color = Color.RED
        pen.strokeWidth = 8F
        pen.style = Paint.Style.STROKE
    }

    private val box = RectF(219.0F, 744.0F, 739.0F, 1356.0F)

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        canvas.drawRect(box, pen)
    }
}
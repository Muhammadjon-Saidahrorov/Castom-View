package uz.gita.castomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MySpeedometer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val maxSpeed = 180
    private var currentSpeed = 0

    private val paint = Paint().apply {
        strokeWidth = 16f
        style = Paint.Style.STROKE
        color = Color.RED
    }

    private val paintFill = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val x = width / 2f
        val y = height * 1f
        val radius = min(x, y)

        val rect = RectF(0f + 16, 0f + 16f, 2 * radius - 16f, 2 * radius)

        canvas.drawArc(rect, 180f, 180f, false, paint)

        canvas.drawCircle(radius, radius, 20f, paintFill)

        val angle = Math.PI - Math.PI * currentSpeed / maxSpeed
        val endX = radius + cos(angle) * radius - 20f
        val endY = radius - sin(angle) * radius + 20f

        canvas.drawLine(radius, radius, endX.toFloat(),endY.toFloat(), paintFill)
    }

    fun setCurrentSpeed(speed: Int){
        if (speed in 1 until maxSpeed){
            currentSpeed = speed
            invalidate()
        }
    }

}
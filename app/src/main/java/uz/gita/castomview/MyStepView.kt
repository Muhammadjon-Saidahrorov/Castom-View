package uz.gita.castomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyStepView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val countStep = 5
    private var currentStep = 0
    private val radius = 60f
    private val lineLength = 60f

    private val paintStroke = Paint().apply {
        strokeWidth = 8f
        style = Paint.Style.STROKE
        color = Color.RED
    }

    private val paintFill = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private val paintText = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        isAntiAlias = true
        textSize = 60f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var x = 60f
        val y = 60f

        for (i in 0 until countStep) {
            if (i < currentStep) {
                canvas.drawCircle(x + 8f, y - 8f, radius, paintFill)
            } else canvas.drawCircle(x + 8f, y, radius - 8f, paintStroke)
            canvas.drawText("${i + 1}", x - 12f, y + 20f, paintText)
            x += 2 * radius + lineLength
        }

        x = 2 * radius

        for (i in 0 until countStep - 1) {
            canvas.drawLine(x, y, x + lineLength + 16f, y, paintStroke)
            x += 2 * radius + lineLength
        }

    }

//    fun setCurrentStep(step: Int) {
//        if (step < 0 || step > countStep) return
//        currentStep = step
//        invalidate()
//    }

    fun setDecStep() {
        if (currentStep < 0) return
        currentStep--
        invalidate()
    }

    fun setIncStep() {
        if (currentStep > countStep) return
        currentStep++
        invalidate()
    }

}
package uz.gita.castomview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.ContextCompat

private const val STROKE_WIDTH = 20f

class MyBoardView(context: Context) : View(context) {

    private val backgroundColor = ContextCompat.getColor(context, R.color.white)
    private val drawColor = ContextCompat.getColor(context, R.color.black)

    private val paint = Paint().apply {
        color = drawColor
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        isAntiAlias = true
        isDither = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val path = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {

            motionTouchEventX = it.x
            motionTouchEventY = it.y

            when (it.action) {
                // QO'lini tekkizganda
                MotionEvent.ACTION_DOWN -> startTouch()
                MotionEvent.ACTION_MOVE -> moveTouch()
                MotionEvent.ACTION_UP -> finishTouch()
                else -> {}
            }
        }

        return true
    }

    private fun finishTouch() {
        path.reset()
    }

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private fun moveTouch() {
        val differenceX = Math.abs(motionTouchEventX - currentX)
        val differenceY = Math.abs(motionTouchEventY - currentY)

        if (differenceX >= touchTolerance || differenceY >= touchTolerance) {
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)

            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Cache
            extraCanvas.drawPath(path, paint)

            // Manually chizishga majburlaydi
            invalidate()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if(::extraBitmap.isInitialized) extraBitmap.recycle()

        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)

        extraCanvas.drawColor(backgroundColor)
    }



    private fun startTouch() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
    }

}
package uz.gita.castomview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class MySwitchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {
    private var isTurn = true

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (isTurn) setImageResource(R.drawable.on_switch)
        else setImageResource(R.drawable.off_switch)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isTurn == event.x > width / 2) {
            isTurn = event.x < width / 2
            invalidate()
        }

        return super.onTouchEvent(event)
    }

}
package uz.gita.castomview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class SpeedoMeter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val DEFAULT_MAX_SPEED = 220f

    // Speedometer internal state
    private var mMaxSpeed = 0f
    private var mCurrentSpeed = 0f

    // Scale drawing tools
    private var onMarkPaint = Paint()
    private var offMarkPaint = Paint()
    private var scalePaint = Paint()
    private var readingPaint = Paint()
    private var onPath = Path()
    private var offPath = Path()
    val oval = RectF()

    // Drawing colors
    private var ON_COLOR = Color.GREEN
    private var OFF_COLOR = Color.argb(255, 0x3e, 0x3e, 0x3e)
    private var SCALE_COLOR = Color.argb(255, 255, 255, 255)
    private var SCALE_SIZE = 14f
    private var READING_SIZE = 60f

    // Scale configuration
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyView,
            0, 0
        )
        try {
            mMaxSpeed = a.getFloat(R.styleable.MyView_maxSpeed, DEFAULT_MAX_SPEED)
            mCurrentSpeed = a.getFloat(R.styleable.MyView_currentSpeed, 0f)
            ON_COLOR = a.getColor(R.styleable.MyView_onColor, ON_COLOR)
            OFF_COLOR = a.getColor(R.styleable.MyView_offColor, OFF_COLOR)
            SCALE_COLOR = a.getColor(R.styleable.MyView_scaleColor, SCALE_COLOR)
            SCALE_SIZE = a.getDimension(R.styleable.MyView_scaleTextSize, SCALE_SIZE)
            READING_SIZE = a.getDimension(R.styleable.MyView_readingTextSize, READING_SIZE)
        } finally {
            a.recycle()
        }
        initDrawingTools()
    }

    private fun initDrawingTools() {
        onMarkPaint = Paint()
        onMarkPaint.style = Paint.Style.STROKE
        onMarkPaint.color = ON_COLOR
        onMarkPaint.strokeWidth = 35f
        onMarkPaint.setShadowLayer(5f, 0f, 0f, ON_COLOR)
        onMarkPaint.isAntiAlias = true
        offMarkPaint = Paint(onMarkPaint)
        offMarkPaint.color = OFF_COLOR
        offMarkPaint.style = Paint.Style.FILL_AND_STROKE
        offMarkPaint.setShadowLayer(0f, 0f, 0f, OFF_COLOR)
        scalePaint = Paint(offMarkPaint)
        scalePaint.strokeWidth = 2f
        scalePaint.textSize = 30f
        scalePaint.setShadowLayer(5f, 0f, 0f, Color.RED)
        scalePaint.color = SCALE_COLOR
        readingPaint = Paint(scalePaint)
        readingPaint.style = Paint.Style.FILL_AND_STROKE
        offMarkPaint.setShadowLayer(3f, 0f, 0f, Color.WHITE)
        readingPaint.textSize = 65f
        readingPaint.typeface = Typeface.SANS_SERIF
        readingPaint.color = Color.WHITE
        onPath = Path()
        offPath = Path()
    }

    private fun setCurrentSpeed(mCurrentSpeed: Float) {
        if (mCurrentSpeed > mMaxSpeed) this.mCurrentSpeed =
            mMaxSpeed else if (mCurrentSpeed < 0) this.mCurrentSpeed = 0f else this.mCurrentSpeed =
            mCurrentSpeed
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {

        // Setting up the oval area in which the arc will be drawn
        radius = if (width > height) {
            (height / 4).toFloat()
        } else {
            (width / 4).toFloat()
        }
        oval[centerX - radius, centerY - radius, centerX + radius] = centerY + radius
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)
        val chosenDimension = chosenWidth.coerceAtMost(chosenHeight)
        centerX = (chosenDimension / 2).toFloat()
        centerY = (chosenDimension / 2).toFloat()
        setMeasuredDimension(chosenDimension, chosenDimension)
    }

    private fun chooseDimension(mode: Int, size: Int): Int {
        return if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            size
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            getPreferredSize()
        }
    }

    // in case there is no size specified
    private fun getPreferredSize() = 300

    override fun onDraw(canvas: Canvas) {

        val radius = min(centerX, centerY)

        drawScaleBackground(canvas)
        drawScale(canvas)
        drawLegend(canvas)
        drawReading(canvas)

        val angle = Math.PI - Math.PI * mCurrentSpeed / mMaxSpeed
        val endX = radius + cos(angle) * radius - 20f
        val endY = radius - sin(angle) * radius + 20f

        canvas.drawLine(radius, radius, endX.toFloat(), endY.toFloat(), paintFill)
    }

    private fun drawScaleBackground(canvas: Canvas) {
        offPath.reset()
        var i = -180
        while (i < 0) {
            offPath.addArc(oval, i.toFloat(), 2f)
            i += 4
        }
        canvas.drawPath(offPath, offMarkPaint)
    }

    private fun drawScale(canvas: Canvas) {
        onPath.reset()
        var i = -180
        while (i < mCurrentSpeed / mMaxSpeed * 180 - 180) {
            onPath.addArc(oval, i.toFloat(), 2f)
            i += 4
        }
        canvas.drawPath(onPath, onMarkPaint)
    }

    private fun drawLegend(canvas: Canvas) {
        canvas.save()
        canvas.rotate(-180f, centerX, centerY)
        val circle = Path()
        val halfCircumference = radius * Math.PI
        val increments = 20.0
        var i = 0
        while (i < mMaxSpeed) {
            circle.addCircle(centerX, centerY, radius, Path.Direction.CW)
            canvas.drawTextOnPath(
                String.format("%d", i),
                circle, (i * halfCircumference / mMaxSpeed).toFloat(),
                -40f,
                scalePaint
            )
            i += increments.toInt()
        }
        canvas.restore()
    }

    private fun drawReading(canvas: Canvas) {
        val path = Path()
        val message = String.format("%d", mCurrentSpeed.toInt())
        val widths = FloatArray(message.length)
        readingPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        path.moveTo(centerX - advance / 2, centerY)
        path.lineTo(centerX + advance / 2, centerY)
        canvas.drawTextOnPath(message, path, 0f, 0f, readingPaint)
    }

    fun onSpeedChanged(newSpeedValue: Float) {
        setCurrentSpeed(newSpeedValue)
        onMarkPaint.color = if (newSpeedValue <= 100) {
            readingPaint.color =  Color.GREEN
            Color.GREEN
        } else if (newSpeedValue <= 160){
            readingPaint.color =  Color.YELLOW
            Color.YELLOW
        }else{
            readingPaint.color =  Color.RED
            Color.RED
        }
            invalidate()
    }

    private val paintFill = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
}
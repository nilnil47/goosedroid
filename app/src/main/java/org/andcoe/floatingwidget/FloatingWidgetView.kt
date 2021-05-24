package org.andcoe.floatingwidget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.log

const val TAG = "gooseFloatingWidget"

class FloatingWidgetView : ConstraintLayout, View.OnTouchListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var gooseImage: ImageView
    private val layoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
        PixelFormat.TRANSLUCENT
    )

    private var x: Int = 0
    private var y: Int = 0
    private var touchX: Float = 0f
    private var touchY: Float = 0f
    private var clickStartTimer: Long = 0
    private val windowManager: WindowManager

    init {
        val view = inflate(context, R.layout.floating_widget_layout, this)
        gooseImage = view.findViewById<ImageView>(R.id.floatingIcon)

        setOnTouchListener(this)

        layoutParams.x = x
        layoutParams.y = y

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(this, layoutParams)


//        layoutParams.x = 500
//        layoutParams.y = 0
//        windowManager.updateViewLayout(this, layoutParams)

//        val path = Path().apply {
//            arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
//        }
//        val animator = ObjectAnimator.ofFloat(this, "x", 300f).apply {
//            duration = 2000
//            start()
//        }
    }

    fun setParamsX(x: Float) {
        layoutParams.x = x.toInt()
        Log.d(TAG, "setX: ${layoutParams.x}")
        windowManager.updateViewLayout(this, layoutParams)
    }

    fun setParamsY(y: Float) {
        Log.d(TAG, "setY: ${layoutParams.y}")
        layoutParams.y = y.toInt()
        windowManager.updateViewLayout(this, layoutParams)
    }

    companion object {
        private const val CLICK_DELTA = 200
    }

    fun goto(x : Float, y : Float) {
        val path = Path().apply {
//            arcTo(0f, 0f, x, y, 270f, -180f, true)
            quadTo(layoutParams.x.toFloat(), layoutParams.y.toFloat(), x, y)
        }
        val animator = ObjectAnimator.ofFloat(this, "paramsX", "paramsY", path).apply {
            duration = 2000
            start()
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "touch down: ")
                clickStartTimer = System.currentTimeMillis()

                x = layoutParams.x
                y = layoutParams.y

                touchX = event.rawX
                touchY = event.rawY

                gooseImage.rotation = 50f

//                val animator = ObjectAnimator.ofFloat(this, "paramsX", 300f).apply {
//                    duration = 2000
//                    start()
//                }

                goto(300f, 300f)
//                setParamsX(100f)
//                windowManager.updateViewLayout(this, layoutParams)

            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - clickStartTimer < CLICK_DELTA) {
                    Toast.makeText(context, "clicked floating widget", Toast.LENGTH_SHORT).show()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                layoutParams.x = (x + event.rawX - touchX).toInt()
                layoutParams.y = (y + event.rawY - touchY).toInt()
                Log.d(TAG, "onTouch: ${layoutParams.x}, ${layoutParams.y}")
                windowManager.updateViewLayout(this, layoutParams)
            }
        }
        return true
    }

}

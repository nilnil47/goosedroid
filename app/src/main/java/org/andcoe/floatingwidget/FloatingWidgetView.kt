package org.andcoe.floatingwidget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random


const val TAG = "gooseFloatingWidget"

const val animationTime: Long = 2000

class FloatingWidgetView : ConstraintLayout, View.OnTouchListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private val positionHandler: Handler = Handler()
    private var gooseImage: ImageView
    private val layoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
        PixelFormat.TRANSLUCENT
    )

    private var touchX: Float = 0f
    private var touchY: Float = 0f
    private var clickStartTimer: Long = 0
    private val windowManager: WindowManager

    init {
        val view = inflate(context, R.layout.floating_widget_layout, this)
        gooseImage = view.findViewById<ImageView>(R.id.floatingIcon)

        setOnTouchListener(this)

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(this, layoutParams)
        startWalking()
    }

    //
    fun startWalking() {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                val x: Float = Random.nextInt(-800, 800).toFloat()
                val y: Float = Random.nextInt(-1500, 1500).toFloat()
                goto(x, y)
                positionHandler.postDelayed(this, animationTime)
            }
        }
        positionHandler.post(runnable)
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


    fun goto(x: Float, y: Float) {
        Log.d(
            TAG,
            "goto: x: ${layoutParams.x.toFloat()} -> $x y: ${layoutParams.y.toFloat()} -> $y"
        )
        val path = Path().apply {
//            arcTo(0f, 0f, x, y, 270f, -180f, true)
//            lineTo(200f, 50f)

        }

        val objectAnimatorParamsX =
            ObjectAnimator.ofFloat(this, "paramsX", layoutParams.x.toFloat(), x)
        val objectAnimatorParamsY =
            ObjectAnimator.ofFloat(this, "paramsY", layoutParams.y.toFloat(), y)
        val animatorSet = AnimatorSet().apply {
            playTogether(objectAnimatorParamsX, objectAnimatorParamsY)
            duration = animationTime
            start()
        }
//        val animator = ObjectAnimator.ofFloat(this, "paramsX", "paramsY", path).apply {
//            duration = 2000
//            start()
//        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "touch down: ")
//                clickStartTimer = System.currentTimeMillis()

                touchX = event.rawX
                touchY = event.rawY

                Log.d(TAG, "onTouch: - layoutparams: ${layoutParams.x}, ${layoutParams.y}")
                goto(touchX, touchY)

                gooseImage.rotation = 50f

//                val animator = ObjectAnimator.ofFloat(this, "paramsX", 300f).apply {
//                    duration = 2000
//                    start()
//                }

//                goto(300f, 300f)
//                setParamsX(100f)
//                windowManager.updateViewLayout(this, layoutParams)

//            }
//            MotionEvent.ACTION_UP -> {
//                if (System.currentTimeMillis() - clickStartTimer < CLICK_DELTA) {
//                    Toast.makeText(context, "clicked floating widget", Toast.LENGTH_SHORT).show()
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                layoutParams.x = (x + event.rawX - touchX).toInt()
//                layoutParams.y = (y + event.rawY - touchY).toInt()
//                Log.d(TAG, "onTouch: ${layoutParams.x}, ${layoutParams.y}")
//                windowManager.updateViewLayout(this, layoutParams)
            }
        }
        return true
    }

}

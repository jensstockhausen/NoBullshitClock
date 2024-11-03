package de.famst.nobullshitclock

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val DEBUG_TAG = "MainActivity"

    private val executor = Executors.newScheduledThreadPool(1)
    private lateinit var clockText : TextView

    private val brightnessValues = listOf(255, 191, 80, 50, 20)
    private var currentBrightnessIndex = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        WindowInsetsControllerCompat(window, window.decorView)
            .hide(WindowInsetsCompat.Type.systemBars())

        // config UI
        setContentView(R.layout.activity_main)

        //val main = findViewById<View>(R.id.main)
        clockText = findViewById<View>(R.id.clockText) as TextView
        clockText.setTextColor(Color.rgb(brightnessValues[currentBrightnessIndex], 0,0))
        clockText.setAutoSizeTextTypeUniformWithConfiguration(1, 250, 1, TypedValue.COMPLEX_UNIT_DIP)

        executor.scheduleWithFixedDelay({
            updateClock()
        }, 0, 1, TimeUnit.SECONDS) // 1 second interval
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d(DEBUG_TAG, "Action was DOWN")

            return true
        }

        else if (event.action == MotionEvent.ACTION_UP) {
            Log.d(DEBUG_TAG, "Action was UP")

            currentBrightnessIndex++
            if (currentBrightnessIndex >= brightnessValues.size) {
                currentBrightnessIndex = 0
            }

            clockText.setTextColor(Color.rgb(brightnessValues[currentBrightnessIndex], 0,0))

            return true
        }

        return super.onTouchEvent(event)
    }


    @SuppressLint("SimpleDateFormat")
    private fun updateClock() {
        val clockText: TextView = findViewById<View>(R.id.clockText) as TextView
        val currentTime: Date = Calendar.getInstance().time

        var df: DateFormat = SimpleDateFormat("HH:MM")

        if(Calendar.getInstance().get(Calendar.SECOND) % 2 == 0) {
            df = SimpleDateFormat("HH MM")
        }

        currentTime.toString()
        clockText.text = df.format(currentTime)
    }
}
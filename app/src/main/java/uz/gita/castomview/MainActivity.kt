package uz.gita.castomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boardView = MyBoardView(this)

        setContentView(boardView)


//        val stepView =  findViewById<MyStepView>(R.id.stepView)
//        val incButton = findViewById<Button>(R.id.inc)
//        val decButton = findViewById<Button>(R.id.dec)

        /*val speedometer =  findViewById<SpeedoMeter>(R.id.speedometer)

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speedometer.onSpeedChanged(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })*/

//        incButton.setOnClickListener {
//            stepView.setIncStep()
//        }
//
//        decButton.setOnClickListener {
//            stepView.setDecStep()
//        }

    }


}
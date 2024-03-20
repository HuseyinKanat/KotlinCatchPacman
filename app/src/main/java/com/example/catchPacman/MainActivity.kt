package com.example.catchPacman

import android.content.DialogInterface
import android.icu.text.Normalizer2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.catchPacman.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var runnable: Runnable= Runnable{}
    var handler : Handler= Handler(Looper.getMainLooper())
    lateinit var arr: Array<ImageView>
    lateinit var mode:Modes
    private  var score= 0
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        arr = arrayOf( binding.iv00,binding.iv01,binding.iv02,binding.iv10,binding.iv11,binding.iv12,binding.iv20,binding.iv21,binding.iv22)
        var mySpinner = binding.spinner
        mySpinner.setAdapter( ArrayAdapter<Modes>(this, android.R.layout.simple_spinner_item, Modes.values()));

    }


    fun incrementScore(view: View){
        score = score + 1
        binding.tvScore.text = "Score: $score"

    }


    fun startGame(view:View) {
        mode= Modes.valueOf(binding.spinner.selectedItem.toString())
        showImageRandomly()
        binding.btnStart.visibility= Button.INVISIBLE
        binding.btnFinish.visibility=  Button.VISIBLE
        countDownTimer= object : CountDownTimer(15500, 1000) {
            override fun onFinish() {

                binding.tvClock.text = "Time: 0"
                binding.tvScore.text = "Your Score:"+0
                handler.removeCallbacks(runnable)
                binding.btnStart.visibility= Button.VISIBLE
                for (image in arr) {
                    image.visibility = View.INVISIBLE
                    binding.btnFinish.visibility=  Button.INVISIBLE
                }


                //Alert
                val alert = AlertDialog.Builder(this@MainActivity)

                alert.setTitle("Game Over")
                alert.setMessage("Score: $score Restart The Game?")
                alert.setPositiveButton("Yes") { dialog, which ->
                    //Restart
                    val intent = intent
                    finish()
                    startActivity(intent)


                }

                alert.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(this@MainActivity, "Game Over", Toast.LENGTH_LONG).show()
                }

                alert.show()
                score=0
                binding.tvScore.text = "Your Score:"+score

            }

            override fun onTick(millisUntilFinished: Long) {
                binding.tvClock.text = "Time: " + millisUntilFinished / 1000
            }


        }.start()
    }
    fun showImageRandomly() {
        runnable = Runnable {
            for (image in arr) {
                image.visibility = View.INVISIBLE
            }

            val random = Random
            val randomIndex = random.nextInt(9)
            arr[randomIndex].visibility = View.VISIBLE

            handler.postDelayed(runnable, mode.time)
        }

        handler.post(runnable)
    }
    fun finishGame(view: View){
        countDownTimer.let {
            it.cancel()
            it.onFinish()
        }
    }
}


enum class Modes{

    EASY{

        override val modeVal: Int
            get() = 1
        override val time: Long
            get() = 1000
    },

    NORMAL{
        override val modeVal: Int
            get() = 2
        override val time: Long
            get() = 500
    },

    HARD{
        override val modeVal: Int
            get() = 4
        override val time: Long
            get() = 250

    },
    VETERAN{
        override val modeVal: Int
            get() = 5
        override val time: Long
            get() = 200

    },

    NIGHTMARE{
        override val modeVal: Int
            get() = 10
        override val time: Long
            get() = 100

    };

    abstract val modeVal:Int
    abstract  val time :Long

}

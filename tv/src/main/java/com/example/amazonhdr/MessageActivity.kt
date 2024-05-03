package com.example.amazonhdr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message2)
        val mIntent = intent
        val displayID = mIntent.getIntExtra("displayID", -1)
//        val nxtBtn = findViewById<Button>(R.id.message_button)
        val nxtBtn = findViewById<Button>(R.id.message_button)
        nxtBtn.setBackgroundResource(R.color.red)

        if (displayID == 0)
        {
            Log.d("MESSAGE_ACTIVITY",  getString(R.string.training_start_message))
            findViewById<TextView>(R.id.textView).text = getString(R.string.training_start_message)
            nxtBtn.setOnClickListener {
                moveToTrainHumanStudy()
            }
        }
        else if (displayID == 1)
        {

            findViewById<TextView>(R.id.textView).text =  getString(R.string.session_start_message)
            nxtBtn.setOnClickListener {
                moveToHumanStudy()
            }
        }
        else        {
            findViewById<TextView>(R.id.textView).text = getString(R.string.session_end_message)
            nxtBtn.setText(R.string.finish)
            nxtBtn.setOnClickListener {
                moveToMainPage()
            }
        }


    }
    private fun moveToTrainHumanStudy()
    {
        Log.d("MESSAGE_ACTIVITY", "Going to Video Player Page")
        val intent: Intent = Intent(
            this,
            PlayerActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("train",true)
        startActivity(intent)
    }

    private fun moveToHumanStudy() {
        Log.d("MESSAGE_ACTIVITY", "Going to Video Player Page")
        val intent: Intent = Intent(
            this,
            PlayerActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("train",false)
        startActivity(intent)
    }

    private fun moveToMainPage() {
        val intent: Intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    enum class DisplayState(val id : Int, val stringValue : String)
    {
        WELCOME_SCREEN_TRAINING(0, R.string.training_start_message.toString()),
        WELCOME_SCREEN(1, R.string.session_start_message.toString()),
        END_CREEN(2, R.string.session_end_message.toString())
    }


}
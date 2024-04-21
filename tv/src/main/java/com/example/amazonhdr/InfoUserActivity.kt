package com.example.amazonhdr

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InfoUserActivity : AppCompatActivity() {

    private var txtUserID: EditText? = null
    private var txtPreferredName:EditText? = null
    private var txtSessionID:EditText? = null
    private var btnLogin: Button? = null
    private var mLastClickTime1: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_user)

        txtUserID = findViewById(R.id.txtUserID)
        txtPreferredName = findViewById(R.id.txtPreferredName)
        txtSessionID = findViewById(R.id.txtSessionID)
        btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin?.setOnClickListener {
            mLastClickTime1 = SystemClock.elapsedRealtime()

            val userID = txtUserID?.text.toString()
            val preferredName = txtPreferredName?.text.toString()
            val sessionID = txtSessionID?.text.toString()

            if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(preferredName) || TextUtils.isEmpty(sessionID)) {
                Toast.makeText(this, "One or more empty field, please try again",Toast.LENGTH_SHORT).show()

            } else if (!TextUtils.isDigitsOnly(userID) || !TextUtils.isDigitsOnly(sessionID)) {
                Toast.makeText(this, "User ID and Session ID should be digits only",Toast.LENGTH_SHORT).show()
            } else {
                val user = User(userID.toInt(), preferredName, sessionID.toInt())
                Toast.makeText(this, "One or more empty field, please try again",Toast.LENGTH_SHORT).show()
                val sessionManagement = SessionManagement(this)
                sessionManagement.saveSession(user)
                if (user.sessID.toString() == "1")
                {
                    user.isTrain = true
                    sessionManagement.setIsTrain(true)
                    showStartMessage(0)

                }
                else
                {
                    sessionManagement.setIsTrain(false)
                    showStartMessage(1)
                }


            }

        }
    }

    private fun showTraining()
    {

    }

    private fun showStartMessage(id: Int)
    {
        Log.d("INFO_USER_ACTIVITY", "Going to Message Page")
        val intent: Intent = Intent(
            this,
            MessageActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("displayID", id);
        startActivity(intent)
    }

    private fun moveToHumanStudy() {
        Log.d(null, "Going to Video Player Page")
        val intent: Intent = Intent(
            this,
            PlayerActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
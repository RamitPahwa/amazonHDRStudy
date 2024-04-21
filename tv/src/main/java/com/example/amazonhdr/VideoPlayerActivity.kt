package com.example.amazonhdr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class VideoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val sessionManagement = SessionManagement(this@VideoPlayerActivity)
        val videoIndex = sessionManagement.videoCount
        val userID = sessionManagement.session
        val sessID = sessionManagement.sessID
        val playlistFilePath = userID.toString() + "_" + sessID.toString() + ".txt"
        println("Accessing File")
        println(playlistFilePath)
        sessionManagement.incrementVideoCount()
        val length = 103
        if (videoIndex != length) {

            //setVideo(files[videoIndex].getName());
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(
                    InputStreamReader(assets.open(playlistFilePath), "UTF-8")
                )
                val mLine: String
                for (loop in 0 until videoIndex) reader.readLine()
                mLine = reader.readLine()
                println(mLine)
//                setVideo(mLine)
            } catch (e: IOException) {
                println(e)
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        println(e)
                    }
                }
            }
        } else {
//            moveToFinalPage()
    }
    }
}
package com.example.amazonhdr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date

class ScoreCaptureActivity : AppCompatActivity() {


    private var seekbar: SeekBar? = null
    private var btnNext: Button? = null
    private var mLastClickTime: Long = 0
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_capture)

        seekbar = findViewById<SeekBar>(R.id.seekBar)
        btnNext = findViewById<Button>(R.id.btnNext)
        btnNext?.isEnabled = false

        val random =
            0 //changing to zero to avoid any bias due to randomization new Random().nextInt(101);

        seekbar?.setProgress(random)
        seekbar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                btnNext?.setEnabled(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        btnNext?.setOnClickListener {
            val sessionManagementRate = SessionManagement(this)
            val idRate = sessionManagementRate.session
            val nameRate = sessionManagementRate.name
            val sessIDRate = sessionManagementRate.sessID
            val videoIndexRate = sessionManagementRate.videoCount - 1
            val trainVideoIndexRate = sessionManagementRate.trainVideoCount - 1
            val isTrain = sessionManagementRate.isTrain


            if (isTrain)
            {
                var trainVideoNameRate = ""

                val trainFileNames = listOf<String>(
                    "/Download/Live_sports1_HDR10_3840x2160_6000k_11.mp4",
                    "/Download/Live_sports1_HDR10_3840x2160_6000k_11.mp4",
                    "/Download/Live_sports1_HDR10_3840x2160_6000k_11.mp4"
                )

                val videoNameRate = trainFileNames[trainVideoIndexRate]

                val s = SimpleDateFormat("ddMMyyyyhhmmss")
                val date_format = s.format(Date())

                val path_file = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + idRate.toString() + "_" + nameRate + "_" + sessIDRate.toString() + "_train.txt"
                val scoreFileRate = File(path_file)
                try {
                    scoreFileRate.createNewFile()
                    val outStreamRate = FileOutputStream(scoreFileRate, true)
                    outStreamRate.write((date_format + "," + idRate.toString() + "," + nameRate + "," + sessIDRate.toString() + "," + videoNameRate + "," + seekbar?.progress + "\n").toByteArray())
                    outStreamRate.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                moveToNextVideo(isTrain)

            }
            else
            {
                var videoNameRate = ""

                var reader: BufferedReader? = null
                val playlistFilePath = idRate.toString() + "_" + sessIDRate.toString() + ".txt"
                try {
                    reader = BufferedReader(
                        InputStreamReader(assets.open(playlistFilePath), "UTF-8")
                    )
                    var mLine: String
                    for (loop in 0 until videoIndexRate) reader.readLine()
                    videoNameRate = reader.readLine()
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

                val s = SimpleDateFormat("ddMMyyyyhhmmss")
                val date_format = s.format(Date())

                val path_file = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + idRate.toString() + "_" + nameRate + "_" + sessIDRate.toString() + ".txt"
                val scoreFileRate = File(path_file)
                try {
                    scoreFileRate.createNewFile()
                    val outStreamRate = FileOutputStream(scoreFileRate, true)
                    outStreamRate.write((date_format + "," + idRate.toString() + "," + nameRate + "," + sessIDRate.toString() + "," + videoNameRate + "," + seekbar?.progress + "\n").toByteArray())
                    outStreamRate.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                moveToNextVideo(false)
            }
            }
    }


    private fun moveToNextVideo(isTrain : Boolean) {
        val intent = Intent(
            this,
            PlayerActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("train", isTrain)
        startActivity(intent)
    }
}
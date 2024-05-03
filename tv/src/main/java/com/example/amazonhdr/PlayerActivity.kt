package com.example.amazonhdr

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Display.HdrCapabilities.HDR_TYPE_HLG
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import com.example.amazonhdr.databinding.ActivityPlayerBinding
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

private const val TAG = "PlayerActivity"

class PlayerActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private val playbackStateListener: Player.Listener = playbackStateListener()
    private var player: Player? = null

    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val sessionManagement = SessionManagement(this@PlayerActivity)
        val videoIndex = sessionManagement.videoCount
        val userID = sessionManagement.session
        val sessID = sessionManagement.sessID
        val tvID = sessionManagement.tvid
        val trainVideoIndex = sessionManagement.trainVideoCount
        val playlistFilePath = userID.toString() + "_" + sessID.toString() + "_TV"+ tvID.toString() + ".txt"

        var trainSession = true

        trainSession = intent.getBooleanExtra("train", false)
        val trainFileNames = listOf<String>(
            "/Download/HDR10+.mp4",
            "/Download/HDR10.mp4",
            "/Download/SDR.mp4"
        )

        if (trainSession) {
            val totalTrainLength = 3
            if (trainVideoIndex != totalTrainLength) {
                sessionManagement.incrementTrainVideoCount()
                try {

                    setVideo(trainFileNames[trainVideoIndex], sessionManagement.isTrain)
                } catch (e: Exception) {
                    Log.d("PLAYER_ACTIVITY", "In the train session exception")
                }
            } else {
                // move to main session message screen
                sessionManagement.setIsTrain(false)
                moveToSessionStartScreen()
            }

        } else {
            sessionManagement.incrementVideoCount()
            playVideo(videoIndex, playlistFilePath)

        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    public fun playVideo(videoIndex :Int, playlistFilePath : String)
    {
        val length = 135
        if (videoIndex != length) {
            // /sdcard/Download/WOT_S2E1_HDR10_3840x2160_6000k_8.mp4
            //setVideo(files[videoIndex].getName());
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(
                    InputStreamReader(assets.open(playlistFilePath), "UTF-8")
                )
                val mLine: String
                for (loop in 0 until videoIndex) reader.readLine()
                mLine = reader.readLine()
                Log.d("player", mLine)
                setVideo(mLine, false)
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
            moveToFinalPage()
        }
    }

    //    public override fun onStart() {
//        super.onStart()
//        if (Build.VERSION.SDK_INT > 23) {
////            initializePlayer()
//        }
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        hideSystemUi()
//        if (Build.VERSION.SDK_INT <= 23 || player == null) {
////            initializePlayer()
//        }
//    }
//
    // /storage/emulated/0/Download/Live_sports2_HDR10_3840x2160_15000k_8.mp4
    // /storage/emulated/0/Download/Live_sports2_HDR10_3840x2160_15000k_8.mp4
    @androidx.annotation.OptIn(UnstableApi::class)
    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    @OptIn(UnstableApi::class)
    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }
//    /storage/08F0-1B5B/DATASET/videos

    @RequiresApi(Build.VERSION_CODES.R)
    private fun copyFiles(playlistFilePath : String, playlistLength : Int, onCompletionCallBack: onCompletionCallBack)
    {
        val sourceVideoPath = Environment.getStorageDirectory().absolutePath + "/08F0-1B5B/DATASET/videos/"
        val targetVideoPath = Environment.getExternalStorageDirectory().absolutePath

        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(
                InputStreamReader(assets.open(playlistFilePath), "UTF-8")
            )
            val mLine: String
            for (loop in 0 until playlistLength)
            {
                var videoPath = reader.readLine()
                var videoName = videoPath.split("/")[2]
                var s_path = sourceVideoPath + videoName
                var sFile = File(s_path)
                var d_path = targetVideoPath + videoPath
                val to = File(d_path)
                if(to.exists().not()) {
                    to.createNewFile()

                }
                sFile.copyTo(to, true)
            }
//            onCompletionCallBack.onComplete()
        } catch (e: IOException) {
            onCompletionCallBack.onError()
            println(e)
        } finally {
            if (reader != null) {
                try {
                    onCompletionCallBack.onComplete()
                    reader.close()
                } catch (e: IOException) {
                    onCompletionCallBack.onError()
                    println(e)
                }
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun setVideo(fileName: String, train: Boolean) {
        val videoPath = Environment.getExternalStorageDirectory().absolutePath + "/" + fileName.split("/")[1] + "/TV1_merged/" +fileName.split("/")[2]
        val videoUri = Uri.parse(videoPath)
        val mediaItem = MediaItem.fromUri(videoUri)

        Log.d("AmazonHDR", videoPath)
        val capabilities = display?.hdrCapabilities?.supportedHdrTypes ?: intArrayOf()
        Log.d("AmazonHDR", "HDR Capable:" + capabilities.contains(HDR_TYPE_HLG).toString())

//        if (!capabilities.contains(HDR_TYPE_HLG)) {
//            throw RuntimeException("Display does not support desired HDR type");
//        }
        // /storage/emulated/0/Download/WOT_S2E1_HDR10_3840x2160_6000k_8.mp4
//        val mediaItem = MediaItem.Builder()
//            .setUri("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
//            .setMi bmeType(MimeTypes.APPLICATION_MPD)
//            .build()
        initializePlayer(mediaItem, train)

    }

    private fun initializePlayer(mediaItem: MediaItem, train: Boolean) {
        // ExoPlayer implements the Player interface
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                viewBinding.videoView.player = exoPlayer
                // Update the track selection parameters to only pick standard definition tracks
                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSizeSd()
                    .build()

                exoPlayer.setMediaItems(listOf(mediaItem), mediaItemIndex, playbackPosition)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> endOfPlayback()
                else -> "UNKNOWN_STATE             -"
            }
            Log.d(TAG, "changed state to $stateString")
        }
    }

    private fun endOfPlayback(): String {
        Log.d("Player Activity", "End of Playback ")
        val intent: Intent = Intent(
            this,
            ScoreCaptureActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        return "Done with Score Capture"
    }

    private fun moveToFinalPage() {
        val intent: Intent = Intent(
            this,
            MessageActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("displayID", 2);
        startActivity(intent)
    }

    private fun moveToSessionStartScreen()
    {
        val intent: Intent = Intent(
            this,
            MessageActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("displayID", 1)
        startActivity(intent)
    }

    public interface onCompletionCallBack
    {
        fun onComplete()
        fun onError()
    }
}
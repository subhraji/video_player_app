package com.example.videoplayerapp

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.videoplayerapp.databinding.ActivityMainBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object{
        private var isFullScreen = false
        private var isLock = false
    }
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private lateinit var bt_lock: ImageView
    private lateinit var bt_fullscreen: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simpleExoplayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        binding.player.player = simpleExoplayer
        binding.player.keepScreenOn = true

        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        bt_lock = findViewById<ImageView>(R.id.exo_lock)

        bt_fullscreen.setOnClickListener {
            if(!isFullScreen){
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen_exit))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            }else{
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            isFullScreen = !isFullScreen
        }

        bt_lock.setOnClickListener {
            if(!isLock){
                bt_lock.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock))
            }else{
                bt_lock.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_lock_open))
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        simpleExoplayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if(playbackState == Player.STATE_BUFFERING){
                    binding.progressBar.visibility = View.VISIBLE
                }else if(playbackState == Player.STATE_READY){
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        val videoSource = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoplayer.setMediaItem(mediaItem)
        simpleExoplayer.prepare()
        simpleExoplayer.play()
    }

    private fun lockScreen(lock: Boolean) {
        val sec_vid = findViewById<LinearLayout>(R.id.sec_controllvid1)
        val sec_button = findViewById<LinearLayout>(R.id.sec_controllvid2)
        if(lock){
            sec_vid.visibility = View.INVISIBLE
            sec_button.visibility = View.INVISIBLE
        }else{
            sec_vid.visibility = View.VISIBLE
            sec_button.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if(isLock) return
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            bt_fullscreen.performClick()
        }
        else super.onBackPressed()
    }

    override fun onStop() {
        simpleExoplayer.stop()
        super.onStop()
    }

    override fun onDestroy() {
        simpleExoplayer.release()
        super.onDestroy()
    }

    override fun onPause() {
        simpleExoplayer.pause()
        super.onPause()
    }
}
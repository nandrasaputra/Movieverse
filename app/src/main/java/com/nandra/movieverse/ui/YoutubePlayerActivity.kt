package com.nandra.movieverse.ui

import android.os.Bundle
import android.widget.Toast
import com.endiar.movieverse.core.utils.Constant
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.nandra.movieverse.R

class YoutubePlayerActivity : YouTubeBaseActivity() {

    private lateinit var youTubePlayerView: YouTubePlayerView
    lateinit var youTubePlayer: YouTubePlayer
    private val apiKey = getString(R.string.GOOGLE_YOUTUBE_API)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        val videoKey = intent.getStringExtra(Constant.EXTRA_YOUTUBE_KEY)

        youTubePlayerView = findViewById(R.id.youtube_playerview)
        youTubePlayerView.initialize(apiKey, object: YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
                if(!wasRestored) {
                    youTubePlayer = player!!
                    youTubePlayer.apply {
                        setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        loadVideo(videoKey)
                        play()
                    }
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider?, error: YouTubeInitializationResult?) {
                val errorMessage = error.toString()
                Toast.makeText(this@YoutubePlayerActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
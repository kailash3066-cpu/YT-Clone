package com.audioapp.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.audioapp.cache.CacheHolder

object AudioPlayer {

    @Volatile
    private var exoPlayer: ExoPlayer? = null
    private var playerListener: Player.Listener? = null

    fun initialize(context: Context) {
        if (exoPlayer == null) {
            synchronized(this) {
                if (exoPlayer == null) {
                    exoPlayer = ExoPlayer.Builder(context.applicationContext)
                        .build()
                        .apply {
                            playWhenReady = false
                        }
                }
            }
        }
    }

    fun getPlayer(): ExoPlayer? = exoPlayer

    fun playAudio(context: Context, url: String) {
        val player = exoPlayer ?: run {
            initialize(context)
            exoPlayer!!
        }

        val cache = CacheHolder.getCache(context)
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(dataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    fun pauseAudio() {
        exoPlayer?.playWhenReady = false
    }

    fun stopAudio() {
        exoPlayer?.apply {
            stop()
            clearMediaItems()
        }
    }

    fun resumeAudio() {
        exoPlayer?.playWhenReady = true
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    fun setPlayerListener(listener: Player.Listener) {
        playerListener?.let { exoPlayer?.removeListener(it) }
        playerListener = listener
        exoPlayer?.addListener(listener)
    }

    fun removePlayerListener() {
        playerListener?.let { exoPlayer?.removeListener(it) }
        playerListener = null
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        playerListener = null
    }
}

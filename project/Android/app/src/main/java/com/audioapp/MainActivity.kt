package com.audioapp

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.audioapp.player.AudioPlayer
import com.audioapp.service.AudioService

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var audioService: AudioService? = null
    private var serviceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.AudioBinder
            audioService = binder.getService()
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
            serviceBound = false
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startAudioService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AudioPlayer.initialize(this)

        webView = findViewById(R.id.webview)
        setupWebView()

        requestNotificationPermissionIfNeeded()
    }

    private fun setupWebView() {
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = true
                allowContentAccess = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()

            addJavascriptInterface(NativeInterface(), "Native")

            loadUrl("https://your-web-app-url.com")
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startAudioService()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            startAudioService()
        }
    }

    private fun startAudioService() {
        val intent = Intent(this, AudioService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    inner class NativeInterface {

        @JavascriptInterface
        fun playAudio(url: String) {
            runOnUiThread {
                AudioPlayer.playAudio(this@MainActivity, url)

                if (serviceBound && audioService != null) {
                    audioService?.startForegroundService()
                }
            }
        }

        @JavascriptInterface
        fun pauseAudio() {
            runOnUiThread {
                AudioPlayer.pauseAudio()
            }
        }

        @JavascriptInterface
        fun stopAudio() {
            runOnUiThread {
                AudioPlayer.stopAudio()
            }
        }

        @JavascriptInterface
        fun resumeAudio() {
            runOnUiThread {
                AudioPlayer.resumeAudio()
            }
        }

        @JavascriptInterface
        fun isPlaying(): Boolean {
            return AudioPlayer.isPlaying()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
        webView.destroy()
        super.onDestroy()
    }
}

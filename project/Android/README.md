# Android Audio WebView App

A native Android app that loads your web UI in a WebView and replaces web audio playback with ExoPlayer for background audio support and media controls.

## Features

- **WebView Integration**: Loads your web UI with full JavaScript support
- **ExoPlayer Audio**: Native audio playback with background support
- **Media Controls**: Notification controls for play/pause
- **Spotify-Style Caching**: Intelligent cache management (20% of storage, 500MB-10GB)
- **JavaScript Bridge**: Simple API to control audio from web

## JavaScript API

Use these methods from your web app:

```javascript
// Play audio
Native.playAudio("https://example.com/audio.mp3");

// Pause audio
Native.pauseAudio();

// Stop audio
Native.stopAudio();

// Resume audio
Native.resumeAudio();

// Check if playing
const playing = Native.isPlaying();
```

## Project Structure

```
Android/
├── app/
│   ├── src/main/
│   │   ├── java/com/audioapp/
│   │   │   ├── cache/
│   │   │   │   ├── SegmentLruEvictor.kt    # LRU cache eviction
│   │   │   │   ├── CacheUtils.kt            # Cache size calculations
│   │   │   │   └── CacheHolder.kt           # Cache singleton
│   │   │   ├── player/
│   │   │   │   └── AudioPlayer.kt           # ExoPlayer wrapper
│   │   │   ├── service/
│   │   │   │   └── AudioService.kt          # Foreground service
│   │   │   └── MainActivity.kt              # WebView activity
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       └── colors.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradlew
```

## Build Instructions

### Local Build

1. Install Android Studio or Android SDK
2. Navigate to the Android directory:
   ```bash
   cd Android
   ```
3. Make gradlew executable:
   ```bash
   chmod +x gradlew
   ```
4. Build the APK:
   ```bash
   ./gradlew assembleDebug
   ```
5. Find the APK at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### GitHub Actions Build

The project includes a GitHub Actions workflow that automatically builds the APK:

- **Trigger**: Push to `main` or `master`, pull requests, or manual workflow dispatch
- **Output**: APK artifact available in the Actions tab
- **Requirements**: No setup needed, workflow is pre-configured

To trigger manually:
1. Go to your GitHub repository
2. Click "Actions" tab
3. Select "Build Android APK"
4. Click "Run workflow"

## Configuration

### Change Web URL

Edit `MainActivity.kt` line with `loadUrl`:

```kotlin
webView.loadUrl("https://your-web-app-url.com")
```

### Change Package Name

1. Update `app/build.gradle`:
   ```gradle
   defaultConfig {
       applicationId "com.yourcompany.yourapp"
   }
   ```

2. Update `AndroidManifest.xml`:
   ```xml
   <manifest package="com.yourcompany.yourapp">
   ```

3. Rename package directories accordingly

### Adjust Cache Size

Edit `CacheUtils.kt` to change cache parameters:

```kotlin
private const val MIN_CACHE_SIZE = 500L * 1024 * 1024  // 500 MB
private const val MAX_CACHE_SIZE = 10L * 1024 * 1024 * 1024  // 10 GB
private const val CACHE_PERCENTAGE = 0.20  // 20%
```

## Dependencies

- AndroidX Core & AppCompat
- Material Components
- Media3 ExoPlayer (v1.2.1)
- Media3 Session
- Media3 DataSource & Database

## Permissions

The app requests:
- `INTERNET`: For loading web content and streaming audio
- `FOREGROUND_SERVICE`: For background audio playback
- `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: Android 14+ requirement
- `POST_NOTIFICATIONS`: For media controls notification
- `WAKE_LOCK`: To keep audio playing

## Minimum Requirements

- **Android SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **JDK**: 17

## How It Works

1. **WebView**: Loads your web UI with JavaScript enabled
2. **JS Bridge**: Exposes `Native` interface to JavaScript
3. **ExoPlayer**: Single global instance handles all audio
4. **Caching**: Downloads audio segments and caches intelligently
5. **Service**: Foreground service keeps audio alive in background
6. **MediaSession**: Integrates with Android media system
7. **Notifications**: Shows media controls in notification shade

## Troubleshooting

### Build Fails

- Ensure JDK 17 is installed
- Run `./gradlew clean` before building
- Check internet connection (needed for dependencies)

### Audio Not Playing

- Check URL is accessible
- Verify INTERNET permission is granted
- Check audio format is supported by ExoPlayer

### No Notification

- Grant POST_NOTIFICATIONS permission on Android 13+
- Check notification channel is created

## License

Proprietary

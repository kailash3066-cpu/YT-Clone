# Add project specific ProGuard rules here.
-keep class com.audioapp.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

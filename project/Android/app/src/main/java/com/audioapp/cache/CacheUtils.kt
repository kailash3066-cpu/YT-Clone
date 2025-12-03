package com.audioapp.cache

import android.content.Context
import android.os.StatFs
import java.io.File

object CacheUtils {

    private const val MIN_CACHE_SIZE = 500L * 1024 * 1024
    private const val MAX_CACHE_SIZE = 10L * 1024 * 1024 * 1024
    private const val CACHE_PERCENTAGE = 0.20

    fun calculateOptimalCacheSize(context: Context): Long {
        val cacheDir = context.getExternalFilesDir(null) ?: context.cacheDir
        val stat = StatFs(cacheDir.path)
        val availableBytes = stat.availableBlocksLong * stat.blockSizeLong

        val calculatedSize = (availableBytes * CACHE_PERCENTAGE).toLong()

        return when {
            calculatedSize < MIN_CACHE_SIZE -> MIN_CACHE_SIZE
            calculatedSize > MAX_CACHE_SIZE -> MAX_CACHE_SIZE
            else -> calculatedSize
        }
    }

    fun getCacheDirectory(context: Context): File {
        val cacheDir = File(context.getExternalFilesDir(null), "audio_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir
    }

    fun getCacheSize(cacheDir: File): Long {
        var size = 0L
        if (cacheDir.exists()) {
            cacheDir.walkTopDown().forEach { file ->
                if (file.isFile) {
                    size += file.length()
                }
            }
        }
        return size
    }
}

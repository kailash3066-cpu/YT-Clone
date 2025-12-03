package com.audioapp.cache

import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

object CacheHolder {

    @Volatile
    private var simpleCache: SimpleCache? = null

    fun getCache(context: Context): SimpleCache {
        return simpleCache ?: synchronized(this) {
            simpleCache ?: createCache(context).also { simpleCache = it }
        }
    }

    private fun createCache(context: Context): SimpleCache {
        val cacheDir = CacheUtils.getCacheDirectory(context)
        val cacheSize = CacheUtils.calculateOptimalCacheSize(context)
        val databaseProvider = StandaloneDatabaseProvider(context)
        val evictor = SegmentLruEvictor(cacheSize)

        return SimpleCache(cacheDir, evictor, databaseProvider)
    }

    fun releaseCache() {
        simpleCache?.release()
        simpleCache = null
    }
}

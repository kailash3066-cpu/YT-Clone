package com.audioapp.cache

import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheSpan
import java.io.File

class SegmentLruEvictor(
    private val maxCacheBytes: Long
) : Cache.Evictor {

    private val accessOrder = mutableMapOf<String, Long>()
    private var currentSize = 0L
    private var timestamp = 0L

    override fun requiresCacheSpanTouches(): Boolean = true

    override fun onCacheInitialized() {
        currentSize = 0L
        accessOrder.clear()
    }

    override fun onStartFile(
        cache: Cache,
        key: String,
        position: Long,
        length: Long
    ) {
        if (length != -1L) {
            evictCache(cache, length)
        }
    }

    override fun onSpanAdded(cache: Cache, span: CacheSpan) {
        currentSize += span.length
        accessOrder[span.key] = ++timestamp
        evictCache(cache, 0)
    }

    override fun onSpanRemoved(cache: Cache, span: CacheSpan) {
        currentSize -= span.length
    }

    override fun onSpanTouched(cache: Cache, oldSpan: CacheSpan, newSpan: CacheSpan) {
        accessOrder[newSpan.key] = ++timestamp
    }

    private fun evictCache(cache: Cache, requiredSpace: Long) {
        while (currentSize + requiredSpace > maxCacheBytes && accessOrder.isNotEmpty()) {
            val lruKey = accessOrder.entries.minByOrNull { it.value }?.key ?: break

            val cachedSpans = cache.getCachedSpans(lruKey)
            if (cachedSpans.isNotEmpty()) {
                for (span in cachedSpans) {
                    try {
                        cache.removeSpan(span)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            accessOrder.remove(lruKey)
        }
    }
}

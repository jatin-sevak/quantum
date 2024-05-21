package com.example.quantum.Helper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageCache {
    private val memoryCache: LruCache<String, Bitmap>
    private val diskCacheDir = File("/absolute/path/to/cache/dir")

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs()
        }
    }

    fun addBitmapToCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            memoryCache.put(key, bitmap)
            addBitmapToDiskCache(key, bitmap)
        }
    }

    fun getBitmapFromMemoryCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }

    fun getBitmapFromDiskCache(key: String): Bitmap? {
        val file = File(diskCacheDir, key.hashCode().toString())
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    private fun addBitmapToDiskCache(key: String, bitmap: Bitmap) {
        val file = File(diskCacheDir, key.hashCode().toString())
        try {
            if (!file.exists()) {
                val out: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.close()
            }
        }catch(e: Exception) {
            e.printStackTrace()
        }
    }
}

package com.example.quantum.Helper
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.util.concurrent.Executors

object ImageLoader {
    private val client = OkHttpClient()
    private val executor = Executors.newFixedThreadPool(4)
    private val mainHandler = Handler(Looper.getMainLooper())

    fun loadImage(url: String, imageView: ImageView) {
        // Check memory cache
        val cachedBitmap = ImageCache.getBitmapFromMemoryCache(url)
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap)
            return
        }

        // Check disk cache
        executor.execute {
            val diskBitmap = ImageCache.getBitmapFromDiskCache(url)
            if (diskBitmap != null) {
                ImageCache.addBitmapToCache(url, diskBitmap)
                mainHandler.post {
                    imageView.setImageBitmap(diskBitmap)
                }
                return@execute
            }

            // Load from network
            try {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw Exception("Failed to load image: ${response.code}")

                    val inputStream: InputStream? = response.body?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    ImageCache.addBitmapToCache(url, bitmap)

                    mainHandler.post {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mainHandler.post {
                    imageView.setImageResource(android.R.drawable.stat_notify_error)
                }
            }
        }
    }
}

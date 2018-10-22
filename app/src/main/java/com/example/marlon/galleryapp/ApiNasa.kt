package com.example.marlon.galleryapp


import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


const val CACHE_CONTROL = "Cache-Control"

const val API_KEY = "D4qaqQHftAk3mp7OkM7kbUArBzTiGaxEZymsHXHT"

class ApiNasa {
    companion object {
        private const val BASE_URL =
            "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/"//photos?sol=50"//&page=3"
        private var retrofit: Retrofit? = null
        private lateinit var weakReference: WeakReference<Context>
        /**
         *@param weakReference reference to the activity or fragment that calls the method
         */

        fun getApi(weakReference: WeakReference<Context>): Retrofit? {
            // Creates the okHttp client for save and load the cache
            this.weakReference = weakReference
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build()

            // Creates the retrofit client for request and read the json
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

        private fun provideOfflineCacheInterceptor(): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()
                // If is connected to the server, writes in cache
                if (isConnected()) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(60, TimeUnit.DAYS)
                        .build()
                    request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
                }
                chain.proceed(request)
            }
        }

        private fun provideCacheInterceptor(): Interceptor {
            return Interceptor { chain ->
                val response = chain.proceed(chain.request())

                // re-write response header to force use of cache
                val cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()

                response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build()
            }
        }

        // Use the activity or fragment cache
        private fun provideCache(): Cache? {
            var cache: Cache? = null
            try {
                cache = Cache(
                    File(weakReference.get()?.cacheDir, "http-cache"),
                    10 * 1024 * 1024
                ) // 10 MB
            } catch (e: Exception) {
                Log.e(TAG, "Could not create Cache!")
            }

            return cache
        }

        // Return true if is connected
        @Throws(InterruptedException::class, IOException::class)
        fun isConnected(): Boolean {
            val command = "ping -c 1 $BASE_URL"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        }
    }


}
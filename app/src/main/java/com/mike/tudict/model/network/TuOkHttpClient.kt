package com.mike.tudict.model.network

import android.util.Log
import com.mike.tudict.Constants.NETWORK.CONNECTION_TIMEOUT
import com.mike.tudict.Constants.NETWORK.HTTP_DEBUG
import com.mike.tudict.Constants.NETWORK.HTTP_DEBUG_TAG
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


/**
 * Author: Mike
 * Email: bhw8412@hotmail.com
 * Date: 2019/4/29
 * Description:
 */
class TuOkHttpClient {
    companion object {
        val instance = TuOkHttpClient()
    }

    val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            callbackLog(
                message
            )
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(httpLoggingInterceptor)

        builder.addInterceptor { chain ->
            val request =
                chain.request().newBuilder().removeHeader("User-Agent").addHeader("User-Agent", getUserAgent()).build()
            chain.proceed(request)
        }
        okHttpClient = builder.build()
    }

    private fun callbackLog(message: String) {
        if (HTTP_DEBUG) {
            Log.i(HTTP_DEBUG_TAG, "retrofitBack = $message")
        }
    }

    private fun getUserAgent(): String {
        var userAgent: String? = ""
        val sb = StringBuffer()
        userAgent = System.getProperty("http.agent")//Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo X9L Build/MMB29M)

        var i = 0
        val length = userAgent!!.length
        while (i < length) {
            val c = userAgent[i]
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", c.toInt()))
            } else {
                sb.append(c)
            }
            i++
        }

        Log.i("User-Agent", "User-Agent: $sb")
        return sb.toString()
    }
}
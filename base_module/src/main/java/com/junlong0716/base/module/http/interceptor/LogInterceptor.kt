package com.junlong0716.base.module.http.interceptor

import android.util.Log

import java.io.IOException

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/8/2 下午4:45
 *@modified by:
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        Log.d(TAG, "\n")
        Log.e(TAG, "----------------Start----------------")
        Log.d(TAG, "| " + request.toString())
        val method = request.method()
        if ("POST" == method) {
            val sb = StringBuilder()
            if (request.body() is FormBody) {
                val body = request.body() as FormBody?
                for (i in 0 until body!!.size()) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",")
                }
                sb.delete(sb.length - 1, sb.length)
                Log.d(TAG, "| RequestParams:{" + sb.toString() + "}")
            }
        }
        Log.d(TAG, "| Response:$content")
        Log.e(TAG, "----------------End:" + duration + "毫秒----------------")
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build()
    }

    companion object {
        var TAG = "LogInterceptor"
    }
}

package com.future.wanandroid.network.interceptor

import com.future.toolkit.utils.log.LogUtils
import com.future.wanandroid.network.ApiTool
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.*

class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        try {
            val request = chain.request()
            LogUtils.v(ApiTool.TAG, "request:$request")
            LogUtils.v(ApiTool.TAG, "headers: " + request.headers().toString())
            val t1 = System.nanoTime()
            response = chain.proceed(request)
            val t2 = System.nanoTime()
            LogUtils.v(
                ApiTool.TAG, String.format(
                    Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6, response.headers()
                )
            )
            val body = response.body()
            val mediaType = body!!.contentType()
            LogUtils.i(ApiTool.TAG, "contentLength:" + body.contentLength())
            val content = body.string()
            LogUtils.i(ApiTool.TAG, "response body:$content")
            return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response!!
    }
}
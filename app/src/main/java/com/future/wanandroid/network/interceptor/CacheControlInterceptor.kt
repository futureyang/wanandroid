package com.future.wanandroid.network.interceptor

import android.content.Context
import android.util.Log
import com.future.toolkit.utils.NetUtils
import com.future.wanandroid.network.ApiTool
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheControlInterceptor(val context: Context) : Interceptor {

    companion object {
        //设缓存有效期为1天
        val CACHE_STALE_SEC = 60 * 60 * 24.toLong()
        //查询缓存的Cache-Control设置，为only-if-cached时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
        val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetUtils.isConnected(context)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val originalResponse = chain.proceed(request)
        return if (NetUtils.isConnected(context)) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            val cacheControl = request.cacheControl().toString()
            originalResponse.newBuilder()
                .header(
                    "Cache-Control",
                    cacheControl
                )
                .addHeader("token", "")
                .removeHeader("Pragma")
                .build()
        } else {
            Log.e(ApiTool.TAG, "无网络设置_Interceptor")
            originalResponse.newBuilder()
                .header("Cache-Control", CACHE_CONTROL_CACHE)
                .removeHeader("Pragma")
                .build()
        }
    }
}
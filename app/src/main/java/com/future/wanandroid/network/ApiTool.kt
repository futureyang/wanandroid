package com.future.wanandroid.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.future.toolkit.utils.log.MLog
import com.future.wanandroid.MyApplication
import com.future.wanandroid.network.interceptor.CacheControlInterceptor
import com.future.wanandroid.network.interceptor.LogInterceptor
import com.future.wanandroid.network.service.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object ApiTool {

    const val TAG = "OKHTTP"

    fun clearCookie() = cookieJar.clear()

    private val cookieJar = PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(MyApplication.context)
    )

    private val builder = Retrofit.Builder()
        .baseUrl(HttpConst.API_HOST_URL)
        .client(genericClient(MyApplication.context))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private fun genericClient(context: Context) = OkHttpClient.Builder().run {
        connectTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
        writeTimeout(15, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        cookieJar(cookieJar)
        cache(Cache(File(context.cacheDir, "http"), 1024 * 1024 * 100))
        addInterceptor(CacheControlInterceptor(context))
        if (MLog.getDebugStatus()) {
            addInterceptor(LogInterceptor())
        }
        addNetworkInterceptor(CacheControlInterceptor(context))
        build()
    }

    val apiService: ApiService = builder.build().create(ApiService::class.java)
}
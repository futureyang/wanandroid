package com.future.wanandroid

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.future.wanandroid.common.ActivityLifecycleCallbacksAdapter
import com.future.wanandroid.ui.ActivityManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        init()
    }

    fun init() {
        rigesterActivityCallbacks()
    }

    private fun rigesterActivityCallbacks() {
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                ActivityManager.activities.add(activity)
            },
            onActivityDestroyed = { activity ->
                ActivityManager.activities.remove(activity)
            }
        ))
    }
}
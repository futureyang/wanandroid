package com.future.wanandroid.ui

import android.app.Activity
import android.content.Intent
import com.future.wanandroid.ext.putExtras

object ActivityManager {

    val activities = mutableListOf<Activity>()

    fun start(clazz: Class<out Activity>, params: Map<String, Any> = emptyMap()) {
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
    }

    fun finish(vararg clazz: Class<out Activity>) {
        activities.forEach { activiy ->
            if (clazz.contains(activiy::class.java)) {
                activiy.finish()
            }
        }
    }

    fun exit() {
        activities.forEach { it.finish() }
    }
}

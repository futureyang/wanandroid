package com.future.wanandroid.network

object HttpConst {

    //测试
    const val IS_DEV_API = false

    const val API_HOST = "https://www.wanandroid.com"

    const val API_HOST_DEV = "https://www.wanandroid.com"

    // 正式服
    val API_HOST_URL = if (IS_DEV_API) API_HOST_DEV else API_HOST

}
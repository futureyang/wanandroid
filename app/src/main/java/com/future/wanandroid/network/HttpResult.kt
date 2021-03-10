package com.future.wanandroid.network

class HttpResult<T>(val state: Int, val message: String, private val data: T) {
    fun await(): T {
        if (state == 0) {
            return data
        } else {
            throw HttpException(state, message)
        }
    }
}
package com.future.wanandroid.network

/**
 *
 */
data class ApiResult<T>(val errorCode: Int, val errorMsg: String, private val data: T) {
    fun apiData(): T {
        if (errorCode == 0) {
            return data
        } else {
            throw HttpException(errorCode, errorMsg)
        }
    }
}

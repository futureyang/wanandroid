package com.future.wanandroid.network

class HttpException(var state: Int, override var message: String) : RuntimeException()
package com.future.mvvmk.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.future.toolkit.utils.ToastUtils
import com.future.toolkit.utils.log.LogUtils
import com.future.wanandroid.MyApplication
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ext.showToast
import com.future.wanandroid.network.HttpException
import com.future.wanandroid.ui.login.UserRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit

abstract class BaseViewModel : ViewModel() {

    protected val userRepository by lazy { UserRepository() }

    val loginStatusInvalid: MutableLiveData<Boolean> = MutableLiveData()

    fun loginStatus() = userRepository.isLogin()

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(block: Block<Unit>, error: Error? = null, cancel: Cancel? = null): Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            } catch (e: Exception) {
                e.message?.let { LogUtils.e("onError()", it) }
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 一般网络请求使用launch方法
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke() }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     */
    private fun onError(e: Exception) {
        when (e) {
            is HttpException -> {
                when (e.state) {
                    -1001 -> {
                        // 登录失效
                        userRepository.clearLoginState()
                        LiveBus.post(USER_LOGIN_STATE_CHANGED, false)
                        loginStatusInvalid.value = true
                    }
                    -1 -> {
                        // 其他api错误
                        MyApplication.context.showToast(e.message)
                    }
                    else -> {
                        // 其他错误
                        MyApplication.context.showToast(e.message)
                    }
                }
            }
            is ConnectException -> {
                // 连接失败
                MyApplication.context.showToast(MyApplication.context.getString(R.string.network_connection_failed))
            }
            is SocketTimeoutException -> {
                // 请求超时
                MyApplication.context.showToast(MyApplication.context.getString(R.string.network_request_timeout))
            }
            is JsonParseException -> {
                // 数据解析错误
                MyApplication.context.showToast(MyApplication.context.getString(R.string.api_data_parse_error))
            }
            else -> {
                // 其他错误
                e.message?.let { MyApplication.context.showToast(it) }
            }
        }
    }

}
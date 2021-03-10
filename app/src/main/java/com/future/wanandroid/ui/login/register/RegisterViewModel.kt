package com.future.wanandroid.ui.login.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.login.login.LoginRepository

/**
 * Created by yangqc on 2020/12/18
 *
 */
class RegisterViewModel @ViewModelInject constructor(val repository: RegisterRepository) : BaseViewModel() {
    val submitting = MutableLiveData<Boolean>()
    val registerResult = MutableLiveData<Boolean>()

    fun register(account: String, password: String, confirmPassword: String) {
        submitting.value = true
        launch(
            block = {
                val userInfo = repository.register(account, password, confirmPassword)
                userRepository.updateUserInfo(userInfo)
                LiveBus.post(USER_LOGIN_STATE_CHANGED, true)
                submitting.value = false
                registerResult.value = true
            },
            error = {
                it.message
                submitting.value = false
                registerResult.value = false
            }
        )
    }
}
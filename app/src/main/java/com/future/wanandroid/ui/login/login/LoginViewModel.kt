package com.future.wanandroid.ui.login.login

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
class LoginViewModel @ViewModelInject constructor(val repository: LoginRepository) : BaseViewModel() {
    val submitting = MutableLiveData<Boolean>()
    val loginResult = MutableLiveData<Boolean>()

    fun login(account: String, password: String) {
        submitting.value = true
        launch(
            block = {
                val userInfo = repository.login(account, password)
                userRepository.updateUserInfo(userInfo)
                LiveBus.post(USER_LOGIN_STATE_CHANGED, true)
                submitting.value = false
                loginResult.value = true
            },
            error = {
                it.message
                submitting.value = false
                loginResult.value = false
            }
        )
    }
}
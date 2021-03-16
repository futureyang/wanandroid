package com.future.wanandroid.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED

class SettingsViewModel@ViewModelInject constructor(): BaseViewModel() {

    val isLogin = MutableLiveData<Boolean>()

    fun getLoginStatus() {
        isLogin.value = userRepository.isLogin()
    }

    fun logout() {
        userRepository.clearLoginState()
        LiveBus.post(USER_LOGIN_STATE_CHANGED, false)
    }
}
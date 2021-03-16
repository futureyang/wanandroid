package com.future.mvvmk.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.login.login.LoginActivity

abstract class BaseVmActivity<VM: BaseViewModel, VB: ViewDataBinding> : BaseActivity<VB>() {

    val mViewModel: VM by lazy { ViewModelProvider(this).get(viewModelClass()) }

    protected abstract fun viewModelClass(): Class<VM>

    /**
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (mViewModel.loginStatus()) {
            then?.invoke()
            true
        } else {
            ActivityManager.start(LoginActivity::class.java)
            false
        }
    }
}
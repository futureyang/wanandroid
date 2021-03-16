package com.future.wanandroid.ui.login.login

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.toolkit.utils.ToastUtils
import com.future.wanandroid.R
import com.future.wanandroid.databinding.ActivityLoginBinding
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.login.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by yangqc on 2020/12/18
 * 登录
 */
@AndroidEntryPoint
class LoginActivity : BaseVmActivity<LoginViewModel, ActivityLoginBinding>() {

    private var isCipher = true //显示密码

    override fun viewModelClass() = LoginViewModel::class.java

    override fun getLayoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {
        layout_login_password.apply {
            realRightView.findViewById<ImageView>(R.id.iv_template_item_right).apply {
                setOnClickListener {
                    isCipher = !isCipher
                    setContentInputCipher(isCipher)
                    setImageResource(if (isCipher) R.drawable.ic_visibility_off else R.drawable.ic_visibility)
                }
            }
        }
    }

    fun login() {
        val account = layout_login_account.contentText
        val password = layout_login_password.contentText
        when {
            account.isEmpty() -> ToastUtils.show(this, R.string.account_can_not_be_empty)
            password.isEmpty() -> ToastUtils.show(this, R.string.password_can_not_be_empty)
            else -> mViewModel.login(account, password)
        }
    }

    fun register() {
        ActivityManager.start(RegisterActivity::class.java)
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            submitting.observe(this@LoginActivity, Observer {
                if (it) showDialog() else dismissDialog()
            })
            loginResult.observe(this@LoginActivity, Observer {
                if (it) finish()
            })
        }
    }
}
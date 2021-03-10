package com.future.wanandroid.ui.login.register

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.toolkit.utils.ToastUtils
import com.future.wanandroid.R
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.login.login.LoginActivity
import com.future.wanandroid.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Created by yangqc on 2020/12/18
 *
 */
@AndroidEntryPoint
class RegisterActivity : BaseVmActivity<RegisterViewModel>() {

    private var isCipher = true

    private var isCipherConfirm = true

    override fun viewModelClass() = RegisterViewModel::class.java

    override fun getLayoutId() = R.layout.activity_register

    override fun initView(savedInstanceState: Bundle?) {
        layout_register_password.apply {
            realRightView.findViewById<ImageView>(R.id.iv_template_item_right).apply {
                setOnClickListener {
                    isCipher = !isCipher
                    setContentInputCipher(isCipher)
                    setImageResource(if (isCipher) R.drawable.ic_visibility_off else R.drawable.ic_visibility)
                }
            }
        }
        layout_register_password_confirm.apply {
            realRightView.findViewById<ImageView>(R.id.iv_template_item_right).apply {
                setOnClickListener {
                    isCipherConfirm = !isCipherConfirm
                    setContentInputCipher(isCipherConfirm)
                    setImageResource(if (isCipherConfirm) R.drawable.ic_visibility_off else R.drawable.ic_visibility)
                }
            }
        }
        btnRegister.setOnClickListener {
            val account = layout_register_account.contentText
            val password = layout_register_password.contentText
            val confirmPassword = layout_register_password_confirm.contentText
            when {
                account.isEmpty() -> ToastUtils.show(this, R.string.account_can_not_be_empty)
                account.length < 3 -> ToastUtils.show(this, R.string.account_length_over_three)
                password.isEmpty() -> ToastUtils.show(this, R.string.password_can_not_be_empty)
                password.length < 6 -> ToastUtils.show(this, R.string.password_length_over_six)
                confirmPassword.isEmpty() -> ToastUtils.show(
                    this,
                    R.string.confirm_password_can_not_be_empty
                )
                confirmPassword != password -> ToastUtils.show(
                    this,
                    R.string.two_password_are_inconsistent
                )
                else -> mViewModel.register(account, password, confirmPassword)
            }
        }
        ivBack.setOnClickListener { finish() }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            submitting.observe(this@RegisterActivity, Observer {
                if (it) showDialog() else dismissDialog()
            })
            registerResult.observe(this@RegisterActivity, Observer {
                if (it) {
                    finish()
                    ActivityManager.finish(LoginActivity::class.java)
                }
            })
        }
    }
}
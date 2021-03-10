package com.future.mvvmk.base

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.future.wanandroid.util.showProgress

abstract class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: Dialog? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView(savedInstanceState)
        observe()
        // 因为Activity恢复后savedInstanceState不为null，
        // 重新恢复后会自动从ViewModel中的LiveData恢复数据，
        // 不需要重新初始化数据。
        if (savedInstanceState == null) {
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }

    open fun initView(savedInstanceState: Bundle?) {}

    open fun initData() {}

    open fun observe() {}

    open fun showDialog() {
        dismissDialog()
        mProgressDialog = showProgress(this)
    }

    open fun dismissDialog() {
        mProgressDialog?.dismiss()
        mProgressDialog = null
    }
}
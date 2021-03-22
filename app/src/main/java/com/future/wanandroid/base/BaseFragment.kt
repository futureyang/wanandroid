package com.future.wanandroid.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.future.wanandroid.util.showProgress

abstract class BaseFragment<VB: ViewDataBinding> : Fragment(){

    lateinit var mBinding: VB

    private var lazyLoaded = false

    private var mProgressDialog: Dialog? = null

    protected var mContext: Context? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
        // 因为Fragment恢复后savedInstanceState不为null，
        // 重新恢复后会自动从ViewModel中的LiveData恢复数据，
        // 不需要重新初始化数据。
        if (savedInstanceState == null) {
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        // 实现懒加载
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
    }

    open fun initView() {}

    open fun initData() {}

    open fun observe() {}

    open fun lazyLoadData() {}

    open fun showDialog() {
        dismissProgress()
        mProgressDialog = mContext?.let { showProgress(it) }
    }

    open fun dismissProgress() {
        mProgressDialog?.dismiss()
        mProgressDialog = null
    }
}
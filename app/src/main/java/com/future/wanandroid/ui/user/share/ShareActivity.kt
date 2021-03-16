package com.future.wanandroid.ui.user.share

import android.os.Bundle
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.databinding.ActivityShareBinding
import com.future.wanandroid.ext.hideSoftInput
import com.future.wanandroid.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_title.*

/**
 * Created by yangqc on 2021/2/3
 * 我的分享
 */
@AndroidEntryPoint
class ShareActivity : BaseVmActivity<ShareViewModel, ActivityShareBinding>() {

    override fun viewModelClass() = ShareViewModel::class.java

    override fun getLayoutId() = R.layout.activity_share

    override fun initView(savedInstanceState: Bundle?) {
        ivBack.setOnClickListener { finish() }
        tvTitle.text = getString(R.string.my_share)
        tvMenu.apply {
            text = getString(R.string.submit)
            setOnClickListener {
                val title = mBinding.acetTitle.text.toString().trim()
                val link = mBinding.acetlink.text.toString().trim()
                if (title.isEmpty()) {
                    showToast(R.string.title_toast)
                    return@setOnClickListener
                }
                if (link.isEmpty()) {
                    showToast(R.string.link_toast)
                    return@setOnClickListener
                }
                tvMenu.hideSoftInput()
                mViewModel.shareArticle(title, link)
            }
        }
    }

    override fun initData() {
        mViewModel.getUserInfo()
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            submitting.observe(this@ShareActivity, Observer {
                if (it) showDialog() else dismissDialog()
            })
            shareResult.observe(this@ShareActivity, Observer {
                if (it) {
                    showToast(R.string.share_article_success)
                }
            })
        }
    }
}
package com.future.wanandroid.ui.settings

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.BuildConfig
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.SeekBarChangeListenerAdapter
import com.future.wanandroid.databinding.ActivitySettingsBinding
import com.future.wanandroid.ext.setNavigationBarColor
import com.future.wanandroid.ext.showToast
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.details.DetailActivity.Companion.PARAM_ARTICLE
import com.future.wanandroid.ui.login.login.LoginActivity
import com.future.wanandroid.util.clearCache
import com.future.wanandroid.util.getCacheSize
import com.future.wanandroid.util.isNightMode
import com.future.wanandroid.util.setNightMode
import com.future.wanandroid.store.SettingsStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.view_change_text_zoom.*

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class SettingsActivity : BaseVmActivity<SettingsViewModel, ActivitySettingsBinding>() {

    override fun getLayoutId() = R.layout.activity_settings

    override fun viewModelClass() = SettingsViewModel::class.java

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.activity = this
        mBinding.viewModel = mViewModel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNavigationBarColor(getColor(R.color.bgColorSecondary))
        }
        ivBack.setOnClickListener { ActivityManager.finish(SettingsActivity::class.java) }
        mBinding.scDayNight.setOnCheckedChangeListener { _, checked ->
            setNightMode(checked)
            SettingsStore.setNightMode(checked)
        }
        mBinding.llClearCache.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_clear_cache)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    clearCache(this)
                    tvClearCache.text = getCacheSize(this)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
        mBinding.llAboutUs.setOnClickListener {
            ActivityManager.start(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.abount_us),
                        link = "https://github.com/futureyang/wanandroid"
                    )
                )
            )
        }
        mBinding.tvLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_logout)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    mViewModel.logout()
                    ActivityManager.start(LoginActivity::class.java)
                    ActivityManager.finish(SettingsActivity::class.java)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
    }

    fun isNightMode() = isNightMode(this)

    fun getCacheSize() = getCacheSize(this)

    fun fontSize() = "${SettingsStore.getWebTextZoom()}%"

    fun aboutUs() = getString(R.string.current_version, BuildConfig.VERSION_NAME)

    fun showToast() = showToast(getString(R.string.already_latest_version))

    @SuppressLint("SetTextI18n")
    fun setFontSize() {
        val textZoom = SettingsStore.getWebTextZoom()
        var tempTextZoom = textZoom
        AlertDialog.Builder(this)
            .setTitle(R.string.font_size)
            .setView(LayoutInflater.from(this).inflate(R.layout.view_change_text_zoom, null).apply {
                seekBar.progress = textZoom - 50
                seekBar.setOnSeekBarChangeListener(SeekBarChangeListenerAdapter(
                    onProgressChanged = { _, progress, _ ->
                        tempTextZoom = 50 + progress
                        tvFontSize.text = "$tempTextZoom%"
                    }
                ))
            })
            .setNegativeButton(R.string.cancel) { _, _ ->
                tvFontSize.text = "$textZoom%"
            }
            .setPositiveButton(R.string.confirm) { _, _ ->
                SettingsStore.setWebTextZoom(tempTextZoom)
            }
            .show()
    }

    override fun initData() {
        mViewModel.getLoginStatus()
    }
}

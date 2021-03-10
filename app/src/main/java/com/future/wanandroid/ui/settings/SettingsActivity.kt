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
import com.future.wanandroid.common.SeekBarChangeListenerAdapter
import com.future.wanandroid.ext.setNavigationBarColor
import com.future.wanandroid.ext.showToast
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.login.login.LoginActivity
import com.future.wanandroid.util.clearCache
import com.future.wanandroid.util.getCacheSize
import com.future.wanandroid.util.isNightMode
import com.future.wanandroid.util.setNightMode
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.view_change_text_zoom.*

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class SettingsActivity : BaseVmActivity<SettingsViewModel>() {

    override fun getLayoutId() = R.layout.activity_settings

    override fun viewModelClass() = SettingsViewModel::class.java

    override fun initView(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNavigationBarColor(getColor(R.color.bgColorSecondary))
        }
        scDayNight.isChecked = isNightMode(this)
        tvFontSize.text = "${SettingsStore.getWebTextZoom()}%"
        tvClearCache.text = getCacheSize(this)
        tvAboutUs.text = getString(R.string.current_version, BuildConfig.VERSION_NAME)

        ivBack.setOnClickListener { ActivityManager.finish(SettingsActivity::class.java) }
        scDayNight.setOnCheckedChangeListener { _, checked ->
            setNightMode(checked)
            SettingsStore.setNightMode(checked)
        }
        llFontSize.setOnClickListener {
            setFontSize()
        }
        llClearCache.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.confirm_clear_cache)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    clearCache(this)
                    tvClearCache.text = getCacheSize(this)

                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
        llCheckVersion.setOnClickListener {
            // TODO 检查版本
            showToast(getString(R.string.already_latest_version))
        }
//        llAboutUs.setOnClickListener {
//            ActivityManager.start(
//                DetailActivity::class.java,
//                mapOf(
//                    PARAM_ARTICLE to Article(
//                        title = getString(R.string.abount_us),
//                        link = "https://github.com/xiaoyanger0825/wanandroid"
//                    )
//                )
//            )
//        }
        tvLogout.setOnClickListener {
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

    @SuppressLint("SetTextI18n")
    private fun setFontSize() {
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

    override fun observe() {
        mViewModel.isLogin.observe(this, Observer {
            tvLogout.isVisible = it
        })
    }
}

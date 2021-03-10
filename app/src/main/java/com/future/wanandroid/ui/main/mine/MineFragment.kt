package com.future.wanandroid.ui.main.mine

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.provider.MediaStore
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.details.DetailActivity.Companion.PARAM_ARTICLE
import com.future.wanandroid.ui.login.login.LoginActivity
import com.future.wanandroid.ui.settings.SettingsActivity
import com.future.wanandroid.ui.user.collection.CollectionActivity
import com.future.wanandroid.ui.user.history.HistoryActivity
import com.future.wanandroid.ui.user.integral.IntegralActivity
import com.future.wanandroid.ui.user.opensource.OpenSourceActivity
import com.future.wanandroid.ui.user.ranking.RankingActivity
import com.future.wanandroid.ui.user.share.list.ShareListActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseVmFragment<MineViewModle>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun viewModelClass() = MineViewModle::class.java

    override fun getLayoutId() = R.layout.fragment_mine

    override fun initView() {
        llMyPoints.setOnClickListener {
            checkLogin { ActivityManager.start(IntegralActivity::class.java) }
        }
        llRanking.setOnClickListener {
            ActivityManager.start(RankingActivity::class.java)
        }
        llMyShare.setOnClickListener {
            checkLogin { ActivityManager.start(ShareListActivity::class.java) }
        }
        llMyCollect.setOnClickListener {
            checkLogin { ActivityManager.start(CollectionActivity::class.java) }
        }
        llHistory.setOnClickListener {
            ActivityManager.start(HistoryActivity::class.java)
        }
        llOpenSource.setOnClickListener {
            ActivityManager.start(OpenSourceActivity::class.java)
        }
        llAboutAuthor.setOnClickListener {
            ActivityManager.start(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.my_about_author),
                        link = "https://github.com/futureyang"
                    )
                )
            )
        }
        llSetting.setOnClickListener {
            ActivityManager.start(SettingsActivity::class.java)
        }
        ll_to_login.setOnClickListener {
            ActivityManager.start(LoginActivity::class.java)
        }
        iv_user_photo.setOnClickListener {
//            val cursor = context!!.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
//                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                    iv_user_photo.setImageURI(uri)
//
//                    println("image uri is $uri")
//                }
//                cursor.close()
//            }
            image()
        }

    }

    override fun initData() {
        mViewModel.getUserInfo()
    }

    override fun observe() {
        mViewModel.run {
            isLogin.observe(viewLifecycleOwner, Observer {
                tv_login_registered.isGone = it
                tv_user_name.isVisible = it
                ll_to_login.isClickable = !it
            })
            userInfo.observe(viewLifecycleOwner, Observer {
                it?.let {
                    tv_user_name.text = it.nickname
                }
            })
        }
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.getUserInfo()
        }
    }

    fun image() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "video/*"
        startActivityForResult(intent, 100)
    }
}
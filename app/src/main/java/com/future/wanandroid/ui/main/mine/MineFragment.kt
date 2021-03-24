package com.future.wanandroid.ui.main.mine

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.future.mvvmk.base.BaseVmFragment
import com.future.toolkit.utils.log.LogUtils
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.databinding.FragmentMineBinding
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
import com.future.wanandroid.util.GlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.engine.PictureSelectorEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_mine.*

@AndroidEntryPoint
class MineFragment : BaseVmFragment<MineViewModle, FragmentMineBinding>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun viewModelClass() = MineViewModle::class.java

    override fun getLayoutId() = R.layout.fragment_mine

    override fun initView() {
        mBinding.llMyPoints.setOnClickListener {
            checkLogin { ActivityManager.start(IntegralActivity::class.java) }
        }
        mBinding.llRanking.setOnClickListener {
            ActivityManager.start(RankingActivity::class.java)
        }
        mBinding.llMyShare.setOnClickListener {
            checkLogin { ActivityManager.start(ShareListActivity::class.java) }
        }
        mBinding.llMyCollect.setOnClickListener {
            checkLogin { ActivityManager.start(CollectionActivity::class.java) }
        }
        mBinding.llHistory.setOnClickListener {
            ActivityManager.start(HistoryActivity::class.java)
        }
        mBinding.llOpenSource.setOnClickListener {
            ActivityManager.start(OpenSourceActivity::class.java)
        }
        mBinding.llAboutAuthor.setOnClickListener {
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
        mBinding.llSetting.setOnClickListener {
            ActivityManager.start(SettingsActivity::class.java)
        }
        mBinding.llToLogin.setOnClickListener {
            ActivityManager.start(LoginActivity::class.java)
        }
        mBinding.ivPhoto.setOnClickListener {
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
            checkLogin { setPhoto() }
        }

    }

    override fun initData() {
        mViewModel.getUserInfo()
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
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
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    fun setPhoto() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.instance)
            .maxSelectNum(1)
            .isCamera(true)
            .isEnableCrop(true)
            .freeStyleCropEnabled(true)
            .scaleEnabled(true)
            .isCompress(true)
            .withAspectRatio(1, 1)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val localPicPath: String
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    if (!selectList.isEmpty()) {
                        val media = selectList[0]
                        if (media.isCut || media.isCompressed) {
                            localPicPath = media.compressPath
                        } else {
                            localPicPath = media.path
                        }
                        mViewModel.saveUserPhoto(localPicPath)

//                        LogUtils.d("ImageUrl", localPicPath)
//                        val file = context?.getExternalFilesDir(null)
//                        LogUtils.d("ImageUrl", file?.path)
                    }
                }
            }
        }
    }
}
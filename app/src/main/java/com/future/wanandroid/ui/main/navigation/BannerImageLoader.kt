package com.future.wanandroid.ui.main.navigation

import android.content.Context
import android.widget.ImageView
import com.future.wanandroid.R
import com.future.wanandroid.util.ImageOptions
import com.future.wanandroid.util.loadImage
import com.xiaojianjun.wanandroid.model.bean.Banner

import com.youth.banner.loader.ImageLoader


class BannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        val imagePath = (path as? Banner)?.imagePath
        imageView?.loadImage(imagePath, ImageOptions().apply {
            placeholder = R.drawable.shape_bg_image_default
        })
    }

}
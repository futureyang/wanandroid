package com.future.wanandroid.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

/**
 * Created by yangqc on 2021/3/24
 * BindingAdapter
 */
@BindingAdapter("bind:loadBingUrl", "bind:errorDrawable")
fun ImageView.loadBingUrl(url: String?, errorDrawable: Drawable) {
    if (url != null) Glide.with(context).load(url).error(errorDrawable).into(this)
}

@BindingAdapter("bind:loadBingUri", "bind:errorDrawable", requireAll = false)
fun ImageView.loadBingUri(uri: String?, errorDrawable: Drawable) {
    if (uri.isNullOrEmpty()) {
        setImageDrawable(errorDrawable)
    } else {
        setImageURI(Uri.parse(uri))
    }
}

@BindingAdapter("bind:colorSchemeResources")
fun SwipeRefreshLayout.colorSchemeResources(resId: Int) {
    setColorSchemeResources(resId)
}

@BindingAdapter("bind:selected")
fun ImageView.selected(isSelected: Boolean) {
    setSelected(isSelected)
}
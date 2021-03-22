package com.future.wanandroid.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by yangqc on 2021/3/22
 *
 */
class BaseDBViewHolder(view: View) : BaseViewHolder(view) {

    private var binding: ViewDataBinding? = null

    init {
        binding = DataBindingUtil.bind(itemView)
    }

    fun getBinding(): ViewDataBinding? {
        return binding
    }
}
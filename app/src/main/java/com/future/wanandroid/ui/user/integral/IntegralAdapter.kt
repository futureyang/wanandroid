package com.future.wanandroid.ui.user.integral

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.bean.PointRecord
import com.future.wanandroid.ext.toDateTime
import kotlinx.android.synthetic.main.item_integral.view.*

@SuppressLint("SetTextI18n")
class IntegralAdapter : BaseQuickAdapter<PointRecord, BaseViewHolder>(R.layout.item_integral) {
    override fun convert(helper: BaseViewHolder, item: PointRecord) {
        helper.itemView.run {
            tvReason.text = item.reason
            tvTime.text = item.date.toDateTime("yyyy-MM-dd HH:mm:ss")
            tvPoint.text = "+${item.coinCount}"
        }
    }

}
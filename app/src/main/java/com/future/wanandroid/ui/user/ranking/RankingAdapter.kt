package com.future.wanandroid.ui.user.ranking

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.bean.PointRank
import kotlinx.android.synthetic.main.item_ranking.view.*

@SuppressLint("SetTextI18n")
class RankingAdapter : BaseQuickAdapter<PointRank, BaseViewHolder>(R.layout.item_ranking) {
    override fun convert(helper: BaseViewHolder, item: PointRank) {
        helper.itemView.run {
            tvNo.text = "${helper.adapterPosition + 1}"
            tvName.text = item.username
            tvPoints.text = item.coinCount.toString()
        }
    }
}
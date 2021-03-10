package com.future.wanandroid.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Navigation
import kotlinx.android.synthetic.main.item_navigation.view.*


class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    var onItemTagClickListener: ((article: Article) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: Navigation) {
        helper.itemView.run {
            title.text = item.name
            tagFlawLayout.adapter = ItemTagAdapter(item.articles)
            tagFlawLayout.setOnTagClickListener { _, position, _ ->
                onItemTagClickListener?.invoke(item.articles[position])
                true
            }
        }
    }
}
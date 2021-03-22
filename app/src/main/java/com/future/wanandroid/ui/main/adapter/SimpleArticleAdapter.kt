package com.future.wanandroid.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.base.BaseDBViewHolder
import com.future.wanandroid.bean.Article
import com.future.wanandroid.databinding.ItemArticleSimpleBinding
import com.future.wanandroid.ext.htmlToSpanned
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_article.view.iv_collect
import kotlinx.android.synthetic.main.item_article.view.tv_author
import kotlinx.android.synthetic.main.item_article.view.tv_fresh
import kotlinx.android.synthetic.main.item_article.view.tv_time
import kotlinx.android.synthetic.main.item_article.view.tv_title
import kotlinx.android.synthetic.main.item_article_simple.view.*

/**
 * Created by yangqc on 2020/12/14
 *
 */
class SimpleArticleAdapter(layoutResId: Int = R.layout.item_article_simple) :
    BaseQuickAdapter<Article, BaseDBViewHolder>(layoutResId) {

    override fun convert(helper: BaseDBViewHolder, item: Article) {
        val binding = helper.getBinding() as ItemArticleSimpleBinding
        binding.apply {
            tvAuthor.text = when {
                !item.author.isNullOrEmpty() -> {
                    item.author
                }
                !item.shareUser.isNullOrEmpty() -> {
                    item.shareUser
                }
                else -> mContext.getString(R.string.anonymous)
            }
            tvTitle.text = item.title.htmlToSpanned()
        }
        helper.addOnClickListener(R.id.iv_collect)
    }
}
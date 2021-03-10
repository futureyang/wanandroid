package com.future.wanandroid.ui.main.adapter

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.ext.htmlToSpanned
import kotlinx.android.synthetic.main.item_article.view.*

/**
 * Created by yangqc on 2020/12/14
 *
 */
class ArticleAdapter(layoutResId: Int = R.layout.item_article) :
    BaseQuickAdapter<Article, BaseViewHolder>(layoutResId){

    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.apply {
            itemView.apply {
                tv_author.text = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_top.isVisible = item.top
                tv_fresh.isVisible = item.fresh
                tv_tag.visibility = if (item.tags.isNotEmpty()) {
                    tv_tag.text = item.tags[0].name
                    View.VISIBLE
                } else {
                    View.GONE
                }
                tv_chapter.text = when {
                    item.superChapterName.isNullOrEmpty() ->
                        item.chapterName.htmlToSpanned()
                    item.chapterName.isNullOrEmpty() ->
                        item.superChapterName.htmlToSpanned()
                    else ->
                        "${item.superChapterName.htmlToSpanned()}/${item.chapterName.htmlToSpanned()}"
                }
                tv_title.text = item.title.htmlToSpanned()
                tv_desc.text = item.desc.htmlToSpanned()
                tv_desc.isGone = item.desc.isNullOrEmpty()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
            addOnClickListener(R.id.iv_collect)
        }
    }
}
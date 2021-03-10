package com.xiaojianjun.wanandroid.model.bean

import com.future.wanandroid.bean.Article

data class Navigation(
    val cid: Int,
    val name: String,
    val articles: List<Article>
)
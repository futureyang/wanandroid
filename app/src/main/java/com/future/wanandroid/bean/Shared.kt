package com.future.wanandroid.bean

data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>
)
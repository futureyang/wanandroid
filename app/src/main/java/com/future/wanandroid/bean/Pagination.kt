package com.future.wanandroid.bean

/**
 * Created by yangqc on 2020/12/14
 * 分页数据模板
 */
data class Pagination<T>(
        val offset: Int,
        val size: Int,
        val total: Int,
        val pageCount: Int,
        val curPage: Int,
        val over: Boolean,
        val datas: List<T>
)
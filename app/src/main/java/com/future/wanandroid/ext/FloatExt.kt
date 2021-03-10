package com.future.wanandroid.ext

import com.future.wanandroid.MyApplication
import com.future.wanandroid.util.dpToPx
import com.future.wanandroid.util.pxToDp


fun Float.toPx() = dpToPx(MyApplication.context, this)

fun Float.toIntPx() = dpToPx(MyApplication.context, this).toInt()

fun Float.toDp() = pxToDp(MyApplication.context, this)

fun Float.toIntDp() = pxToDp(MyApplication.context, this).toInt()
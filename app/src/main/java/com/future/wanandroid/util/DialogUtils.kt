package com.future.wanandroid.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.future.wanandroid.R

/**
 * Created by yangqc on 2020/12/22
 *
 */
@SuppressLint("InflateParams")
fun showProgress(context: Context): Dialog {
    val view: View = LayoutInflater.from(context).inflate(
        R.layout.layout_dialog_progress,
        null
    )
    return Dialog(context, R.style.Dialog_Progress).apply {
        setContentView(view)
        setCancelable(false)
        show()
    }
}
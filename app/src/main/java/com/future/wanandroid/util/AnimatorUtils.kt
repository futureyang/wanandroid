package com.future.wanandroid.util

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView

/**
 * Created by yangqc on 2021/2/3
 *
 */

fun doIntAnim(target: TextView, to: Int, duration: Long): Unit {
    val fromStr = target.text.toString()
    val from: Int
    from = try {
        fromStr.toInt()
    } catch (e: NumberFormatException) {
        0
    }
    doIntAnim(target, from, to, duration)
}

fun doIntAnim(target: TextView?, from: Int, to: Int, duration: Long) {
    val animator = ValueAnimator.ofInt(from, to)
    animator.addUpdateListener { animation: ValueAnimator ->
        val value = animation.animatedValue as Int
        if (target != null) {
            target.text = String.format("%d", value)
        }
    }
    animator.duration = duration
    animator.interpolator = DecelerateInterpolator()
    animator.start()
}
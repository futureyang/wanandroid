package com.future.wanandroid.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.future.wanandroid.R

/**
 * Created by yangqc on 2021/1/22
 *
 */
class ViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_)
        val animLogoView: AnimLogoView = findViewById(R.id.anim_logo)
        animLogoView.addOffsetAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                Log.d("AnimLogoView", "Offset anim end")
            }
        })
        animLogoView.addGradientAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                Log.d("AnimLogoView", "Gradient anim end")
            }
        })
        animLogoView.startAnimation()
    }
}
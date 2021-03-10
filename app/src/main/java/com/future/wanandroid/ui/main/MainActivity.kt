package com.future.wanandroid.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.fragment.app.Fragment
import com.future.mvvmk.base.BaseActivity
import com.future.wanandroid.R
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.ext.showToast
import com.future.wanandroid.ui.main.hierarchy.HierarchyFragment
import com.future.wanandroid.ui.main.home.HomeFragment
import com.future.wanandroid.ui.main.mine.MineFragment
import com.future.wanandroid.ui.main.navigation.NavigationFragment
import com.google.android.material.animation.AnimationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var previousTimeMillis = 0L

    private var bottomNavigationViewAnimtor: ViewPropertyAnimator? = null

    private var currentBottomNavagtionState = true

    private lateinit var fragments: Map<Int, Fragment>

    override fun getLayoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        fragments = mapOf(
            R.id.home to createFragment(HomeFragment::class.java),
            R.id.system to createFragment(HierarchyFragment::class.java),
            R.id.navigation to createFragment(NavigationFragment::class.java),
            R.id.mine to createFragment(MineFragment::class.java)
        )

        bottomNavigationView.run {
            setOnNavigationItemSelectedListener { menuItem ->
                showFragment(menuItem.itemId)
                true
            }
            setOnNavigationItemReselectedListener { menuItem ->
                val fragment = fragments.entries.find { it.key == menuItem.itemId }?.value
                if (fragment is ScrollToTop) {
                    fragment.scrollToTop()
                }
            }
        }

        if (savedInstanceState == null) {
            val initialItemId = R.id.home
            bottomNavigationView.selectedItemId = initialItemId
            showFragment(initialItemId)
        }
    }

    private fun createFragment(clazz: Class<out Fragment>): Fragment {
        var fragment = supportFragmentManager.fragments.find { it.javaClass == clazz }
        if (fragment == null) {
            fragment = when (clazz) {
                HomeFragment::class.java -> HomeFragment.newInstance()
                HierarchyFragment::class.java -> HierarchyFragment.newInstance()
                NavigationFragment::class.java -> NavigationFragment.newInstance()
                MineFragment::class.java -> MineFragment.newInstance()
                else -> throw IllegalArgumentException("argument ${clazz.simpleName} is illegal")
            }
        }
        return fragment
    }

    private fun showFragment(menuItemId: Int) {
        val currentFragment = supportFragmentManager.fragments.find {
            it.isVisible && it in fragments.values
        }
        val targetFragment = fragments.entries.find { it.key == menuItemId }?.value
        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { if (it.isVisible) hide(it) }
            targetFragment?.let {
                if (it.isAdded) show(it) else add(R.id.container, it)
            }
        }.commit()
    }

    fun animateBottomNavigationView(show: Boolean) {
        if (currentBottomNavagtionState == show) {
            return
        }
        if (bottomNavigationViewAnimtor != null) {
            bottomNavigationViewAnimtor?.cancel()
            bottomNavigationView.clearAnimation()
        }
        currentBottomNavagtionState = show
        val targetY = if (show) 0F else bottomNavigationView.measuredHeight.toFloat()
        val duration = if (show) 225L else 175L
        bottomNavigationViewAnimtor = bottomNavigationView.animate()
            .translationY(targetY)
            .setDuration(duration)
            .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    bottomNavigationViewAnimtor = null
                }
            })
    }

    override fun onBackPressed() {
        val currentTimMillis = System.currentTimeMillis()
        if (currentTimMillis - previousTimeMillis < 2000) {
            super.onBackPressed()
        } else {
            showToast(R.string.press_again_to_exit)
            previousTimeMillis = currentTimMillis
        }
    }

    override fun onDestroy() {
        bottomNavigationViewAnimtor?.cancel()
        bottomNavigationView.clearAnimation()
        bottomNavigationViewAnimtor = null
        super.onDestroy()
    }
}
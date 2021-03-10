package com.future.wanandroid.ui.main.home

import androidx.fragment.app.Fragment
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.base.BaseFragment
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.SimpleFragmentPagerAdapter
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.main.MainActivity
import com.future.wanandroid.ui.main.home.latest.LatestFragment
import com.future.wanandroid.ui.main.home.plaza.PlazaFragment
import com.future.wanandroid.ui.main.home.popular.PopularFragment
import com.future.wanandroid.ui.main.home.project.ProjectFragment
import com.future.wanandroid.ui.main.home.questions.QuestionsFragment
import com.future.wanandroid.ui.main.home.wechat.WechatFragment
import com.future.wanandroid.ui.search.SearchActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), ScrollToTop {

    private lateinit var fragments: List<Fragment>

    private var currentOffset = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun initView() {
        val titles = listOf<CharSequence>(
            getString(R.string.popular_articles),
            getString(R.string.latest_project),
            getString(R.string.plaza),
            getString(R.string.questions),
            getString(R.string.project_category),
            getString(R.string.wechat_public)
        )
        fragments = listOf(
            PopularFragment.newInstance(),
            LatestFragment.newInstance(),
            PlazaFragment.newInstance(),
            QuestionsFragment.newInstance(),
            ProjectFragment.newInstance(),
            WechatFragment.newInstance()
        )
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
        viewPager.offscreenPageLimit = fragments.size
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
        toolbar.setOnClickListener {
            ActivityManager.start(SearchActivity::class.java)
        }
    }

    override fun scrollToTop() {
        if (!this::fragments.isInitialized) return
        val currentFragment = fragments[viewPager.currentItem]
        if (currentFragment is ScrollToTop && currentFragment.isVisible) {
            currentFragment.scrollToTop()
        }
    }
}


package com.future.wanandroid.ui.main.navigation

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.details.DetailActivity.Companion.PARAM_ARTICLE
import com.future.wanandroid.ui.main.MainActivity
import com.future.wanandroid.ui.main.adapter.NavigationAdapter
import com.xiaojianjun.wanandroid.model.bean.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_navigation.reloadView
import kotlinx.android.synthetic.main.include_reload.*

@AndroidEntryPoint
class NavigationFragment : BaseVmFragment<NavigationViewModle>(), ScrollToTop {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private var currentPosition = 0

    private lateinit var mAdapter: NavigationAdapter

    override fun viewModelClass() = NavigationViewModle::class.java

    override fun getLayoutId() = R.layout.fragment_navigation

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.getData() }
        }
        mAdapter = NavigationAdapter(R.layout.item_navigation).apply {
            bindToRecyclerView(recyclerView)
            onItemTagClickListener = {
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(PARAM_ARTICLE to Article(title = it.title, link = it.link))
                )
            }
        }
        btnReload.setOnClickListener {
            mViewModel.getData()
        }
        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
            val lm = recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                tvFloatTitle.y = if (nextView.top < tvFloatTitle.measuredHeight) {
                    (nextView.top - tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        }
    }

    override fun observe() {
        mViewModel.run {
            banners.observe(viewLifecycleOwner, Observer {
                setupBanner(it)
            })
            navigations.observe(viewLifecycleOwner, Observer {
                tvFloatTitle.isGone = it.isEmpty()
                tvFloatTitle.text = it[0].name
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                reloadView.isVisible = it
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getData()
    }

    private fun setupBanner(banners: List<Banner>) {
        bannerView.run {
            setBannerStyle(BannerConfig.NOT_INDICATOR)
            setImageLoader(BannerImageLoader())
            setImages(banners)
            setBannerAnimation(Transformer.BackgroundToForeground)
            start()
            setOnBannerListener {
                val banner = banners[it]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(PARAM_ARTICLE to Article(title = banner.title, link = banner.url))
                )
            }
        }
    }

    override fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }
}
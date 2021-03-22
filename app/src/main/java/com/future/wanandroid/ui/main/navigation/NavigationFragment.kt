package com.future.wanandroid.ui.main.navigation

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.databinding.FragmentNavigationBinding
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.details.DetailActivity.Companion.PARAM_ARTICLE
import com.future.wanandroid.ui.main.MainActivity
import com.future.wanandroid.ui.main.adapter.NavigationAdapter
import com.xiaojianjun.wanandroid.model.bean.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*

@AndroidEntryPoint
class NavigationFragment : BaseVmFragment<NavigationViewModle, FragmentNavigationBinding>(), ScrollToTop {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private var currentPosition = 0

    private lateinit var mAdapter: NavigationAdapter

    override fun viewModelClass() = NavigationViewModle::class.java

    override fun getLayoutId() = R.layout.fragment_navigation

    override fun initView() {
        mBinding.resId = R.color.textColorPrimary
        mAdapter = NavigationAdapter(R.layout.item_navigation).apply {
            bindToRecyclerView(mBinding.recyclerView)
            onItemTagClickListener = {
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(PARAM_ARTICLE to Article(title = it.title, link = it.link))
                )
            }
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getData()
        }
        mBinding.recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                mBinding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
            val lm = mBinding.recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                mBinding.tvFloatTitle.y = if (nextView.top < mBinding.tvFloatTitle.measuredHeight) {
                    (nextView.top - mBinding.tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                mBinding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        }
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            banners.observe(viewLifecycleOwner, Observer {
                setupBanner(it)
            })
            navigations.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })
        }
    }

    override fun lazyLoadData() {
        mViewModel.getData()
    }

    private fun setupBanner(banners: List<Banner>) {
        mBinding.bannerView.run {
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
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}
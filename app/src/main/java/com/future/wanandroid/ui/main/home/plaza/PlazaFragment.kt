package com.future.wanandroid.ui.main.home.plaza

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.SimpleArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.databinding.FragmentPlazaBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * 广场
 */
@AndroidEntryPoint
class PlazaFragment : BaseVmFragment<PlazaViewModel, FragmentPlazaBinding>(), ScrollToTop {

    companion object {
        fun newInstance() = PlazaFragment()
    }

    private var position = 0

    private lateinit var mAdapter: SimpleArticleAdapter

    override fun viewModelClass() = PlazaViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_plaza

    override fun initView() {
        mBinding.resId = R.color.textColorPrimary
        mAdapter = SimpleArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMore()
            }, mBinding.recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = mAdapter.data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, i ->
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    position = i
                    val article = mAdapter.data[i]
                    mViewModel.collect(article.id, article.collect)
                }
            }
        }
        (mBinding.recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refresh()
        }
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
        }
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateListCollectState()
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun lazyLoadData() {
        mViewModel.refresh()
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}
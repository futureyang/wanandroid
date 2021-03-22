package com.future.wanandroid.ui.main.home.project

import androidx.core.view.isGone
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.ArticleAdapter
import com.future.wanandroid.ui.main.adapter.CategoryAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.databinding.FragmentProjectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * 项目
 */
@AndroidEntryPoint
class ProjectFragment : BaseVmFragment<ProjectViewModel, FragmentProjectBinding>(), ScrollToTop {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    private var position = 0

    private lateinit var mAdapter: ArticleAdapter

    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun viewModelClass() = ProjectViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_project

    override fun initView() {
        mBinding.resId = R.color.textColorPrimary
        mCategoryAdapter = CategoryAdapter().apply {
            bindToRecyclerView(mBinding.rvCategory)
            onCheckedListener = {
                mViewModel.refresh(it)
            }
        }
        mAdapter = ArticleAdapter(R.layout.item_article).apply {
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
        mBinding.reloadListView.btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getProjectCategory()
        }
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            categories.observe(viewLifecycleOwner, Observer {
                mBinding.rvCategory.isGone = it.isEmpty()
                mCategoryAdapter.setNewData(it)
            })
            checkedCategory.observe(viewLifecycleOwner, Observer {
                mCategoryAdapter.check(it)
            })
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
        mViewModel.getProjectCategory()
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}
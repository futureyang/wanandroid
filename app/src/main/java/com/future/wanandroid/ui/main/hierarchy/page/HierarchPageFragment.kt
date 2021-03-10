package com.future.wanandroid.ui.main.hierarchy.page

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.bean.Category
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ext.toIntPx
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.CategoryAdapter
import com.future.wanandroid.ui.main.adapter.SimpleArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*
import kotlinx.android.synthetic.main.layout_common_recyclerview_double.*

/**
 * Created by yangqc on 2020/12/31
 *
 */
@AndroidEntryPoint
class HierarchPageFragment : BaseVmFragment<HierarchPageViewModle>(), ScrollToTop {

    companion object {
        private const val CATEGORY_LIST = "CATEGORY_LIST"
        fun newInstance(categoryList: ArrayList<Category>): HierarchPageFragment {
            return HierarchPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    var checkedPosition = 0

    private lateinit var categoryList: List<Category>

    private lateinit var mAdapter: SimpleArticleAdapter

    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun viewModelClass() = HierarchPageViewModle::class.java

    override fun getLayoutId() = R.layout.layout_common_recyclerview_double

    override fun initView() {
        swipe_refresh.apply {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshArticleList(categoryList[checkedPosition].id) }
        }

        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0

        mCategoryAdapter = CategoryAdapter().apply {
            bindToRecyclerView(rvCategory)
            setNewData(categoryList)
            onCheckedListener = {
                checkedPosition = it
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }
        mAdapter = SimpleArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMoreArticleList(categoryList[checkedPosition].id)
            }, recyclerView)
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
                    val article = mAdapter.data[i]
                    mViewModel.collect(article.id, article.collect)
                }
            }
        }
        reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }

    override fun observe() {
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                swipe_refresh.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                reloadListView.isVisible = it
            })
        }
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun lazyLoadData() {
        mViewModel.refreshArticleList(categoryList[checkedPosition].id)
    }

    override fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    fun check(position: Int) {
        if (position != checkedPosition) {
            checkedPosition = position
            mCategoryAdapter.check(position)
            (rvCategory.layoutManager as? LinearLayoutManager)
                ?.scrollToPositionWithOffset(position, 8f.toIntPx())
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }
}
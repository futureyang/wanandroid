package com.future.wanandroid.ui.main.hierarchy.page

import android.os.Bundle
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
import com.future.wanandroid.databinding.FragmentHierarchPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * Created by yangqc on 2020/12/31
 * 体系分类数据展示页
 */
@AndroidEntryPoint
class HierarchPageFragment : BaseVmFragment<HierarchPageViewModle, FragmentHierarchPageBinding>(), ScrollToTop {

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

    lateinit var categoryList: List<Category>

    private lateinit var mAdapter: SimpleArticleAdapter

    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun viewModelClass() = HierarchPageViewModle::class.java

    override fun getLayoutId() = R.layout.fragment_hierarch_page

    override fun initView() {
        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0

        mBinding.resId = R.color.textColorPrimary
        mBinding.fragment = this
        mBinding.categoryList = categoryList

        mCategoryAdapter = CategoryAdapter().apply {
            bindToRecyclerView(mBinding.rvCategory)
            setNewData(categoryList)
            onCheckedListener = {
                checkedPosition = it
                mViewModel.refresh(categoryList[checkedPosition].id)
            }
        }
        mAdapter = SimpleArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMore(categoryList[checkedPosition].id)
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
                    val article = mAdapter.data[i]
                    mViewModel.collect(article.id, article.collect)
                }
            }
        }
        mBinding.reloadListView.btnReload.setOnClickListener {
            mViewModel.refresh(categoryList[checkedPosition].id)
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
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun lazyLoadData() {
        mViewModel.refresh(categoryList[checkedPosition].id)
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

    fun check(position: Int) {
        if (position != checkedPosition) {
            checkedPosition = position
            mCategoryAdapter.check(position)
            (mBinding.rvCategory.layoutManager as? LinearLayoutManager)
                ?.scrollToPositionWithOffset(position, 8f.toIntPx())
            mViewModel.refresh(categoryList[checkedPosition].id)
        }
    }
}
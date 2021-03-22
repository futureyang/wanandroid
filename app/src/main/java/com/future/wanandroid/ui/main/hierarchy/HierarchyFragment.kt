package com.future.wanandroid.ui.main.hierarchy

import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.bean.Category
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.SimpleFragmentPagerAdapter
import com.future.wanandroid.databinding.FragmentHierarchyBinding
import com.future.wanandroid.ui.main.MainActivity
import com.future.wanandroid.ui.main.hierarchy.category.HierarchCategoryFragment
import com.future.wanandroid.ui.main.hierarchy.page.HierarchPageFragment
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_hierarchy.*
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * 体系
 */
@AndroidEntryPoint
class HierarchyFragment : BaseVmFragment<HierarchyViewModle, FragmentHierarchyBinding>(), ScrollToTop {

    companion object {
        fun newInstance() = HierarchyFragment()
    }

    private var currentOffset = 0

    var hierarchCategoryFragment: HierarchCategoryFragment? = null
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<HierarchPageFragment>()

    override fun viewModelClass() = HierarchyViewModle::class.java

    override fun getLayoutId() = R.layout.fragment_hierarchy

    override fun initView() {
        mBinding.fragment = this
        reloadView.btnReload.setOnClickListener {
            mViewModel.getArticleCategory()
        }
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
    }

    override fun initData() {
        mViewModel.getArticleCategory()
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            categories.observe(viewLifecycleOwner, Observer {
                setup(it)
                hierarchCategoryFragment = HierarchCategoryFragment.newInstance(ArrayList(it))
            })
        }
    }

    //初始化数据
    private fun setup(categories: MutableList<Category>) {
        titles.clear()
        fragments.clear()
        categories.forEach {
            titles.add(it.name)
            fragments.add(HierarchPageFragment.newInstance(it.children))
        }
        viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
        viewPager.offscreenPageLimit = titles.size
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun scrollToTop() {
        if (fragments.isEmpty() || viewPager == null) return
        fragments[viewPager.currentItem].scrollToTop()
    }

    //获取当前选中的分类
    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty() || viewPager == null) return 0 to 0
        val first = viewPager.currentItem
        val second = fragments[viewPager.currentItem].checkedPosition
        return first to second
    }

    //选中了一个分类
    fun check(position: Pair<Int, Int>) {
        if (fragments.isEmpty() || viewPager == null) return
        viewPager.currentItem = position.first
        fragments[position.first].check(position.second)
    }
}
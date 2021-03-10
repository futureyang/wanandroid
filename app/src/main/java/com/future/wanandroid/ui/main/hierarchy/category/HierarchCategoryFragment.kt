package com.future.wanandroid.ui.main.hierarchy.category

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.wanandroid.MyApplication
import com.future.wanandroid.R
import com.future.wanandroid.bean.Category
import com.future.wanandroid.ui.main.hierarchy.HierarchyFragment
import com.future.wanandroid.util.getSreenHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_common_recyclerview.*

class HierarchCategoryFragment : BottomSheetDialogFragment() {

    companion object {
        const val CATEGORY_LIST = "categoryList"
        fun newInstance(
            categoryList: ArrayList<Category>
        ): HierarchCategoryFragment {
            return HierarchCategoryFragment().apply {
                arguments = Bundle().apply { putParcelableArrayList(CATEGORY_LIST, categoryList) }
            }
        }
    }

    private var height: Int? = null
    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_common_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList: List<Category> = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        val checked = (parentFragment as HierarchyFragment).getCurrentChecked()
        HierarchCategoryAdapter(R.layout.item_hierarch_category, categoryList, checked).run {
            bindToRecyclerView(recyclerView_common)
            onCheckedListener = {
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                view.postDelayed({
                    (parentFragment as HierarchyFragment).check(it)
                }, 300)
            }
        }
        view.post {
            (recyclerView_common.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(checked.first, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height ?: ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: (getSreenHeight(MyApplication.context) * 0.75f).toInt()
        if (!this.isAdded) {
            super.show(manager, "SystemCategoryFragment")
        }
    }

}
package com.future.wanandroid.ui.main.hierarchy.category

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.bean.Category
import com.future.wanandroid.ext.htmlToSpanned
import kotlinx.android.synthetic.main.item_hierarch_category.view.*

class HierarchCategoryAdapter(
    layoutResId: Int = R.layout.item_hierarch_category,
    categoryList: List<Category>,
    var checked: Pair<Int, Int>
) : BaseQuickAdapter<Category, BaseViewHolder>(layoutResId, categoryList) {

    var onCheckedListener: ((checked: Pair<Int, Int>) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: Category) {
        helper.itemView.run {
            title.text = item.name.htmlToSpanned()
            tagFlowLayout.adapter = ItemTagAdapter(item.children)
            if (checked.first == helper.adapterPosition) {
                tagFlowLayout.adapter.setSelectedList(checked.second)
            }
            tagFlowLayout.setOnTagClickListener { _, position, _ ->
                checked = helper.adapterPosition to position
                notifyDataSetChanged()
                tagFlowLayout.postDelayed({
                    onCheckedListener?.invoke(checked)
                }, 300)
                true
            }
        }
    }
}
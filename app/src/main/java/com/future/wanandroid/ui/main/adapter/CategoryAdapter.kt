package com.future.wanandroid.ui.main.adapter

import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.future.wanandroid.R
import com.future.wanandroid.bean.Category
import com.future.wanandroid.ext.htmlToSpanned
import com.future.wanandroid.ext.toIntPx
import kotlinx.android.synthetic.main.item_category_sub.view.*

class CategoryAdapter(layoutResId: Int = R.layout.item_category_sub) :
    BaseQuickAdapter<Category, BaseViewHolder>(layoutResId) {

    private var checkedPosition = 0
    var onCheckedListener: ((position: Int) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: Category) {
        helper.itemView.run {
            ctvCategory.text = item.name.htmlToSpanned()
            ctvCategory.isChecked = checkedPosition == helper.adapterPosition
            setOnClickListener {
                val position = helper.adapterPosition
                check(position)
                onCheckedListener?.invoke(position)
            }
            updateLayoutParams<MarginLayoutParams> {
                marginStart = if (helper.adapterPosition == 0) 8f.toIntPx() else 0f.toIntPx()
            }
        }
    }

    fun check(position: Int) {
        checkedPosition = position
        notifyDataSetChanged()
    }

}
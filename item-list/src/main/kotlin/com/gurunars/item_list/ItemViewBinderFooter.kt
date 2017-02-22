package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*

internal class ItemViewBinderFooter<in ItemType : Item> : ItemViewBinder<ItemType> {

    override fun getView(context: Context): View {
        val ui = AnkoContext.createReusable(context, this)
        return ui.textView {
            id=R.id.footer
            padding=dip(15)
            setTag(FOOTER_TYPE, FOOTER_TYPE)
            layoutParams= ViewGroup.LayoutParams(matchParent, wrapContent)
        }
    }

    override fun bind(itemView: View, item: ItemType, previousItem: ItemType?) {

    }

    companion object {

        val FOOTER_TYPE = -42
    }
}

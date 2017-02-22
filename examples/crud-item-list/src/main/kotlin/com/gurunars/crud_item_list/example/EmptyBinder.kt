package com.gurunars.crud_item_list.example

import android.content.Context
import android.view.Gravity.CENTER
import com.gurunars.item_list.EmptyViewBinder
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textView

internal class EmptyBinder : EmptyViewBinder {

    override fun getView(context: Context) = AnkoContext.createReusable(context, this).textView {
        id=R.id.noItemsLabel
        gravity=CENTER
        width= matchParent
        height= matchParent
        text=context.getString(R.string.no_items_at_all)
    }

}

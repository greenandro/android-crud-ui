package com.gurunars.item_list

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.textView

internal class ItemViewBinderEmpty : EmptyViewBinder {

    override fun getView(context: Context): View {
        val ui = AnkoContext.createReusable(context, this)
        return ui.textView {
            gravity=CENTER
            text=context.getString(R.string.empty)
        }
    }

    companion object {
        val EMPTY_TYPE = -404
    }

}

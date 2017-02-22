package com.gurunars.item_list

import android.animation.ValueAnimator
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*

internal class ItemViewBinderString<in ItemType : Item> : ItemViewBinder<ItemType> {

    override fun getView(context: Context): View {
        val ui = AnkoContext.createReusable(context, this)
        return ui.textView {
            padding=dip(15)
            layoutParams= ViewGroup.LayoutParams(matchParent, wrapContent)
        }
    }

    private fun animateUpdate(view: View) {
        view.clearAnimation()
        val anim = ValueAnimator()
        anim.setFloatValues(1.0.toFloat(), 0.0.toFloat(), 1.0.toFloat())
        anim.addUpdateListener { animation -> view.alpha = animation.animatedValue as Float }
        anim.duration = 1300
        anim.start()
    }

    override fun bind(itemView: View, item: ItemType, previousItem: ItemType?) {
        (itemView as TextView).text = item.toString()
        if (previousItem != null) {
            animateUpdate(itemView)
        }
    }

}

package com.gurunars.leaflet_view

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView

/**
 * Renderer to be used when there are no pages in the list.
 */
interface NoPageRenderer {
    /**
     * Render a view to be shown when all pages are removed
     *
     * @return Rendered and populated view
     */
    fun renderNoPage(): View

    /**
     * Called when the no page view is navigated into
     */
    fun enter()

    class Default(val ui: AnkoContext<Any>) : NoPageRenderer {

        override fun renderNoPage(): View {
            return ui.relativeLayout {
                lparams(width= ViewGroup.LayoutParams.MATCH_PARENT, height= ViewGroup.LayoutParams.MATCH_PARENT)
                textView {
                    lparams(width= ViewGroup.LayoutParams.WRAP_CONTENT, height= ViewGroup.LayoutParams.WRAP_CONTENT)
                    text=context.getString(R.string.empty)
                }
                gravity = Gravity.CENTER
            }
        }

        override fun enter() {

        }
    }
}

package com.gurunars.leaflet_view

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView

/**
 * Renderer of individual pages.
 *
 * @param <ViewT> Type of the view to be rendered.
 * @param <PageT> Page to be rendered via the view.
 */
interface PageRenderer<ViewT : View, in PageT : Page> {
    /**
     * Render a page as a view

     * @param page payload to be used to populate the view
     * @return Rendered and populated view
     */
    fun renderPage(page: PageT): ViewT

    /**
     * Called when the page is navigated into
     *
     * @param pageView this view is entered
     */
    fun enter(pageView: ViewT)

    /**
     * Called when the page is navigated from
     *
     * @param pageView this view is abandoned
     */
    fun leave(pageView: ViewT)

    companion object Default {
        internal fun <PageT: Page> render(ui:AnkoContext<Any>, page:PageT) : View {
            return ui.relativeLayout {
                lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                        height= ViewGroup.LayoutParams.MATCH_PARENT)
                textView {
                    lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                            height= ViewGroup.LayoutParams.MATCH_PARENT)
                    text=page.toString()
                }
                gravity = Gravity.CENTER
            }
        }
    }
}

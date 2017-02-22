package com.gurunars.leaflet_view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.viewPager

/**
 * View pager without fragments on steroids.
 *
 * @param <ViewT> View subclass to be used to render individual pages
 * @param <PageT> Page subclass to be used to populate the pages
 */
class LeafletView<ViewT : View, PageT : Page> @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    lateinit private var viewPager : ViewPager
    lateinit private var emptyHolder : ViewGroup

    private val ui = AnkoContext.createReusable(context, this)
    private val leafletAdapter: LeafletAdapter<ViewT, PageT>

    init {
        frameLayout {
            lparams(width= matchParent, height=matchParent)
            viewPager = viewPager {
                lparams(width=matchParent, height=matchParent)
                id = R.id.viewPager
            }
            emptyHolder = frameLayout {
                lparams(width=matchParent, height=matchParent)
                id = R.id.emptyHolder
            }
        }
        leafletAdapter = LeafletAdapter<ViewT, PageT>(context, viewPager, emptyHolder)
        viewPager.adapter = leafletAdapter
        leafletAdapter.setNoPageRenderer(NoPageRenderer.Default(ui))
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                leafletAdapter.goTo(leafletAdapter.currentPage)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    /**
     * @param pageRenderer a substance that produces a view to be shown for a given page
     */
    fun setPageRenderer(pageRenderer: PageRenderer<ViewT, PageT>) {
        leafletAdapter.setPageRenderer(pageRenderer)
    }

    /**
     * @param noPageRenderer a substance that produces a view to be show when there are no pages
     */
    fun setNoPageRenderer(noPageRenderer: NoPageRenderer) {
        leafletAdapter.setNoPageRenderer(noPageRenderer)
    }

    /**
     * @param pages a collection of payloads to traverse
     */
    fun setPages(pages: List<PageT>) {
        leafletAdapter.setPages(pages)
    }

    /**
     * @return currently selected page
     */
    val currentPage: PageT?
        get() = leafletAdapter.currentPage

    /**
     * Scroll to a specific page. The page is expected to exist in a collection set via setPages.
     *
     * @param page page's payload to navigate to
     */
    fun goTo(page: PageT) {
        leafletAdapter.goTo(page)
    }
}

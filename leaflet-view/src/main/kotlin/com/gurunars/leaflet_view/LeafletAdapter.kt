package com.gurunars.leaflet_view

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.esotericsoftware.kryo.Kryo
import org.jetbrains.anko.AnkoContext
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*


internal class LeafletAdapter<ViewT : View, PageT : Page>(
        context: Context,
        private val pager: ViewPager,
        private val emptyHolder: ViewGroup) : PagerAdapter() {

    private val ui = AnkoContext.createReusable(context, this)
    private val kryo = Kryo()
    private var pageRenderer: PageRenderer<ViewT, PageT>? = null
    private var pages: List<PageHolder<PageT>> = ArrayList()
    private val mapping = SparseArray<ViewT>()
    private var noPageRenderer: NoPageRenderer? = null

    init {
        this.kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    fun setNoPageRenderer(noPageRenderer: NoPageRenderer) {
        this.noPageRenderer = noPageRenderer
    }

    fun setPageRenderer(pageRenderer: PageRenderer<ViewT, PageT>) {
        this.pageRenderer = pageRenderer
        this.mapping.clear()
        notifyDataSetChanged()
    }

    fun setPages(pages: List<PageT>) {
        val oldCurrent = currentPage
        this.pages = this.kryo.copy(PageHolder.wrap(pages))
        notifyDataSetChanged()
        if (oldCurrent != null && this.pages.contains(PageHolder(oldCurrent))) {
            goTo(oldCurrent)
        } else {
            goTo(currentPage)
        }
    }

    val currentPage: PageT?
        get() = if (pages.isEmpty()) null else pages[Math.min(pager.currentItem, pages.size - 1)].page

    fun goTo(page: PageT?) {
        if (page == null) {
            pager.visibility = View.GONE
            emptyHolder.visibility = View.VISIBLE
            emptyHolder.removeAllViews()
            emptyHolder.addView(noPageRenderer!!.renderNoPage())
            noPageRenderer!!.enter()
        } else {
            val desiredIndex = pages.indexOf(PageHolder(page))
            if (desiredIndex != pager.currentItem) {
                pager.setCurrentItem(desiredIndex, true)
            }
            pager.visibility = View.VISIBLE
            emptyHolder.visibility = View.GONE

            for (i in 0..mapping.size() - 1) {
                leave(pageRenderer, mapping.get(i))
            }
            enter(pageRenderer, mapping.get(desiredIndex))
        }
    }

    override fun getCount(): Int {
        return pages.size
    }

    private fun enter(renderer: PageRenderer<ViewT, PageT>?, pageView: ViewT?) {
        if (renderer != null && pageView != null) {
            renderer.enter(pageView)
        }
    }

    private fun leave(renderer: PageRenderer<ViewT, PageT>?, pageView: ViewT?) {
        if (renderer != null && pageView != null) {
            renderer.leave(pageView)
        }
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any? {
        val page = pages[position].page
        var view: ViewT? = mapping.get(position)
        var actual: View? = view

        if (actual == null) {
            if (pageRenderer != null) {
                view = pageRenderer!!.renderPage(page)
                mapping.put(position, view)
                if (position == pager.currentItem) {
                    enter(pageRenderer, view)
                }
            }

            actual = if (view == null) PageRenderer.render(ui, page) else view
            collection.addView(actual)
        }

        actual.id = position
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
        mapping.remove(position)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getPageTitle(position: Int): CharSequence {
        return pages[position].toString()
    }

    override fun getItemPosition(obj: Any?): Int {
        val pos = pages.indexOf(obj)
        return if (pos == -1) PagerAdapter.POSITION_NONE else pos
    }
}

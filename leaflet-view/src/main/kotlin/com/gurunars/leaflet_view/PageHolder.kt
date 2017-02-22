package com.gurunars.leaflet_view

import java.io.Serializable
import java.util.ArrayList

internal class PageHolder<out PageT : Page>(val page: PageT) : Serializable {

    override fun hashCode(): Int {
        return java.lang.Long.valueOf(page.id)!!.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PageHolder<*>) {
            return false
        }

        return page.id == other.page.id
    }

    companion object {

        fun <PageT : Page> wrap(pages: List<PageT>): ArrayList<PageHolder<PageT>> {
            val wrapped = ArrayList<PageHolder<PageT>>()
            for (page in pages) {
                wrapped.add(PageHolder(page))
            }
            return wrapped
        }
    }

}

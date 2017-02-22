package com.gurunars.leaflet_view

import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView


inline fun <ViewT: View, PageT: Page> ViewManager.leafletView(theme: Int = 0) : LeafletView<ViewT, PageT> = leafletView(theme) {}
inline fun <ViewT: View, PageT: Page> ViewManager.leafletView(theme: Int = 0, init: LeafletView<ViewT, PageT>.() -> Unit) = ankoView({ LeafletView(it) }, theme, init)

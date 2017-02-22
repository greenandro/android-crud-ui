package com.gurunars.android_utils.ui

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape

/**
 * Drawable with customizable shape and color.
 */
class ColoredShapeDrawable(shape: Shape, color: Int) : ShapeDrawable(shape) {

    init {
        paint.color = color
    }

}

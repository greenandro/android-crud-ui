package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.gurunars.android_utils.ui.ColoredShapeDrawable
import com.gurunars.android_utils.ui.apply


internal class CircularIconButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ImageButton(context, attrs, defStyleAttr) {

    @State var circleColor: Int = Color.WHITE
    set(value) {
        field = value
        reset()
    }

    @State var foregroundColor: Int = Color.BLACK
    set(value) {
        field = value
        reset()
    }
    @State var innerDrawable: Int = R.drawable.ic_plus
    set(value) {
        field = value
        reset()
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        isClickable = true

        val a = context.obtainStyledAttributes(attrs, R.styleable.CircularIconButton)

        circleColor = a.getColor(
                R.styleable.CircularIconButton_backgroundColor,
                ContextCompat.getColor(context, R.color.White))
        foregroundColor = a.getColor(
                R.styleable.CircularIconButton_foregroundColor,
                ContextCompat.getColor(context, R.color.Black))
        innerDrawable = a.getResourceId(
                R.styleable.CircularIconButton_innerDrawable,
                R.drawable.ic_plus)

        a.recycle()

        reset()
    }

    private fun reset() {
        background = ColoredShapeDrawable(OvalShape(), circleColor)
        apply(this, 4)

        scaleType = ImageView.ScaleType.CENTER_CROP
        setPadding(8, 8, 8, 8)

        val fg = ContextCompat.getDrawable(context, innerDrawable)
        fg.setColorFilter(foregroundColor, PorterDuff.Mode.SRC_IN)
        fg.alpha = if (isEnabled) 255 else 50
        setImageDrawable(InsetDrawable(fg, ICON_PADDING))
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        reset()
    }

    override fun onSaveInstanceState(): Parcelable {
        return StateSaver.saveInstanceState(this, super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(StateSaver.restoreInstanceState(this, state))
    }

    companion object {
        private val ICON_PADDING = 50
    }
}

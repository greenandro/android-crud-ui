package com.gurunars.floatmenu

import android.animation.*
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import com.gurunars.android_utils.ui.ColoredShapeDrawable
import com.gurunars.android_utils.ui.apply
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver

internal class Fab @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val ICON_PADDING = 100

    @State var leftHanded : Boolean = false
        set(value) {
            field = value
            loadFromState()
        }

    @State var openIconBgColor: Int = Color.RED
        set(value) {
            field = value
            loadFromState()
        }
    @State var openIconFgColor: Int = Color.WHITE
        set(value) {
            field = value
            loadFromState()
        }
    @State var closeIconBgColor: Int = Color.RED
        set(value) {
            field = value
            loadFromState()
        }
    @State var closeIconFgColor: Int = Color.WHITE
        set(value) {
            field = value
            loadFromState()
        }
    @State var closeIcon = R.drawable.ic_menu_close
        set(value) {
            field = value
            loadFromState()
        }
    @State var openIcon = R.drawable.ic_menu
        set(value) {
            field = value
            loadFromState()
        }

    @State var currentBgColor: Int = 0
    @State var currentIcon: Int = 0
    @State var currentFgColor: Int = 0

    // no reload
    @State var rotationDuration = 400

    private val actualImageView: ImageView = ImageView(getContext())

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        actualImageView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        addView(actualImageView)
        loadFromState()
    }

    private fun reloadUi() {
        currentIcon = if (isActivated) closeIcon else openIcon
        // Bg
        background = ColoredShapeDrawable(OvalShape(), currentBgColor)
        apply(this, 6)
        // Icon
        val fg = ResourcesCompat.getDrawable(resources, currentIcon, null)
        fg?.setColorFilter(currentFgColor, PorterDuff.Mode.SRC_IN)
        actualImageView.setImageDrawable(InsetDrawable(fg, ICON_PADDING))
        contentDescription = "|BG:$currentBgColor|IC:$currentFgColor|ACT:$isActivated|LH:$leftHanded"
        requestLayout()
    }

    private fun loadFromState() {
        currentIcon = if (isActivated) closeIcon else openIcon
        currentBgColor = if (isActivated) closeIconBgColor else openIconBgColor
        currentFgColor = if (isActivated) closeIconFgColor else openIconFgColor
        actualImageView.rotation = (if (isActivated) 360 else 0).toFloat()

        if (layoutParams != null) {
            val layout = layoutParams as RelativeLayout.LayoutParams

            layout.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            layout.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)

            layout.addRule(if (leftHanded)
                RelativeLayout.ALIGN_PARENT_LEFT
            else
                RelativeLayout.ALIGN_PARENT_RIGHT)
        }

        reloadUi()
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", StateSaver.saveInstanceState(this, super.onSaveInstanceState()))
        bundle.putBoolean("isActivated", isActivated)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(StateSaver.restoreInstanceState(this, localState.getParcelable<Parcelable>("superState")))
        super.setActivated(localState.getBoolean("isActivated"))
        loadFromState()
    }

    override fun setActivated(isActive: Boolean) {
        if (isActivated == isActive) {
            return
        }

        val originalState = isActivated

        val rotation = ObjectAnimator.ofFloat(actualImageView,
                "rotation",
                if (isActive) 0F else 360F,
                if (isActive) 360F else 0F)

        val bgColorAnimation = ValueAnimator.ofObject(
                ArgbEvaluator(),
                if (isActive) openIconBgColor else closeIconBgColor,
                if (isActive) closeIconBgColor else openIconBgColor)
        bgColorAnimation.addUpdateListener { animation -> currentBgColor = animation.animatedValue as Int }

        val fgColorAnimation = ValueAnimator.ofObject(
                ArgbEvaluator(),
                if (isActive) openIconFgColor else closeIconFgColor,
                if (isActive) closeIconFgColor else openIconFgColor)
        fgColorAnimation.addUpdateListener { animation -> currentFgColor = animation.animatedValue as Int }

        val uiUpdateAnimaton = ValueAnimator.ofFloat(0F, 1F)
        uiUpdateAnimaton.addUpdateListener { reloadUi() }

        val animatorSet = AnimatorSet()
        animatorSet.startDelay = 0
        animatorSet.duration = rotationDuration.toLong()
        animatorSet.playTogether(rotation, bgColorAnimation, fgColorAnimation, uiUpdateAnimaton)
        animatorSet.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                val handler = Handler()
                handler.postDelayed({
                    super@Fab.setActivated(isActive)
                    reloadUi()
                }, (rotationDuration / 2).toLong())
            }

            override fun onAnimationCancel(animation: Animator) {
                super@Fab.setActivated(originalState)
                reloadUi()
            }
        })
        animatorSet.start()

    }

}

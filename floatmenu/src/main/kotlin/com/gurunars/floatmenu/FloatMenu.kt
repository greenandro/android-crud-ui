package com.gurunars.floatmenu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import org.jetbrains.anko.*

/**
 * Floating menuPane available via a [FAB](https://material.google.com/components/buttons-floating-action-button.html).
 */
class FloatMenu @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val DURATION_IN_MILLIS = 400

    lateinit private var openFab: Fab
    lateinit private var menuPane: MenuPane
    lateinit private var contentPane: ViewGroup

    private var onCloseListener: AnimationListener = AnimationListener.DefaultAnimationListener()
    private var onOpenListener: AnimationListener = AnimationListener.DefaultAnimationListener()

    init {
        relativeLayout {
            lparams(width= matchParent,
                    height= matchParent)
            contentPane = frameLayout {
                lparams(width= matchParent,
                        height= matchParent)
                id=R.id.contentPane
            }
            menuPane = menuPane {
                visibility= GONE
                lparams(width= matchParent,
                        height= matchParent)
                id=R.id.menuPane
            }
            openFab = fab {
                contentDescription="X"
                lparams(width= dip(60),
                        height= dip(60)) {
                    margin=dip(16)
                    alignParentBottom()
                    alignParentRight()
                }
                id=R.id.openFab
            }
        }

        openFab.setOnClickListener { setFloatingMenuVisibility(!openFab.isActivated) }

        setAnimationDuration(DURATION_IN_MILLIS)

        val a = context.obtainStyledAttributes(attrs, R.styleable.FloatMenu)

        setHasOverlay(a.getBoolean(
                R.styleable.FloatMenu_fabHasOverlay,
                true))

        val openIconBgColor = a.getColor(
                R.styleable.FloatMenu_fabOpenIconBgColor,
                Color.RED)
        val openIconFgColor = a.getColor(
                R.styleable.FloatMenu_fabOpenIconFgColor,
                Color.WHITE)
        val closeIconBgColor = a.getColor(
                R.styleable.FloatMenu_fabCloseIconBgColor,
                openIconBgColor)
        val closeIconFgColor = a.getColor(
                R.styleable.FloatMenu_fabCloseIconFgColor,
                openIconFgColor)

        setOpenIcon(a.getResourceId(
                R.styleable.FloatMenu_fabOpenIcon,
                R.drawable.ic_menu))
        setCloseIcon(a.getResourceId(
                R.styleable.FloatMenu_fabCloseIcon,
                R.drawable.ic_menu_close))

        setOpenIconBgColor(openIconBgColor)
        setOpenIconFgColor(openIconFgColor)
        setCloseIconBgColor(closeIconBgColor)
        setCloseIconFgColor(closeIconFgColor)

        setLeftHanded(a.getBoolean(R.styleable.FloatMenu_fabLeftHanded, false))

        a.recycle()

    }

    private fun setFloatingMenuVisibility(visible: Boolean) {

        if ((menuPane.visibility == View.VISIBLE) == visible) {
            return
        }

        openFab.isClickable = false
        openFab.isActivated = visible
        menuPane.isActivated = visible

        val listener = if (visible) onOpenListener else onCloseListener
        val targetVisibility = if (visible) View.VISIBLE else View.GONE
        val sourceAlpha = if (visible) 0.0f else 1.0f
        val targetAlpha = if (visible) 1.0f else 0.0f

        listener.onStart(openFab.rotationDuration)
        menuPane.visibility = View.VISIBLE
        menuPane.alpha = sourceAlpha
        menuPane.animate()
                .alpha(targetAlpha)
                .setDuration(openFab.rotationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        menuPane.visibility = targetVisibility
                        listener.onFinish()
                        openFab.isClickable = true
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        onAnimationEnd(animation)
                    }
                })
    }

    /*
     * Collapse the menuPane.
     */
    fun close() {
        setFloatingMenuVisibility(false)
    }

    /**
     * Expand the menuPane.
     */
    fun open() {
        setFloatingMenuVisibility(true)
    }

    /**
     * @param contentView view to be shown in the content area (clickable when the menuPane is closed)
     */
    fun setContentView(contentView: View) {
        contentPane.removeAllViews()
        contentPane.addView(contentView)
    }

    /**
     * @param onCloseListener actions to be triggered before and after the menuPane is closed
     */
    fun setOnCloseListener(onCloseListener: AnimationListener) {
        this.onCloseListener = onCloseListener
    }

    /**
     * @param onOpenListener actions to be triggered before and after the menuPane is open
     */
    fun setOnOpenListener(onOpenListener: AnimationListener) {
        this.onOpenListener = onOpenListener
    }

    /**
     * @param menuView view to be shown in the menuPane area (clickable when the menuPane is open)
     */
    fun setMenuView(menuView: View) {
        menuPane.removeAllViews()
        menuPane.addView(menuView)
    }

    /**
     * @param hasOverlay false to disable a shaded background, true to enable it. If overlay is
     * * disabled the clicks go through the view group to the view in the back. If it is enabled the
     * * clicks are intercepted by the group.
     */
    fun setHasOverlay(hasOverlay: Boolean) {
        menuPane.isClickable = hasOverlay
    }

    /**
     * @param leftHanded if true - FAB shall be in the bottom left corner, if false - in the bottom right.
     */
    fun setLeftHanded(leftHanded: Boolean) {
        openFab.leftHanded = leftHanded
    }

    /**
     * @param durationInMillis FAB rotation and menuPane appearence duration in milliseconds.
     */
    fun setAnimationDuration(durationInMillis: Int) {
        openFab.rotationDuration = durationInMillis
    }

    /**
     * @param icon - image to be shown in the button clicking which opens the menuPane.
     */
    fun setOpenIcon(icon: Int) {
        openFab.openIcon = icon
    }

    /**
     * @param bgColor - Background color of the button clicking which opens the menuPane.
     */
    fun setOpenIconBgColor(bgColor: Int) {
        openFab.openIconBgColor = bgColor
    }

    /**
     * @param fgColor - Color of the icon shown in the button clicking which opens the menuPane.
     */
    fun setOpenIconFgColor(fgColor: Int) {
        openFab.openIconFgColor = fgColor
    }

    /**
     * @param icon - image to be shown in the button clicking which closes the menuPane.
     */
    fun setCloseIcon(icon: Int) {
        openFab.closeIcon = icon
    }

    /**
     * @param bgColor - Background color of the button clicking which closes the menuPane.
     */
    fun setCloseIconBgColor(bgColor: Int) {
        openFab.closeIconBgColor = bgColor
    }

    /**
     * @param fgColor - Color of the icon shown in the button clicking which closes the menuPane.
     */
    fun setCloseIconFgColor(fgColor: Int) {
        openFab.closeIconFgColor = fgColor
    }

}


package com.gurunars.crud_item_list


import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.KeyEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.*
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import org.jetbrains.anko.*

internal class ContextualMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private interface MoveAction {
        fun perform(isActive: Boolean)
    }

    internal interface MenuListener {
        fun delete()
        fun edit()
        fun moveUp(isActive: Boolean)
        fun moveDown(isActive: Boolean)
        fun selectAll()
    }

    private var menuListener: MenuListener? = null

    @State var iconBgColor: Int = 0
    set(value) {
        field = value
        reload()
    }

    @State var iconFgColor: Int = 0
    set(value) {
        field = value
        reload()
    }

    @State var leftHanded = false
    set(value) {
        field = value
        reload()
    }

    @State var sortable = true
    set(value) {
        field = value
        reload()
    }

    init {
        reload()
    }

    private fun reload() {
        removeAllViews()

        contentDescription = if (leftHanded) "LEFT HANDED" else "RIGHT HANDED"

        val parentAlignment = if (leftHanded) ALIGN_PARENT_LEFT else ALIGN_PARENT_RIGHT
        val horizontalMargins =
                if (leftHanded)
                    Pair(R.dimen.largeMargin, R.dimen.smallMargin)
                else
                    Pair(R.dimen.smallMargin, R.dimen.largeMargin)
        val positioning = if (leftHanded) RIGHT_OF else LEFT_OF

        relativeLayout {
            id=R.id.menuContainer
            lparams(width= matchParent, height= matchParent)

            fun button(btnId: Int,
                       iconId: Int,
                       vertical:Boolean=true) : Pair<View, RelativeLayout.LayoutParams> {
                val verticalMargins = if (vertical) 5 else 23
                val horizontalMargin = if (vertical) 23 else 5
                val view = circleButton {
                    id=btnId
                    circleColor=iconBgColor
                    foregroundColor=iconFgColor
                    lparams(width=dip(45), height=dip(45)) {
                        topMargin=dip(verticalMargins)
                        bottomMargin=dip(verticalMargins)
                        leftMargin=dip(horizontalMargin)
                        rightMargin=dip(horizontalMargin)
                        innerDrawable = iconId
                    }
                }
                return Pair(view, view.layoutParams as RelativeLayout.LayoutParams)
            }

            fun configureMoveBtn(btn: View, action: MoveAction) {
                btn.setOnTouchListener { v, event ->
                    val motion = event.actionMasked
                    if (motion == ACTION_DOWN) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        action.perform(true)
                    } else if (motion == ACTION_UP || motion == ACTION_CANCEL) {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        action.perform(false)
                    }
                    true
                }
            }

            val moveUp = button(R.id.moveUp, R.drawable.ic_move_up)
            moveUp.second.addRule(parentAlignment)
            moveUp.second.above(R.id.moveDown)
            configureMoveBtn(moveUp.first, object : MoveAction {
                override fun perform(isActive: Boolean) {
                    menuListener?.moveUp(isActive)
                }
            })

            val moveDown = button(R.id.moveDown, R.drawable.ic_move_down)
            moveDown.second.addRule(parentAlignment)
            moveDown.second.alignParentBottom()
            moveDown.second.bottomMargin=dip(85)
            configureMoveBtn(moveDown.first, object : MoveAction {
                override fun perform(isActive: Boolean) {
                    menuListener?.moveDown(isActive)
                }
            })

            val delete = button(R.id.delete, R.drawable.ic_delete, false)
            delete.first.setOnClickListener { menuListener?.delete() }
            delete.second.alignParentBottom()
            delete.second.addRule(positioning, R.id.selectAll)

            val selectAll = button(R.id.selectAll, R.drawable.ic_select_all, false)
            selectAll.first.setOnClickListener { menuListener?.selectAll() }
            selectAll.second.alignParentBottom()
            selectAll.second.addRule(positioning, R.id.edit)

            val edit = button(R.id.edit, R.drawable.ic_edit, false)
            edit.first.setOnClickListener { menuListener?.edit() }
            edit.second.alignParentBottom()
            edit.second.addRule(parentAlignment)
            edit.second.leftMargin = resources.getDimensionPixelSize(horizontalMargins.first)
            edit.second.rightMargin = resources.getDimensionPixelSize(horizontalMargins.second)

            val visibility = if (sortable) View.VISIBLE else View.GONE

            findViewById(R.id.moveUp).visibility = visibility
            findViewById(R.id.moveDown).visibility = visibility

        }
    }

    fun setMenuListener(menuListener: MenuListener) {
        this.menuListener = menuListener
    }

    private fun configureMoveButton(buttonId: Int, isActive: Boolean, cancelContinuousMove: () -> Unit) {
        findViewById(buttonId).isEnabled = isActive
        if (!isActive) {  // cancel the action if the button is deactivated
            cancelContinuousMove()
        }
    }

    fun setUpContextualButtons(
            canEdit: Boolean, canMoveUp: Boolean, canMoveDown: Boolean, canDelete: Boolean,
            canSelectAll: Boolean) {
        findViewById(R.id.delete).isEnabled = canDelete
        findViewById(R.id.edit).isEnabled = canEdit
        configureMoveButton(R.id.moveUp, canMoveUp, { menuListener?.moveUp(false) })
        configureMoveButton(R.id.moveDown, canMoveDown, { menuListener?.moveDown(false) })
        findViewById(R.id.selectAll).isEnabled = canSelectAll
    }

    public override fun onSaveInstanceState(): Parcelable {
        return StateSaver.saveInstanceState(this, super.onSaveInstanceState())
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(StateSaver.restoreInstanceState(this, state))
    }

}

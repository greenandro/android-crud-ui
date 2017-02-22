package com.gurunars.crud_item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.gurunars.floatmenu.AnimationListener
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import com.gurunars.item_list.ItemList
import com.gurunars.item_list.itemList
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

/**
 * Widget to be used for manipulating a collection of items.
 */
class CrudItemList<ItemType : Item> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    @State var actionIconFgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var actionIconBgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var contextualCloseFgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var contextualCloseBgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var createCloseFgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var createCloseBgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var openBgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    @State var openFgColor: Int = 0
    set(value) {
        field = value
        setUpColors()
    }

    lateinit private var collectionManager: CollectionManager<ItemType>

    private val throttleBuffer = UiThrottleBuffer()
    private val scheduledRunner = ScheduledRunner()

    lateinit private var creationMenuPlaceholder: ViewGroup
    lateinit private var contextualMenu: ContextualMenu

    private val floatingMenu: FloatMenu
    private val itemList: ItemList<SelectableItem<ItemType>>

    private var onItemEdit: ((ItemType, Boolean) -> Unit)? = null
    private var onItemListChange: ((List<ItemType>) -> Unit)? = null

    private val ui = AnkoContext.createReusable(context, this)

    init {

        itemList = ui.itemList {
            id=R.id.itemList
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        floatingMenu = floatMenu {
            id=R.id.floatingMenu
            setOpenIcon(R.drawable.ic_plus)
            setAnimationDuration(ANIMATION_DURATION)
            layoutParams= ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setMenuView(ui.frameLayout {
                lparams(width= matchParent, height= matchParent)
                contextualMenu = contextualMenu {
                    id=R.id.contextualMenu
                    lparams(width= matchParent, height= matchParent)
                }
                creationMenuPlaceholder = frameLayout {
                    id=R.id.creationMenuPlaceholder
                    lparams(width= matchParent, height= matchParent)
                }
            })
            setContentView(itemList)
        }

        collectionManager = CollectionManager({
            itemList.setItems(it)
            if (collectionManager.hasSelection()) {
                setUpContextualMenu()
                contextualMenu.setUpContextualButtons(
                        collectionManager.canEdit(),
                        collectionManager.canMoveUp(),
                        collectionManager.canMoveDown(),
                        collectionManager.canDelete(),
                        collectionManager.canSelectAll())
                floatingMenu.open()
            } else {
                floatingMenu.close()
            }
        }, {throttleBuffer.call { onItemListChange?.invoke(it) } })

        val a = context.obtainStyledAttributes(attrs, R.styleable.CrudItemList)

        val bgColor = ContextCompat.getColor(context, R.color.Black)
        val fgColor = ContextCompat.getColor(context, R.color.White)

        actionIconFgColor = a.getColor(R.styleable.CrudItemList_actionIconFgColor, bgColor)
        actionIconBgColor = a.getColor(R.styleable.CrudItemList_actionIconBgColor, fgColor)
        contextualCloseFgColor = a.getColor(R.styleable.CrudItemList_contextualCloseFgColor, bgColor)
        contextualCloseBgColor = a.getColor(R.styleable.CrudItemList_contextualCloseBgColor, fgColor)
        createCloseBgColor = a.getColor(R.styleable.CrudItemList_createCloseBgColor, bgColor)
        createCloseFgColor = a.getColor(R.styleable.CrudItemList_createCloseFgColor, fgColor)
        openBgColor = a.getColor(R.styleable.CrudItemList_openBgColor, bgColor)
        openFgColor = a.getColor(R.styleable.CrudItemList_openFgColor, fgColor)

        a.recycle()

        setLeftHanded(false)
        setSortable(true)

        floatingMenu.setOnCloseListener(object : AnimationListener {
            override fun onStart(projectedDuration: Int) {
                if (collectionManager.hasSelection()) {
                    scheduledRunner.stop()
                    collectionManager.unselectAll()
                }
            }

            override fun onFinish() {
                setUpCreationMenu()
            }
        })

        contextualMenu.setMenuListener(object : ContextualMenu.MenuListener {
            override fun delete() {
                collectionManager.deleteSelected()
            }

            override fun edit() {
                collectionManager.triggerConsumption()
            }

            override fun moveUp(isActive: Boolean) {
                if (isActive) {
                    scheduledRunner.start { collectionManager.moveSelectionUp() }
                } else {
                    scheduledRunner.stop()
                }
            }

            override fun moveDown(isActive: Boolean) {
                if (isActive) {
                    scheduledRunner.start { collectionManager.moveSelectionDown() }
                } else {
                    scheduledRunner.stop()
                }
            }

            override fun selectAll() {
                collectionManager.selectAll()
            }
        })

        collectionManager.setItemConsumer { item -> onItemEdit?.invoke(item, false) }

        setUpColors()
    }

    private fun setUpColors() {
        floatingMenu.setOpenIconBgColor(openBgColor)
        floatingMenu.setOpenIconFgColor(openFgColor)
        contextualMenu.iconBgColor = actionIconBgColor
        contextualMenu.iconFgColor = actionIconFgColor
        if (collectionManager.hasSelection()) {
            setUpContextualMenu()
        } else {
            setUpCreationMenu()
        }
    }

    /**
     * @param leftHanded flag specifying if the menuPane should be left handed or not
     */
    fun setLeftHanded(leftHanded: Boolean) {
        floatingMenu.setLeftHanded(leftHanded)
        contextualMenu.leftHanded = leftHanded
    }

    private fun setUpContextualMenu() {
        creationMenuPlaceholder.visibility = View.GONE
        contextualMenu.visibility = View.VISIBLE
        floatingMenu.setCloseIconFgColor(contextualCloseFgColor)
        floatingMenu.setCloseIconBgColor(contextualCloseBgColor)
        floatingMenu.setHasOverlay(false)
    }

    private fun setUpCreationMenu() {
        creationMenuPlaceholder.visibility = View.VISIBLE
        contextualMenu.visibility = View.GONE
        floatingMenu.setCloseIconFgColor(createCloseFgColor)
        floatingMenu.setCloseIconBgColor(createCloseBgColor)
        floatingMenu.setHasOverlay(true)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(
                "superState", StateSaver.saveInstanceState(this, super.onSaveInstanceState()))
        bundle.putSerializable("selectedItems", collectionManager.saveState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(
                StateSaver.restoreInstanceState(this, localState.getParcelable<Parcelable>("superState")))
        collectionManager.loadState(localState.getSerializable("selectedItems"))
        setUpColors()
    }

    override fun onDetachedFromWindow() {
        throttleBuffer.shutdown()
        super.onDetachedFromWindow()
    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    fun setItems(items: List<ItemType>) {
        collectionManager.setItems(items)
    }

    /**
     * Close creation or contextual menuPane.
     */
    fun close() {
        floatingMenu.close()
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Items
     * @param itemViewBinder row renderer for the items of a given type
     * @param menuItemViewId id of the clickable item in creation menuPane
     * @param supplyNewItem supplier of blank new items
     */
    fun registerItemType(
            itemType: Enum<*>,
            itemViewBinder: SelectableItemViewBinder<ItemType>,
            menuItemViewId: Int,
            supplyNewItem: () -> ItemType
    ) {
        itemList.registerItemViewBinder(itemType,
                ClickableItemViewBinder(itemViewBinder, collectionManager))
        // NOTE: if creation menuPane initialization fails to bind the click listener - it should fail
        // ASAP
        findViewById(menuItemViewId)!!.setOnClickListener {
            onItemEdit?.invoke(supplyNewItem(), true)
        }
    }

    /**
     * Set the renderer to be employed when the list contains no items.

     * @param emptyViewBinder renderer for the empty list
     */
    fun setEmptyViewBinder(emptyViewBinder: EmptyViewBinder) {
        itemList.setEmptyViewBinder(emptyViewBinder)
    }

    /**
     * @param sortable if false - move up and down buttons are disabled
     */
    fun setSortable(sortable: Boolean) {
        contextualMenu.sortable = sortable
    }

    /**
     * @param onItemListChange callback to be executed whenever the list gets changed within
     * *                       the widget
     */
    fun setListChangeListener(onItemListChange : (List<ItemType>) -> Unit) {
        this.onItemListChange = onItemListChange
    }

    /**
     * @param creationMenu menuPane to be show when creation mode is active (no items selected) and the
     * *                     menuPane is open
     */
    fun setCreationMenu(creationMenu: View) {
        creationMenuPlaceholder.removeAllViews()
        creationMenuPlaceholder.addView(creationMenu)
    }

    /**
     * @param onItemEdit a listener for the cases when a new item has to be created or when
     * *                 the existing one has to be edited
     */
    fun setItemEditListener(onItemEdit: (ItemType, Boolean) -> Unit) {
        this.onItemEdit = onItemEdit
    }

    companion object {
        private val ANIMATION_DURATION = 400
    }
}

package com.gurunars.crud_item_list.example

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity.CENTER
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import com.gurunars.crud_item_list.CrudItemList
import com.gurunars.crud_item_list.crudItemList
import org.jetbrains.anko.*
import java.util.*


class ActivityMain : AppCompatActivity() {

    lateinit private var crudItemList: CrudItemList<AnimalItem>
    lateinit private var model: Model

    private fun initData(force: Boolean) {
        if (model.wasInited() && !force) {
            crudItemList.setItems(model.items)
            return
        }
        val items = ArrayList<AnimalItem>()
        for (i in 0..0) {
            items.add(AnimalItem(AnimalItem.Type.LION))
            items.add(AnimalItem(AnimalItem.Type.TIGER))
            items.add(AnimalItem(AnimalItem.Type.MONKEY))
            items.add(AnimalItem(AnimalItem.Type.WOLF))
        }
        model.clear()
        model.items = items
        crudItemList.setItems(model.items)
    }

    private fun confItemType(id: Int, type: AnimalItem.Type) {
        crudItemList.registerItemType(type,
                AnimalRowBinder(),
                id,
                { AnimalItem((model.maxItemId + 1), type)})
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = Model(this)

        val ui = AnkoContext.createReusable(this, this)

        crudItemList = ui.crudItemList<AnimalItem> {
            actionIconBgColor=ContextCompat.getColor(context, R.color.Blue)
            actionIconFgColor=ContextCompat.getColor(context, R.color.Yellow)
            contextualCloseBgColor=ContextCompat.getColor(context, R.color.Black)
            contextualCloseFgColor=ContextCompat.getColor(context, R.color.White)
            createCloseBgColor=ContextCompat.getColor(context, R.color.Red)
            createCloseFgColor=ContextCompat.getColor(context, R.color.White)
            openBgColor=ContextCompat.getColor(context, R.color.Green)
            openFgColor=ContextCompat.getColor(context, R.color.Yellow)
            setListChangeListener({ model.items = it.toMutableList() })
            setEmptyViewBinder(EmptyBinder())
            setCreationMenu(
                ui.relativeLayout {
                    id=R.id.innerLayout
                    gravity=CENTER
                    lparams(width= matchParent, height = matchParent)

                    fun button(btnId: Int, imgId: Int) : ImageButton {
                        return imageButton {
                            contentDescription=getString(R.string.monkey)
                            id=btnId
                            setImageDrawable(ContextCompat.getDrawable(context, imgId))
                            backgroundColor=ContextCompat.getColor(context, R.color.Red)
                            padding=dip(10)
                            scaleType=ImageView.ScaleType.FIT_CENTER
                            lparams (width=dip(45), height = dip(45)) {
                                margin=dip(10)
                            }
                        }
                    }

                    button(R.id.monkey, R.drawable.ic_menu_monkey)

                    (button(R.id.lion, R.drawable.ic_menu_lion).layoutParams
                            as RelativeLayout.LayoutParams)
                            .addRule(RelativeLayout.BELOW, R.id.monkey)

                    (button(R.id.tiger, R.drawable.ic_menu_tiger).layoutParams
                            as RelativeLayout.LayoutParams)
                            .addRule(RelativeLayout.RIGHT_OF, R.id.monkey)

                    val params = button(R.id.wolf, R.drawable.ic_menu_wolf).layoutParams
                            as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.RIGHT_OF, R.id.lion)
                    params.addRule(RelativeLayout.BELOW, R.id.tiger)

                }
            )
            setItemEditListener({
                editableItem, isNew ->
                editableItem.version = editableItem.version + 1
                val items = setItem(model.items, editableItem)
                model.items = items.toMutableList()
                crudItemList.setItems(items)
            })
        }
        setContentView(crudItemList)

        confItemType(R.id.lion, AnimalItem.Type.LION)
        confItemType(R.id.tiger, AnimalItem.Type.TIGER)
        confItemType(R.id.monkey, AnimalItem.Type.MONKEY)
        confItemType(R.id.wolf, AnimalItem.Type.WOLF)

        initData(false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        val lm = recyclerView.layoutManager as LinearLayoutManager
        when (i) {
            R.id.leftHanded -> crudItemList.setLeftHanded(true)
            R.id.rightHanded -> crudItemList.setLeftHanded(false)
            R.id.toTop -> lm.scrollToPositionWithOffset(0, 0)
            R.id.toBottom -> lm.scrollToPositionWithOffset(79, 0)
            R.id.reset -> initData(true)
            R.id.lock -> {
                setTitle(R.string.unsortable)
                crudItemList.setSortable(false)
            }
            R.id.unlock -> {
                setTitle(R.string.sortable)
                crudItemList.setSortable(true)
            }
            R.id.addMany -> addMany()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMany() {
        val items = ArrayList<AnimalItem>()
        for (i in 0..19) {
            items.add(AnimalItem(AnimalItem.Type.LION))
            items.add(AnimalItem(AnimalItem.Type.TIGER))
            items.add(AnimalItem(AnimalItem.Type.MONKEY))
            items.add(AnimalItem(AnimalItem.Type.WOLF))
        }
        model.clear()
        model.items = items
        crudItemList.setItems(model.items)
    }

}
package com.gurunars.item_list.example

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.ItemList
import com.gurunars.item_list.ItemViewBinder
import com.gurunars.item_list.itemList
import org.jetbrains.anko.frameLayout
import java.util.*


class ActivityMain : AppCompatActivity() {

    internal class AnimalBinder : ItemViewBinder<AnimalItem> {

        override fun getView(context: Context): View {
            val text = TextView(context)
            val padding = context.resources.getDimensionPixelOffset(R.dimen.padding)
            text.setPadding(padding, padding, padding, padding)
            return text
        }

        private fun animateUpdate(view: View) {
            view.clearAnimation()
            val anim = ValueAnimator()
            anim.setFloatValues(1.0.toFloat(), 0.0.toFloat(), 1.0.toFloat())
            anim.addUpdateListener { animation -> view.alpha = animation.animatedValue as Float }
            anim.duration = 1300
            anim.start()
        }

        override fun bind(itemView: View, item: AnimalItem, previousItem: AnimalItem?) {
            (itemView as TextView).text = "${item} [${item.type.name.toLowerCase()}]"
            if (previousItem != null) {
                animateUpdate(itemView)
            }
        }
    }

    internal class EmptyBinder : EmptyViewBinder {

        override fun getView(context: Context): View {
            val view = TextView(context)
            view.gravity = Gravity.CENTER
            view.setText(R.string.empty)
            return view
        }

    }

    lateinit private var itemList: ItemList<AnimalItem>
    private val items = ArrayList<AnimalItem>()
    private var count = 0

    private fun add(type: AnimalItem.Type) {
        items.add(AnimalItem(count.toLong(), type))
        count++
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                    height= ViewGroup.LayoutParams.MATCH_PARENT)
            itemList=itemList<AnimalItem> {
                id=R.id.itemList
                lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                        height= ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }

        for (type in AnimalItem.Type.values()) {
            itemList.registerItemViewBinder(type, AnimalBinder())
        }

        itemList.setEmptyViewBinder(EmptyBinder())

        reset()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> return showToast(clear())
            R.id.create -> return showToast(create())
            R.id.delete -> return showToast(delete())
            R.id.update -> return showToast(update())
            R.id.moveDown -> return showToast(moveTopToBottom())
            R.id.moveUp -> return showToast(moveBottomToTop())
            R.id.reset -> return showToast(reset())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clear(): Int {
        items.clear()
        itemList.setItems(items)
        return R.string.did_clear
    }

    @StringRes private fun create(): Int {
        add(AnimalItem.Type.TIGER)
        add(AnimalItem.Type.WOLF)
        add(AnimalItem.Type.MONKEY)
        add(AnimalItem.Type.LION)
        itemList.setItems(items)
        return R.string.did_create
    }

    @StringRes private fun delete(): Int {
        for (i in items.indices.reversed()) {
            if (i % 2 != 0) {
                items.removeAt(i)
            }
        }
        itemList.setItems(items)
        return R.string.did_delete
    }

    @StringRes private fun update(): Int {
        for (i in items.indices) {
            if (i % 2 != 0) {
                items[i].update()
            }
        }
        itemList.setItems(items)
        return R.string.did_update
    }

    @StringRes private fun moveBottomToTop(): Int {
        if (items.size <= 0) {
            return R.string.no_action
        }
        val item = items[items.size - 1]
        items.removeAt(items.size - 1)
        items.add(0, item)
        itemList.setItems(items)
        return R.string.did_move_to_top
    }

    @StringRes private fun moveTopToBottom(): Int {
        if (items.size <= 0) {
            return R.string.no_action
        }
        val item = items[0]
        items.removeAt(0)
        items.add(item)
        itemList.setItems(items)
        return R.string.did_move_to_bottom
    }

    @StringRes private fun reset(): Int {
        count = 0
        items.clear()
        create()
        return R.string.did_reset
    }

}
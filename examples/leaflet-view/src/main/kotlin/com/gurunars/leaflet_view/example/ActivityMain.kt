package com.gurunars.leaflet_view.example

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gurunars.leaflet_view.LeafletView
import com.gurunars.leaflet_view.NoPageRenderer
import com.gurunars.leaflet_view.PageRenderer
import com.gurunars.leaflet_view.leafletView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView
import java.util.*


class ActivityMain : AppCompatActivity() {

    private val ui = AnkoContext.createReusable(this, this)
    private var pages: MutableList<TitledPage> = ArrayList()

    lateinit private var leafletView: LeafletView<View, TitledPage>

    private fun updateAdapter() {
        Collections.sort(pages) { lhs, rhs -> lhs.title.compareTo(rhs.title) }
        leafletView.setPages(pages)
    }

    private fun getView(obj:Any, textId:Int) : View {
        return ui.relativeLayout {
            lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                    height= ViewGroup.LayoutParams.MATCH_PARENT)
            textView {
                id=textId
                lparams(width= ViewGroup.LayoutParams.WRAP_CONTENT,
                        height= ViewGroup.LayoutParams.WRAP_CONTENT)
                text=obj.toString()
            }
            tag=obj
            gravity = Gravity.CENTER
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                    height= ViewGroup.LayoutParams.MATCH_PARENT)
            leafletView=leafletView<View, TitledPage> {
                id=R.id.leafletView
                lparams(width= ViewGroup.LayoutParams.MATCH_PARENT,
                        height= ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }

        leafletView.setPageRenderer(object : PageRenderer<View, TitledPage> {
            override fun renderPage(page: TitledPage): View {
                return getView(page, R.id.pageTitle)
            }

            override fun enter(pageView: View) {
                title = pageView.tag.toString()
            }

            override fun leave(pageView: View) {

            }
        })

        leafletView.setNoPageRenderer(object : NoPageRenderer {
            override fun renderNoPage(): View {
                return getView(getString(R.string.empty), R.id.noPageText)
            }

            override fun enter() {
                title = getString(R.string.empty)
            }

        })

        load()
        updateAdapter()
    }

    private fun load() {
        pages = Gson().fromJson<List<TitledPage>>(getPreferences(Context.MODE_PRIVATE)
                .getString("data", "[]"), object : TypeToken<ArrayList<TitledPage>>() {
        }.type).toMutableList()
    }

    private fun save() {
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString("data", Gson().toJson(pages))
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.create -> {
                createPage()
                return true
            }
            R.id.delete -> {
                deletePage()
                return true
            }
            R.id.edit -> {
                editPage()
                return true
            }
            R.id.go_to -> {
                goToPage()
                return true
            }
            R.id.clear -> {
                clear()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private class TitledPageAdapter internal constructor(context: Context, items: List<TitledPage>)
        : ArrayAdapter<TitledPage>(context, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            if (convertView == null) {
                val newView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
                val text = newView.findViewById(android.R.id.text1) as TextView
                text.text = getItem(position).title
                return newView
            } else {
                return convertView
            }
        }

    }

    private fun goToPage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.go_to)
        builder.setSingleChoiceItems(
                TitledPageAdapter(this, pages), -1
        ) { dialog, which ->
            leafletView.goTo(pages[which])
            dialog.dismiss()
        }
        builder.setCancelable(true)
        builder.show()
    }

    private fun editPage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit)
        val input = EditText(this)
        input.id = R.id.pageTitle
        input.inputType = InputType.TYPE_CLASS_TEXT
        val currentPage = leafletView.currentPage ?: return
        input.setText(currentPage.title)
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            val page = leafletView.currentPage ?: return@setPositiveButton
            page.title = input.text.toString()
            // NOTE: surely equals method could have been implemented
            // however the idea is to demo that these methods are not important -
            // only getId method is
            for (i in pages.indices) {
                if (pages[i].id == page.id) {
                    pages[i] = page
                }
            }
            updateAdapter()
            save()
        }
        builder.show()
    }

    private fun deletePage() {
        // NOTE: surely equals method could have been implemented
        // however the idea is to demo that these methods are not important - only getId method is
        val page = leafletView.currentPage ?: return
        for (i in pages.indices) {
            if (pages[i].id == page.id) {
                pages.removeAt(i)
                break
            }
        }
        updateAdapter()
        save()
    }

    private fun createPage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.edit)
        val input = EditText(this)
        input.id = R.id.pageTitle
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            val page = TitledPage(input.text.toString())
            pages.add(page)
            updateAdapter()
            save()
        }
        builder.show()
    }

    private fun clear() {
        pages.clear()
        updateAdapter()
        save()
    }

}
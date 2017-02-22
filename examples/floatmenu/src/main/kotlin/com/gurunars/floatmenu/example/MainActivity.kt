package com.gurunars.floatmenu.example

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.gurunars.floatmenu.AnimationListener
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {

    lateinit private var floatingMenu: FloatMenu
    lateinit private var status: TextView

    @State var flag = true

    private fun toggleButtonColor() {
        val color: Int
        val iconColor: Int

        if (!flag) {
            color = R.color.DarkRed
            iconColor = R.color.White
            flag = true
        } else {
            color = R.color.RosyBrown
            iconColor = R.color.Black
            flag = false
        }

        floatingMenu.setOpenIconBgColor(ContextCompat.getColor(this, color))
        floatingMenu.setOpenIconFgColor(ContextCompat.getColor(this, iconColor))
    }

    private fun bind(viewId: Int, value: String) {
        findViewById(viewId).setOnClickListener {
            AlertDialog.Builder(this@MainActivity)
                    .setTitle(value)
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)

        frameLayout {
            lparams(width= matchParent, height= matchParent)
            floatingMenu = floatMenu {
                lparams(width= matchParent, height= matchParent)
                id=R.id.floatingMenu
                setHasOverlay(true)
                setOpenIconFgColor(ContextCompat.getColor(context, R.color.White))
                setOpenIconBgColor(ContextCompat.getColor(context, R.color.DarkRed))
                setCloseIconBgColor(ContextCompat.getColor(context, R.color.White))
                setCloseIconFgColor(ContextCompat.getColor(context, R.color.Black))
                setContentView(
                    context.relativeLayout {
                        lparams(width= matchParent, height= matchParent)
                        textView {
                            text=getString(R.string.appName)
                            id=R.id.textView
                            isClickable = true
                            lparams(width= wrapContent, height= wrapContent) {
                                centerVertically()
                                centerHorizontally()
                            }
                        }
                        status = textView {
                            text=getString(R.string.menuClosed)
                            id=R.id.status
                            isClickable = true
                            lparams(width= wrapContent, height= wrapContent) {
                                below(R.id.textView)
                                centerVertically()
                                centerHorizontally()
                            }
                        }
                    }
                )
                setMenuView(
                    context.scrollView {
                        verticalLayout {
                            gravity=CENTER_HORIZONTAL
                            button {
                                id=R.id.button
                                text=getString(R.string.click_me)
                                lparams(width= wrapContent, height= wrapContent) {
                                    padding = dip(10)
                                }
                            }
                            frameLayout {
                                id=R.id.buttonFrame
                                isClickable = true
                                backgroundColor = ContextCompat.getColor(context, R.color.AliceBlue)
                                lparams(width= wrapContent, height= wrapContent) {
                                    padding = dip(30)
                                    topMargin=dip(10)
                                }
                            }
                        }
                    }
                )
            }
        }

        floatingMenu.setOnCloseListener(object : AnimationListener {
            override fun onStart(projectedDuration: Int) {
                status.setText(R.string.menuClosed)
            }

            override fun onFinish() {

            }

        })

        floatingMenu.setOnOpenListener(object : AnimationListener {
            override fun onStart(projectedDuration: Int) {
                status.setText(R.string.menuOpen)
            }

            override fun onFinish() {

            }

        })

        bind(R.id.textView, "Text Clicked")
        bind(R.id.button, "Button Clicked")
        bind(R.id.buttonFrame, "Button Frame Clicked")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.enableBackground -> {
                floatingMenu.setHasOverlay(true)
            }
            R.id.disableBackground -> {
                floatingMenu.setHasOverlay(false)
            }
            R.id.toggleButtonColor -> {
                toggleButtonColor()
            }
            R.id.openMenu -> {
                floatingMenu.open()
            }
            R.id.closeMenu -> {
                floatingMenu.close()
            }
            R.id.setLeftHanded -> {
                floatingMenu.setLeftHanded(true)
            }
            R.id.setRightHanded -> {
                floatingMenu.setLeftHanded(false)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        StateSaver.saveInstanceState(this, bundle)
    }

}
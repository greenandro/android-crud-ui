package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.Context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList

internal class Model(private val activity: Activity) {

    var items: MutableList<AnimalItem> = ArrayList()
        set(value) {
            field = value
            for (item in value) {
                if (item.id == 0L) {
                    maxItemId++
                    item.id = maxItemId
                } else {
                    maxItemId = Math.max(maxItemId, item.id)
                }
                // At least version 1
                if (item.version == 0) {
                    item.version = 1
                }
            }
            save()
        }
    var maxItemId: Long = 0
        private set
    private var wasInited = false

    init {
        load()
    }

    fun wasInited(): Boolean {
        return wasInited
    }

    private fun load() {
        val preferences = activity.getPreferences(Context.MODE_PRIVATE)
        wasInited = preferences.getBoolean("wasInited", false)
        maxItemId = preferences.getLong("maxItemId", 0)
        items = Gson().fromJson<List<AnimalItem>>(preferences.getString("data", "[]"),
                object : TypeToken<ArrayList<AnimalItem>>() {

                }.type).toMutableList()
    }

    private fun save() {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString("data", Gson().toJson(items))
        editor.putLong("maxItemId", maxItemId)
        editor.putBoolean("wasInited", true)
        editor.apply()
    }

    fun clear() {
        maxItemId = 0
        items.clear()
    }
}

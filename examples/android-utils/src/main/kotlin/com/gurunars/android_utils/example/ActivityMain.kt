package com.gurunars.android_utils.example

import android.graphics.Color
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView

import com.gurunars.android_utils.ui.apply
import com.gurunars.android_utils.ui.ColoredShapeDrawable

import butterknife.ButterKnife

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val set = ButterKnife.findById<View>(this, R.id.set)
        set.background = ColoredShapeDrawable(OvalShape(), Color.YELLOW)
        apply(set, 6)

        val clear = ButterKnife.findById<View>(this, R.id.clear)
        apply(clear, 6)

        apply(ButterKnife.findById<View>(this, R.id.disabled), 6)
    }

}
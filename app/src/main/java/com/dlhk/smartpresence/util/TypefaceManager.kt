package com.dlhk.smartpresence.util

import android.app.Activity
import android.graphics.Typeface
import com.norbsoft.typefacehelper.TypefaceCollection
import com.norbsoft.typefacehelper.TypefaceHelper

class TypefaceManager(
    val activity: Activity
) {
    init {
        setTypeface()
    }

    lateinit var segoe_ui: TypefaceCollection

    fun setTypeface(){
        segoe_ui = TypefaceCollection.Builder()
            .set(Typeface.NORMAL, Typeface.createFromAsset(activity.assets, "fonts/segoeUI.ttf"))
            .set(Typeface.BOLD, Typeface.createFromAsset(activity.assets, "fonts/segoeUIBold.ttf"))
            .set(Typeface.ITALIC, Typeface.createFromAsset(activity.assets, "fonts/seguisb.ttf"))
            .create()

        TypefaceHelper.init(segoe_ui)
        TypefaceHelper.typeface(activity)
    }
}
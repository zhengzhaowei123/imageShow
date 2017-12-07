package com.zzw.anim.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout


/**
 * recyclerview  多张图片里面的一个子控件(可与其他控件组合)
 * Created by zhengzw on 2016/8/26.
 */
open class ImageItem : RelativeLayout {

    var imageView: ImageView? = null
        private set

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView = ImageView(context)
        addView(imageView, lp)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val view = getChildAt(0)
        view.layout(0, 0, width, height)
    }

    fun setImageBitmap(b: Bitmap?) {
        imageView?.setImageBitmap(b)
    }
}

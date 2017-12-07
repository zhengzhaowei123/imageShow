package com.zzw.anim.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView

import com.zzw.anim.util.GlideUtils


/**
 * recyclerview item包含多张图片里面的一张图片控件
 * Created by Pan_ on 2015/2/2.
 */
class CustomImageView : ImageItem {
    private var url: String? = null
    private var mIsAttachedToWindow = false
    private var mIsLoad = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    public override fun onAttachedToWindow() {
        mIsAttachedToWindow = true
        setImageUrl(url)
        super.onAttachedToWindow()
    }

    public override fun onDetachedFromWindow() {
        mIsAttachedToWindow = false
        mIsLoad = false
        setImageBitmap(null)
        super.onDetachedFromWindow()
    }

    /**
     * @param url  缩略图地址
     */
    fun setImageUrl(url: String?) {
        if (!TextUtils.isEmpty(url) && imageView != null) {
            if (mIsLoad && url == this.url) {
                return
            }
            this.url = url
            if (mIsAttachedToWindow) {
                mIsLoad = true
                GlideUtils.loadPlaceHolder(context, url!!, imageView!!)
            }
        }
    }


    fun setScaleType(centerCrop: ImageView.ScaleType) {
        imageView?.scaleType = centerCrop
    }
}

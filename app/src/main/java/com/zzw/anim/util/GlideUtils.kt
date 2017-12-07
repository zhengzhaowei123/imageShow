package com.zzw.anim.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zzw.anim.model.GlideRoundTransform

import com.zzw.anim.R
import com.zzw.anim.callback.ImageLoadCallback

/**
 * glide工具类
 * Created by zhengzw on 2017/9/21.
 */

object GlideUtils {

    fun loadWithListener(context: Context, url: String, view: ImageView, callback: ImageLoadCallback?) {
        Glide.with(context).load(url).listener(object : RequestListener<String, GlideDrawable> {
            override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                callback?.loadFail(e.toString())
                return false
            }

            override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                callback?.loadSuccess()
                return false
            }
        }).dontAnimate().error(R.drawable.imagefail).into(view)
    }

    fun load(context: Context, url: String, view: ImageView, width: Int, height: Int) {
        Glide.with(context).load(url).error(R.drawable.anim2).override(width, height).centerCrop().into(view)
    }

    fun loadPlaceHolder(context: Context, url: String, view: ImageView) {
        Glide.with(context).load(url).placeholder(ColorDrawable(Color.parseColor("#f5f5f5"))).diskCacheStrategy(DiskCacheStrategy.RESULT).into(view)
    }

    fun load(context: Context, url: String, view: ImageView) {
        Glide.with(context).load(url).error(R.drawable.anim2).into(view)
    }

    fun loadRoundDefault(context: Context, url: String?, defres: Int, round: Int, imageView: ImageView, width: Int, height: Int) {
        if (Utils.isEmpty(url)) {
            Glide.with(context).load(defres).transform(CenterCrop(context), GlideRoundTransform(context, round)).override(width, height).crossFade().into(imageView)
        } else {
            Glide.with(context).load(url).transform(CenterCrop(context), GlideRoundTransform(context, round)).placeholder(defres).override(width, height).crossFade().into(imageView)
        }
    }
}

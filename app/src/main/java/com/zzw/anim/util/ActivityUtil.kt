package com.zzw.anim.util

import android.app.Activity
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.zzw.anim.R
import com.zzw.anim.model.LocationModel

/**
 * activity工具类
 * Created by zhengzw on 2017/12/4.
 */
object ActivityUtil {
    /**
     * 设置上个界面的statusbar,防止返回上个界面出现抖动
     */
    fun initStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            // 获取状态栏高度
            val statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
            val rectView = View(activity)
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
            rectView.layoutParams = params
            //设置状态栏颜色（该颜色根据你的App主题自行更改）
            rectView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
            // 添加矩形View到布局中
            val decorView = activity.window.decorView as ViewGroup
            decorView.addView(rectView)
            val rootView = (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
            rootView.fitsSystemWindows = true
            rootView.clipToPadding = true
        }
    }

    /**
     * 获取view的屏幕位置
     */
    fun getLocation(v: View, location: IntArray? = null): LocationModel {
        val model = LocationModel()
        val loc = location ?: IntArray(2)
        v.getLocationOnScreen(loc)
        model.x = loc[0]
        model.y = loc[1]
        model.width = if (v.width != 0) v.width else v.measuredWidth
        model.height = if (v.height != 0) v.height else v.measuredHeight
        return model
    }
}
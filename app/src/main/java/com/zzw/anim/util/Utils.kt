package com.zzw.anim.util

/**
 * 工具类
 * Created by zhengzw on 2017/11/29.
 */

object Utils {
    /**
     * 获取屏幕宽度

     * @return
     */
    fun getScreenWidth(context: android.content.Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度

     * @return
     */
    fun getScreenHeight(context: android.content.Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun dp2px(context: android.content.Context, dx: Int): Int {
        return (context.resources.displayMetrics.density * dx + 0.5).toInt()
    }

    fun isEmpty(str: String?): Boolean {
        return android.text.TextUtils.isEmpty(str) || "null" == str!!.toLowerCase()
    }
}

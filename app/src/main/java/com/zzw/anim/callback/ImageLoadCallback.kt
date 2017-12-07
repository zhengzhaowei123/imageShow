package com.zzw.anim.callback

/**
 * 图片加载状态类
 * Created by zhengzw on 2017/9/21.
 */

interface ImageLoadCallback {
    fun loadSuccess()

    fun loadFail(e: String)
}

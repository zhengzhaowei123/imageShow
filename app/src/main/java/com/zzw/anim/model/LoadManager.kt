package com.zzw.anim.model

import java.util.ArrayList

/**
 * 图片加载记录管理
 * Created by zhengzw on 2017/11/29.
 */

class LoadManager private constructor() {
    private val mLoadedUrls: MutableList<String>?

    init {
        mLoadedUrls = ArrayList<String>()
    }

    /**
     * 添加到已加载图片列表

     * @param url
     */
    fun addUrl(url: String) {
        if (mLoadedUrls != null && !mLoadedUrls.contains(url)) {
            mLoadedUrls.add(url)
        }
    }

    fun contailUrl(url: String): Boolean {
        return mLoadedUrls != null && mLoadedUrls.contains(url)
    }

    companion object {
        private var mLoadManager: LoadManager? = null

        val instance: LoadManager
            get() {
                if (mLoadManager == null) {
                    synchronized(LoadManager::class.java) {
                        if (mLoadManager == null) {
                            mLoadManager = LoadManager()
                        }
                    }
                }
                return mLoadManager!!
            }
    }
}
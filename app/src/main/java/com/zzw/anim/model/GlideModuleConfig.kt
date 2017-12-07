package com.zzw.anim.model

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.GlideModule

/**
 * glide配置
 * Created by zhengzw on 2017/3/18.
 */
class GlideModuleConfig : GlideModule {

    override fun applyOptions(context: Context, glideBuilder: GlideBuilder) {
        try {
            val MAX_SIZE = 50 * 1024 * 1024 //磁盘缓存最多50M
            glideBuilder.setDiskCache(DiskLruCacheFactory(context.externalCacheDir!!.path, "glide", MAX_SIZE))
            val maxMemory = Runtime.getRuntime().maxMemory().toInt()//获取系统分配给应用的总内存大小
            val memoryCacheSize = maxMemory / 8//设置图片内存缓存占用八分之一
            //设置内存缓存大小
            glideBuilder.setMemoryCache(LruResourceCache(memoryCacheSize))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun registerComponents(context: Context, glide: Glide) {

    }
}

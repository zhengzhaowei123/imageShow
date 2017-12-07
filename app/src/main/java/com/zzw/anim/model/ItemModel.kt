package com.zzw.anim.model

import java.io.Serializable
import java.util.ArrayList

/**
 * 图片详情数据源
 * Created by zhengzw on 2017/9/21.
 */

data class ItemModel(var mImageInfos: List<ImageInfo>?) : Serializable {


    companion object {
        //组合60张图片的数据包括每张图片的长宽（不包含缩略图地址）
        val instance: ItemModel
            get() {
                val mImageInfos = ArrayList<ImageInfo>()
                val builder = StringBuilder(1000)
                //每张图片长宽 intArrayOf(w, h)
                val wh = arrayOf(intArrayOf(800, 600), intArrayOf(400, 280), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 258), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 286), intArrayOf(400, 256), intArrayOf(400, 300), intArrayOf(400, 260), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(400, 286), intArrayOf(400, 286), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(347, 300), intArrayOf(215, 300), intArrayOf(400, 263), intArrayOf(200, 300), intArrayOf(400, 263), intArrayOf(220, 300), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(225, 300), intArrayOf(317, 300), intArrayOf(400, 284), intArrayOf(400, 252), intArrayOf(400, 284), intArrayOf(400, 286), intArrayOf(400, 286), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(233, 300), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 300), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 228), intArrayOf(400, 263), intArrayOf(400, 241))
                var len = 0
                var i = 1
                while (i <= 60) {
                    builder.append(Url.URLS)
                    if (i < 10) {
                        builder.append("0")
                    }
                    builder.append(i)
                    builder.append(".gif")
                    val info = ImageInfo(builder.substring(len), "", wh[i - 1][0], wh[i - 1][1], null)//0,0
                    mImageInfos.add(info)
                    i++
                    len = builder.length
                }
                return ItemModel(mImageInfos)
            }
    }

    override fun toString(): String {
        return "ItemModel(mImageInfos=$mImageInfos)"
    }
}

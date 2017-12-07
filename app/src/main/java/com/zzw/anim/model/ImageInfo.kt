package com.zzw.anim.model

import java.io.Serializable


/**
 * 图片模型
 * Created by zhengzw on 2017/9/21.
 */

data class ImageInfo(var url: String?, //原图地址
                     var thumbUrl: String?, //缩略图地址
                     var width: Int = 0, //原图长度
                     var height: Int = 0, //原图宽度
                     var mLocationModel: LocationModel?) //缩略图所在屏幕位置
    : Serializable

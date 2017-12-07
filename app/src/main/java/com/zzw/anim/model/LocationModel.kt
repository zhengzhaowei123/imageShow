package com.zzw.anim.model

import java.io.Serializable

/**
 * 控件在屏幕的坐标类
 * Created by zhengzw on 2017/10/13.
 */

class LocationModel : Serializable {
    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0

    override fun toString(): String {
        return "LocationModel{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}'
    }
}

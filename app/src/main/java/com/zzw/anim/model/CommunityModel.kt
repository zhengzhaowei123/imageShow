package com.zzw.anim.model

import java.io.Serializable
import java.util.*

/**
 * 社区item模型
 * Created by zhengzw on 2017/9/26.
 */
data class CommunityModel(var TOPICTITLE: String, //问题标题
                          var TOPICTIME: String, //提问时间
                          var USERNAME: String, //提问人名字
                          var USERHEAD: String, //提问人头像
                          var list: ArrayList<ImageInfo>?
) : Serializable

package com.zzw.anim.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.zzw.anim.R
import com.zzw.anim.model.ImageInfo
import com.zzw.anim.util.ActivityUtil
import com.zzw.anim.util.Utils

/**
 * recyclever item包含多张图片 父布局控件
 * Created by Pan_ on 2015/2/2.
 */
class NineGridlayout : ViewGroup {

    private var gap = 0
    private var columns: Int = 0//
    private var rows: Int = 0//
    private var listData: List<ImageInfo>? = null
    private var totalWidth = 0
    private var mItemClick: ItemClick? = null
    private var mGroupPosition: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        //根据布局 手动设置该控件的长度
        totalWidth = Utils.getScreenWidth(context) - Utils.dp2px(context, 80)
        gap = Utils.dp2px(context, 3)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    private fun layoutChildrenView() {
        val childrenCount = listData!!.size
        val singleWidth: Int
        val singleHeight: Int
        if (childrenCount == 1) {
            //1列
            singleWidth = totalWidth
            singleHeight = totalWidth * 9 / 16
        } else if (childrenCount == 2 || childrenCount == 4) {
            //2列
            singleWidth = (totalWidth - gap) / 2
            singleHeight = singleWidth
        } else {
            //3列
            singleWidth = (totalWidth - gap * (3 - 1)) / 3
            singleHeight = singleWidth
        }

        val params = layoutParams
        params.height = singleHeight * rows + gap * (rows - 1)
        layoutParams = params

        for (i in 0..childrenCount - 1) {
            val childrenView = getChildAt(i) as CustomImageView
            childrenView.setTag(R.id.tag_position, i)
            val position = findPosition(i)
            val left = (singleWidth + gap) * position[1]
            val top = (singleHeight + gap) * position[0]
            val right = left + singleWidth
            val bottom = top + singleHeight
            childrenView.layout(left, top, right, bottom)
            childrenView.setImageUrl(listData?.get(i)?.url)
        }

    }


    private fun findPosition(childNum: Int): IntArray {
        val position = IntArray(2)
        for (i in 0..rows - 1) {
            for (j in 0..columns - 1) {
                if (i * columns + j == childNum) {
                    position[0] = i
                    position[1] = j
                    break
                }
            }
        }
        return position
    }


    fun setImagesData(position: Int, lists: List<ImageInfo>?) {
        if (lists == null || lists.isEmpty()) {
            return
        }
        mGroupPosition = position
        generateChildrenLayout(lists.size)
        if (listData == null) {
            var i = 0
            while (i < lists.size) {
                val iv = generateImageView()
                addView(iv, generateDefaultLayoutParams())
                i++
            }
        } else {
            val oldViewCount = listData!!.size
            val newViewCount = lists.size
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount)
            } else if (oldViewCount < newViewCount) {
                for (i in 0..newViewCount - oldViewCount - 1) {
                    val iv = generateImageView()
                    addView(iv, generateDefaultLayoutParams())
                }
            }
        }
        listData = lists
        layoutChildrenView()
    }


    private fun generateChildrenLayout(length: Int) {
        if (length <= 3) {
            rows = 1
            columns = length
        } else if (length <= 6) {
            rows = 2
            columns = 3
            if (length == 4) {
                columns = 2
            }
        } else {
            rows = 3
            columns = 3
        }
    }

    private fun generateImageView(): CustomImageView {
        val iv = CustomImageView(context)
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP)
        iv.setOnClickListener(mOnClickListener)
        return iv
    }

    private val mOnClickListener = View.OnClickListener { v ->
        if (mItemClick != null) {
            //计算九宫格里每个view的位置，以便于动画退出
            val loc = IntArray(2)
            for (i in 0..childCount - 1) {
                val view = getChildAt(i)
                listData?.get(i)?.mLocationModel = ActivityUtil.getLocation(view, loc)
            }
            mItemClick?.onClick(v, mGroupPosition, v.getTag(R.id.tag_position) as Int)
        }
    }

    interface ItemClick {
        fun onClick(view: View, pGroupPosition: Int, itemPosition: Int)
    }

    fun setItemClick(click: ItemClick?) {
        mItemClick = click
    }

}

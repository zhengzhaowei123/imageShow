package com.zzw.anim.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.zzw.anim.R
import com.zzw.anim.util.Utils

/**
 * viewpager圆点控件
 * Created by zhengzw on 2017/12/6.
 */
class DotLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    //上一次viewpager的位置
    private var mLastIndex = 0
    //8dp代表原点资源的长度
    private val mDefaultItemWidth = Utils.dp2px(context, 8)

    /**
     * 初始化dot视图
     */
    fun setData(currentIndex: Int, totalDot: Int, itemWidth: Int = mDefaultItemWidth) {
        if (currentIndex < 0 || totalDot <= 0 || currentIndex >= totalDot || totalDot > 9) return
        mLastIndex = currentIndex
        val padding = itemWidth / 2
        var totalWidth = 0
        for (i in 0..totalDot - 1) {
            val image = ImageView(context)
            image.scaleType = ImageView.ScaleType.CENTER_INSIDE
            image.setImageResource(R.drawable.selector_dot_selected)
            val lp = LayoutParams(itemWidth, itemWidth)
            lp.leftMargin = padding
            //设置为选中状态
            if (i == currentIndex) {
                image.isEnabled = false
            }
            addView(image, lp)
            totalWidth += (itemWidth + padding)
        }
        //显示动画
        ObjectAnimator.ofInt(this, "width", 0, totalWidth).setDuration(250).start()
    }

    /**
     * 更改当前的位置
     */
    fun setCurrentIndex(index: Int) {
        if (childCount <= index) return
        getChildAt(index).isEnabled = false
        getChildAt(mLastIndex).isEnabled = true
        mLastIndex = index
    }

    /**
     * 动画执行
     */
    fun setWidth(width: Int) {
        val lp = layoutParams as FrameLayout.LayoutParams
        lp.width = width
        layoutParams = lp
    }
}
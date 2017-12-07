package com.zzw.anim.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

import com.zzw.anim.util.Utils

import uk.co.senab.photoview.PhotoView

/**
 * 图片详情根布局,监听手指上下移动事件
 * Created by zhengzw on 2017/11/30.
 */

class UpDownRelativeLayout : RelativeLayout {
    private var mDownY: Int = 0
    private var mDownX: Int = 0
    private var mPhotoView: PhotoView? = null
    //原图在屏幕的高度尺寸
    private var mFinalHeight: Int = 0
    //缩略图动画结束前不能移动
    private var mCanMove = false
    private var mPhotoViewMoveListener: PhotoViewMoveListener? = null
    //移动时是否缩放,默认移动缩放
    private var mIsScalMove = true
    //上移是否能退出，默认只能下移动退出
    private var mIsMoveUpFinished = false
    //上下移动的速度
    private val mMoveSpeed = 1.1f

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    constructor(context: Context, attr: AttributeSet, defStyle: Int) : super(context, attr, defStyle)

    /**
     * 设置PhotoView

     * @param view         设置PhotoView
     * *
     * @param mFinalHeight 原图高度
     * *
     * @param listener     移动监听器
     * @param  mIsScalMove 上下移动时是否缩放,默认true移动缩放类似微信 ,false代表不缩放效果类似今日头条
     * @param  mIsMoveUpFinished  上移是否能退出，默认false只能向下移动退出
     */
    fun initData(view: PhotoView, mFinalHeight: Int, listener: PhotoViewMoveListener, mIsScalMove: Boolean = true
                 , mIsMoveUpFinished: Boolean = false) {
        mPhotoView = view
        this.mFinalHeight = mFinalHeight
        mPhotoViewMoveListener = listener
        this.mIsScalMove = mIsScalMove
        this.mIsMoveUpFinished = mIsMoveUpFinished
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.rawX.toInt()
                mDownY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE ->
                //拦截交给onTouchEvent执行移动
                if (ev.pointerCount == 1)
                    return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 是否可以移动图片

     * @param mCanMove =true可以移动
     */
    fun setmCanMove(mCanMove: Boolean) {
        this.mCanMove = mCanMove
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mCanMove) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val mMoveX = event.rawX.toInt()
                val mMoveY = event.rawY.toInt()
                //与上次距离相差3px以上更新位置
                if (mPhotoView != null) {
                    setMarginTop(((mPhotoView!!.layoutParams as LayoutParams).topMargin + mMoveSpeed * (mMoveY - mDownY)).toInt(),
                            ((mPhotoView!!.layoutParams as LayoutParams).leftMargin + mMoveSpeed * (mMoveX - mDownX)).toInt())
                }
                mDownY = mMoveY
                if (mIsScalMove) {
                    mDownX = mMoveX
                }
            }
            MotionEvent.ACTION_UP -> upImage()
        }
        return true
    }

    /**
     * 抬起图片时，如果移动距离大于原图的1/4则退出该界面否则返回到原始位置
     * mIsMoveUpFinished = false，表示下移才可以退出 当topMargin是正数并且大于原图的1/4 才退出
     * mIsMoveUpFinished = true，表示上下移都可以退出， 只要topMargin绝对值大于原图的1/4 才退出
     */
    private fun upImage() {
        val photoLp = mPhotoView?.layoutParams as?  RelativeLayout.LayoutParams ?: return
        if ((if (mIsMoveUpFinished) Math.abs(photoLp.topMargin) else photoLp.topMargin) >= mFinalHeight / 4) {
            //动画退出到上一个界面缩略图的位置
            if (mIsScalMove) {
                photoLp.rightMargin = 0
                mPhotoView?.layoutParams = photoLp
                mPhotoViewMoveListener?.upFinish()
            } else {
                //动画退出到屏幕顶端或者底端
                startOriginAnim(photoLp.topMargin, if (photoLp.topMargin > 0) Utils.getScreenHeight(context)
                else -Utils.getScreenHeight(context))
            }
        } else {
            //原图返回到原始位置
            startOriginAnim(photoLp.topMargin, 0)
        }
    }

    /**
     * 执行松开图片动画

     * @param startTopMargin 开始时TopMargin
     * *
     * @param finalTopMargin 结束时TopMargin
     */
    private fun startOriginAnim(startTopMargin: Int, finalTopMargin: Int) {
        val lp = mPhotoView?.layoutParams as? RelativeLayout.LayoutParams ?: return
        val anim = ValueAnimator.ofFloat(0f, 1f)
        val startLeft = lp.leftMargin
        anim.addUpdateListener { a ->
            val value = a.animatedValue as Float
            setMarginTop((startTopMargin + value * (finalTopMargin - startTopMargin)).toInt(),
                    (startLeft + value * (0 - startLeft)).toInt())
        }
        anim.setDuration(500).start()
    }

    /**
     * 上下移动执行函数

     * @param marginTop 当前marginTop
     */
    fun setMarginTop(marginTop: Int, marginLeft: Int) {
        val photoLp = mPhotoView?.layoutParams as? RelativeLayout.LayoutParams ?: return
        //根据上边距的移动 计算缩放比例以及设置透明度
        var scale = if (mIsMoveUpFinished) (1 - Math.abs(marginTop) * 1.0f / Utils.getScreenHeight(context))
        else if (marginTop < 0) 1f else (1 - marginTop * 1.0f / Utils.getScreenHeight(context))
        if (scale > 1) scale = 1f
        if (scale < 0) scale = 0f
        photoLp.topMargin = marginTop
        //为了移动时保持高度不变。不设置则bottomMargin=0导致图片会放大或者缩小
        if (!mIsScalMove) {
            photoLp.bottomMargin = -photoLp.topMargin
        } else {
            //根据scale计算长宽
            photoLp.width = (Utils.getScreenWidth(context) * scale).toInt()
            photoLp.height = (Utils.getScreenHeight(context) * scale).toInt()
            photoLp.leftMargin = marginLeft
            photoLp.rightMargin = -marginLeft
        }
        mPhotoView?.layoutParams = photoLp
        //说明是退出动画,退出该界面
        if (Math.abs(marginTop) == Utils.getScreenHeight(context)) {
            mPhotoViewMoveListener?.getmActivity()?.finish()
            mPhotoViewMoveListener?.getmActivity()?.overridePendingTransition(0, 0)
        }
        //根据移动量计算alpha值
        mPhotoViewMoveListener?.moveAlpha(scale)
    }

    interface PhotoViewMoveListener {
        //获取当前Activity
        fun getmActivity(): Activity?

        //改变黑色背景透明度
        fun moveAlpha(alpha: Float)

        //手指移动至大于临界位置时松开退出界面
        fun upFinish()
    }
}
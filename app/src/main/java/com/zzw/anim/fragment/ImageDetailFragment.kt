package com.zzw.anim.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout


import com.zzw.anim.R
import com.zzw.anim.activity.DetailActivity
import com.zzw.anim.model.ImageInfo
import com.zzw.anim.model.LoadManager
import com.zzw.anim.util.GlideUtils
import com.zzw.anim.util.Utils
import com.zzw.anim.callback.ImageLoadCallback
import com.zzw.anim.view.UpDownRelativeLayout
import kotlinx.android.synthetic.main.fragment_image_detail.*

import uk.co.senab.photoview.PhotoViewAttacher

/**
 * 图片详情
 * Created by zhengzw on 2017/9/21.
 */

class ImageDetailFragment : Fragment() {

    //原图信息
    private var mImageInfo: ImageInfo? = null
    //加载原图控件
    private var mRootView: View? = null
    //原图的fitCenter后的高度
    private var mFinalHeight: Int = 0
    //是否动画结束了
    private var isAnimStoped = true
    //原图加载是否成功
    private var mLoadSuccess: Boolean = false
    //    private final String TAG = getClass().getName();
    //是否是刚进入的fragment
    private var mIsEnterFragment: Boolean = false
    //默认缩略图大小
    private val mDefaultThumbWidth = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageInfo = arguments.getSerializable("data") as? ImageInfo
        val imageWidth = mImageInfo?.width ?: Utils.getScreenWidth(context)
        val imageHeight = mImageInfo?.height ?: Utils.getScreenHeight(context)
        mIsEnterFragment = arguments.getBoolean("mIsEnterFragment", false)
        //最大不能超过屏幕宽度 , 原图长度>宽度则以屏幕宽度为基准缩放高度，否则原图高度=屏幕高度
        mFinalHeight = Math.min(Utils.getScreenHeight(context), if (imageWidth > imageHeight)
            (Utils.getScreenWidth(context) * 1.0f / imageWidth * imageHeight).toInt()
        else
            Utils.getScreenHeight(context))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater?.inflate(R.layout.fragment_image_detail, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pv_fragment_image_detail.onPhotoTapListener = PhotoViewAttacher.OnPhotoTapListener { _, _, _ -> activity.onBackPressed() }
        pv_fragment_image_detail.onViewTapListener = PhotoViewAttacher.OnViewTapListener { _, _, _ -> activity.onBackPressed() }
        //初始化上下手势的模式
        (mRootView as UpDownRelativeLayout).initData(pv_fragment_image_detail, mFinalHeight, mMoveListener
                , true, false)
        init()
    }

    /**
     * 手指上下移动退出监听
     */
    private val mMoveListener = object : UpDownRelativeLayout.PhotoViewMoveListener {
        override fun upFinish() {
            startThumbAnim(false, false)
            (activity as? DetailActivity)?.startAnimAlpha()
        }

        override fun getmActivity(): Activity {
            return activity
        }

        override fun moveAlpha(alpha: Float) {
            (activity as? DetailActivity)?.setAlpha(alpha)
        }
    }


    /**
     * 加载原图url
     */
    private fun loadImage() {
        GlideUtils.loadWithListener(context, mImageInfo?.url ?: "", pv_fragment_image_detail, mImageLoadCallback)
    }

    /**
     *  加载原图/缩略图图片
     */
    private fun init() {
        //提前加载原图，以便动画结束马上显示
        loadImage()
        //显示上一个界面的缩略图位置
        loadThumb()
        //该Fragment是初始位置的fragment则进行动画进入 如果有原图的尺寸，则可以进行动画
        if (mIsEnterFragment) {
            if (mImageInfo != null && mImageInfo!!.width > 0 && mImageInfo!!.height > 0) {
                startThumbAnim(true)
            } else {
                //如果原图没有尺寸(服务器没有)则不进行动画，加个渐变，防止一进来就显示原图，视觉效果不好
                ObjectAnimator.ofFloat(mRootView, "alpha", 0f, 1f).setDuration(500).start()
                (activity as DetailActivity).onAnimEnd()
            }
        }
    }

    /**
     * 加载缩略图
     *  需要动画时，还原上一个界面时的缩略图位置，准备动画
     *  不需要动画时，缩略图居中显示
     */
    private fun loadThumb() {
        val showLp = iv_second_show.layoutParams as RelativeLayout.LayoutParams
        val mLocationModel = mImageInfo?.mLocationModel ?: (activity as DetailActivity).getEnterLocation()
        showLp.width = if (mLocationModel?.width != null && mLocationModel.width != 0) mLocationModel.width else mDefaultThumbWidth
        showLp.height = if (mLocationModel?.height != null && mLocationModel.height != 0) mLocationModel.height else mDefaultThumbWidth
        //显示上一个界面缩略图位置,默认屏幕居中显示
        showLp.leftMargin = if (mLocationModel?.x != null && mIsEnterFragment &&
                mImageInfo != null && mImageInfo!!.width > 0 && mImageInfo!!.height > 0) mLocationModel.x else (Utils.getScreenWidth(context) - showLp.width) / 2
        showLp.topMargin = if (mLocationModel?.y != null && mIsEnterFragment &&
                mImageInfo != null && mImageInfo!!.width > 0 && mImageInfo!!.height > 0) mLocationModel.y else (Utils.getScreenHeight(context) - showLp.height) / 2
        iv_second_show.layoutParams = showLp

        //原图已经加载过
        if (LoadManager.instance.contailUrl(mImageInfo?.url ?: "")) {
            // 缩略图控件直接加载原图
            GlideUtils.load(context, mImageInfo?.url ?: "", iv_second_show)
            pb_fragment_image_detail.visibility = View.GONE
        } else {
            GlideUtils.load(context, mImageInfo?.url ?: "", iv_second_show, showLp.width, showLp.height)
            pb_fragment_image_detail.visibility = View.VISIBLE
        }

    }

    /**
     * 开始进入或退出动画
     * 进入动画: 如果有上一界面缩略图的位置则从那个位置进行动画，否则从屏幕中间开始动画
     * 退出动画：如果有上一界面缩略图的位置则退出至那个位置 ，否则退回到屏幕中间直至消失
     * @param isShow true=进入动画
     * @param isThumb true代表缩略图进行动画， false代表原图进行动画
     */
    fun startThumbAnim(isShow: Boolean, isThumb: Boolean = true) {
        val showLp = (if (isThumb) iv_second_show else pv_fragment_image_detail).layoutParams as RelativeLayout.LayoutParams
        val mLocationModel = mImageInfo?.mLocationModel
        //显示动画，没有指定长度则为缩略图长度 。 隐藏动画没有指定长度则为0表示一直缩小到0
        val orginalWidth = mLocationModel?.width ?: if (isShow) showLp.width else 0
        //总共移动的长度（从原始位置到屏幕长度）
        val totalWidth = (if (isThumb) Utils.getScreenWidth(context) else showLp.width) - orginalWidth
        //没有指定高度则为0
        val orginalHeight = mLocationModel?.height ?: if (isShow) showLp.height else 0
        //总共移动的高度（从原始位置到图片高度）
        val totalHeight = (if (isThumb) mFinalHeight else showLp.height) - orginalHeight
        //原始左边距,默认图片居中显示
        val orginalLeftMargin = mLocationModel?.x ?: (Utils.getScreenWidth(context) - orginalWidth) / 2
        //总共移动的左边距（从原始位置到0）
        val totalLeftMargin = (if (isThumb) 0 else showLp.leftMargin) - orginalLeftMargin
        //原始上边距，默认图片居中显示
        val orginalTopMargin = mLocationModel?.y ?: (Utils.getScreenHeight(context) - orginalHeight) / 2
        //总共移动的上边距（从原始位置到最终的上边距）
        val totalTopMargin = (if (isThumb) (Utils.getScreenHeight(context) - mFinalHeight) / 2 else showLp.topMargin) - orginalTopMargin
        val animator = ValueAnimator.ofFloat(if (isShow) 0f else 1f, if (isShow) 1f else 0f)
        animator.interpolator = DecelerateInterpolator(2f)
        animator.addUpdateListener { animation ->
            if (iv_second_show != null && pv_fragment_image_detail != null) {
                val value = animation.animatedValue as Float
                showLp.leftMargin = (orginalLeftMargin + value * totalLeftMargin).toInt()
                showLp.topMargin = (orginalTopMargin + value * totalTopMargin).toInt()
                showLp.width = (orginalWidth + value * totalWidth).toInt()
                showLp.height = (orginalHeight + value * totalHeight).toInt()
                (if (isThumb) iv_second_show else pv_fragment_image_detail).layoutParams = showLp
                //退出动画接近结束时，为了与上一个界面缩略图重合，把缩略图ScaleType改成和上一个界面缩略图ScaleType一样，
                if (value < 0.1 && !isShow && mLocationModel != null) {
                    (if (isThumb) iv_second_show else pv_fragment_image_detail).scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isAnimStoped = false
                //动画开始时，隐藏原图view
                if (isThumb) {
                    pv_fragment_image_detail.visibility = View.GONE
                    iv_second_show.visibility = View.VISIBLE
                }
                if (!isShow) {
                    pb_fragment_image_detail.visibility = View.GONE
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                isAnimStoped = true
                if (activity != null) {
                    if (!isShow) {
                        activity.finish()
                        activity.overridePendingTransition(0, 0)
                    } else {
                        //动画结束，显示原图view
                        pv_fragment_image_detail.visibility = View.VISIBLE
                        //动画结束，通知back可以返回退出界面
                        (activity as DetailActivity).onAnimEnd()
                    }
                    //缩略图动画结束后并且原图加载完毕才可移动图片
                    setCanMove()
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animator.setDuration(500).start()
    }

    /**
     * 设置是否可以移动原图
     */
    private fun setCanMove() {
        //动画结束并且原图加载完毕,则可以移动
        if (isAnimStoped && mLoadSuccess) {
            (mRootView as? UpDownRelativeLayout)?.setmCanMove(true)
        }
    }

    private var mImageLoadCallback: ImageLoadCallback? = object : ImageLoadCallback {
        override fun loadFail(e: String) {
            if (iv_second_show != null && pb_fragment_image_detail != null) {
                iv_second_show.visibility = View.GONE
                pb_fragment_image_detail.visibility = View.GONE
            }
        }

        override fun loadSuccess() {
            if (iv_second_show != null && pb_fragment_image_detail != null) {
                iv_second_show.visibility = View.GONE
                pb_fragment_image_detail.visibility = View.GONE
                mLoadSuccess = true
                setCanMove()
                //原图加载完成，添加到记录里
                if (mImageInfo?.url != null) {
                    LoadManager.instance.addUrl(mImageInfo!!.url!!)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mImageLoadCallback = null
        mImageInfo = null
    }

    companion object {

        fun getInstance(info: ImageInfo?, mIsEnterFragment: Boolean): ImageDetailFragment {
            val fragment = ImageDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("data", info)
            bundle.putBoolean("mIsEnterFragment", mIsEnterFragment)
            fragment.arguments = bundle
            return fragment
        }
    }
}

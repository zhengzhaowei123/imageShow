package com.zzw.anim.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager

import com.zzw.anim.R
import com.zzw.anim.model.ItemModel
import com.zzw.anim.adapter.DetailViewPagerAdapter
import com.zzw.anim.fragment.ImageDetailFragment
import kotlinx.android.synthetic.main.activity_detail.*


/**
 * 图片详情界面
 * Created by zhengzw on 2017/9/21.
 */

class DetailActivity : AppCompatActivity() {

    private var mVpAdapter: DetailViewPagerAdapter? = null
        private set
    private var mIsBackPressed: Boolean = false
    //是否能退出
    private var mIsCanBack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_detail)
        init()
    }

    private fun init() {
        //当前图片的位置
        val index = intent.getIntExtra("index", 0)
        //数据源
        val model = intent.getSerializableExtra("data") as? ItemModel ?: return
        mVpAdapter = DetailViewPagerAdapter(supportFragmentManager, model, index)
        vp_detail.adapter = mVpAdapter
        vp_detail.currentItem = index
        vp_detail.addOnPageChangeListener(mPageListener)
        ObjectAnimator.ofFloat(view_detail_bg, "alpha", 0f, 1f).setDuration(500).start()
        llyt_detail_dot.setData(index, model.mImageInfos?.size ?: 0)
    }

    private var mPageListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            llyt_detail_dot.setCurrentIndex(position)
        }

    }

    /**
     * 获取刚进入时的缩略图位置
     */
    fun getEnterLocation() = mVpAdapter?.getmModel()?.mImageInfos?.get(intent.getIntExtra("index", 0))?.mLocationModel

    override fun onBackPressed() {
        //防止点击多次返回键导致重复执行动画
        if (mIsCanBack && !mIsBackPressed) {
            mIsBackPressed = true
            llyt_detail_dot.visibility = View.GONE
            ObjectAnimator.ofFloat(view_detail_bg, "alpha", 1f, 0f).setDuration(500).start()
            (mVpAdapter?.instantiateItem(vp_detail, vp_detail.currentItem) as? ImageDetailFragment)?.startThumbAnim(false)
        }
    }

    fun onAnimEnd() {
        mIsCanBack = true
    }

    /**
     * 根据手指移动距离 设置背景透明度
     */
    fun setAlpha(alpha: Float) {
        view_detail_bg.alpha = alpha
    }

    /**
     * 手指松开后背景隐藏动画
     */
    fun startAnimAlpha() {
        ObjectAnimator.ofFloat(view_detail_bg, "alpha", view_detail_bg.alpha, 0f).setDuration(500).start()
    }
}

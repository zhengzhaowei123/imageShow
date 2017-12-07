package com.zzw.anim.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import com.aspsine.irecyclerview.RefreshTrigger

import com.zzw.anim.R


/**
 * 下拉刷新控件
 * Created by aspsine on 16/3/14.
 */
class ClassicRefreshHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), RefreshTrigger {
    private val ivArrow: ImageView

    private val ivSuccess: ImageView

    private val tvRefresh: TextView

    private val progressBar: ProgressBar

    private val rotateUp: Animation

    private val rotateDown: Animation

    private var rotated = false

    private var mHeight: Int = 0

    init {

        View.inflate(context, R.layout.layout_irecyclerview_classic_refresh_header_view, this)

        tvRefresh = findViewById(R.id.tvRefresh) as TextView

        ivArrow = findViewById(R.id.ivArrow) as ImageView

        ivSuccess = findViewById(R.id.ivSuccess) as ImageView

        progressBar = findViewById(R.id.progressbar) as ProgressBar

        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up)

        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down)
    }

    override fun onStart(automatic: Boolean, headerHeight: Int, finalHeight: Int) {
        this.mHeight = headerHeight
        progressBar.isIndeterminate = false
    }

    override fun onMove(isComplete: Boolean, automatic: Boolean, moved: Int) {
        if (!isComplete) {
            ivArrow.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            ivSuccess.visibility = View.GONE
            if (moved <= mHeight) {
                if (rotated) {
                    ivArrow.clearAnimation()
                    ivArrow.startAnimation(rotateDown)
                    rotated = false
                }

                tvRefresh.text = resources.getString(R.string.xlistview_header_hint_normal)
            } else {
                tvRefresh.text = resources.getString(R.string.xlistview_header_hint_ready)
                if (!rotated) {
                    ivArrow.clearAnimation()
                    ivArrow.startAnimation(rotateUp)
                    rotated = true
                }
            }
        }
    }

    override fun onRefresh() {
        ivSuccess.visibility = View.GONE
        ivArrow.clearAnimation()
        ivArrow.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        tvRefresh.text = resources.getString(R.string.xlistview_header_hint_loading)
    }

    override fun onRelease() {

    }

    override fun onComplete() {
        rotated = false
        ivSuccess.visibility = View.VISIBLE
        ivArrow.clearAnimation()
        ivArrow.visibility = View.GONE
        progressBar.visibility = View.GONE
        tvRefresh.text = resources.getString(R.string.xlistview_header_hint_completed)
    }

    override fun onReset() {
        rotated = false
        ivSuccess.visibility = View.GONE
        ivArrow.clearAnimation()
        ivArrow.visibility = View.GONE
        progressBar.visibility = View.GONE
    }
}

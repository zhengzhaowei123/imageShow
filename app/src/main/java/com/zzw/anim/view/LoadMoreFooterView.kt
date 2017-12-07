package com.zzw.anim.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

import com.zzw.anim.R


/**
 * 上拉加载更多控件
 * Created by aspsine on 16/3/14.
 */
class LoadMoreFooterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var status: Status? = null
        set(status) {
            field = status
            change()
        }

    private val mLoadingView: View

    private val mErrorView: View

    private val mTheEndView: View

    private var mOnRetryListener: OnRetryListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_irecyclerview_load_more_footer_view, this, true)

        mLoadingView = findViewById(R.id.loadingView)
        mErrorView = findViewById(R.id.errorView)
        mTheEndView = findViewById(R.id.theEndView)

        mErrorView.setOnClickListener {
            if (mOnRetryListener != null) {
                mOnRetryListener!!.onRetry(this@LoadMoreFooterView)
            }
        }

        status = Status.GONE
    }

    fun setOnRetryListener(listener: OnRetryListener) {
        this.mOnRetryListener = listener
    }

    fun canLoadMore(): Boolean {
        return status == Status.GONE || status == Status.ERROR
    }

    private fun change() {
        when (status) {
            LoadMoreFooterView.Status.GONE -> {
                mLoadingView.visibility = View.GONE
                mErrorView.visibility = View.GONE
                mTheEndView.visibility = View.GONE
            }

            LoadMoreFooterView.Status.LOADING -> {
                mLoadingView.visibility = View.VISIBLE
                mErrorView.visibility = View.GONE
                mTheEndView.visibility = View.GONE
            }

            LoadMoreFooterView.Status.ERROR -> {
                mLoadingView.visibility = View.GONE
                mErrorView.visibility = View.VISIBLE
                mTheEndView.visibility = View.GONE
            }

            LoadMoreFooterView.Status.THE_END -> {
                mLoadingView.visibility = View.GONE
                mErrorView.visibility = View.GONE
                mTheEndView.visibility = View.VISIBLE
            }
        }
    }

    enum class Status {
        GONE, LOADING, ERROR, THE_END
    }

    interface OnRetryListener {
        fun onRetry(view: LoadMoreFooterView)
    }

}

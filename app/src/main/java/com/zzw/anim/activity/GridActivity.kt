package com.zzw.anim.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View

import com.aspsine.irecyclerview.OnLoadMoreListener
import com.aspsine.irecyclerview.OnRefreshListener

import com.zzw.anim.R
import com.zzw.anim.adapter.GridAdapter
import com.zzw.anim.model.ItemModel
import com.zzw.anim.util.ActivityUtil
import kotlinx.android.synthetic.main.activity_first.*


/**
 * 网格列表界面
 * Created by zhengzw on 2017/9/21.
 */

class GridActivity : AppCompatActivity(), OnRefreshListener, OnLoadMoreListener {
    private var mGridAdapter: GridAdapter? = null
    private val MIN_NUM = 3
    private var NUM = MIN_NUM
    //记录当前的位置，回来时把位置清空
    private var mCurrentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        ActivityUtil.initStatusBar(this)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
        irv_first.setOnRefreshListener(this)
        irv_first.setOnLoadMoreListener(this)
        //横屏显示5列
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            NUM = 5
        }
        irv_first.layoutManager = GridLayoutManager(this, NUM)
        irv_first.setHasFixedSize(true)
        mGridAdapter = GridAdapter(resources.displayMetrics.widthPixels / NUM - resources.getDimensionPixelOffset(R.dimen.common_20dp),
                View.OnClickListener { v ->
                    //由于gridview view太多，只设置点击view的初始位置
                    val position = v.getTag(R.id.tag) as Int
                    mCurrentPosition = position
                    mGridAdapter?.getmModel()?.mImageInfos?.get(position)?.mLocationModel = ActivityUtil.getLocation(v)
                    startActivity(Intent(this@GridActivity, DetailActivity::class.java).putExtra("index", position)
                            .putExtra("data", mGridAdapter?.getmModel()))
                    overridePendingTransition(0, 0)
                })
        irv_first.iAdapter = mGridAdapter
        getData()
    }

    /**
     * 获取本地数据
     */
    private fun getData() {
        mGridAdapter?.setData(ItemModel.instance)
    }

    override fun onRefresh() {
        irv_first.postDelayed(mRefreshRunnable, 300)
    }

    private val mRefreshRunnable = Runnable { irv_first.setRefreshing(false) }

    override fun onLoadMore() {

    }

    override fun onResume() {
        super.onResume()
        mGridAdapter?.getmModel()?.mImageInfos?.get(mCurrentPosition)?.mLocationModel = null
    }
}

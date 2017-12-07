package com.zzw.anim.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aspsine.irecyclerview.OnLoadMoreListener
import com.aspsine.irecyclerview.OnRefreshListener
import com.zzw.anim.R
import com.zzw.anim.adapter.MixAdapter
import com.zzw.anim.model.ImageInfo
import com.zzw.anim.model.ItemModel
import com.zzw.anim.util.ActivityUtil
import com.zzw.anim.view.LoadMoreFooterView
import kotlinx.android.synthetic.main.activity_first.*
import java.util.ArrayList

/**
 * 九宫格界面
 * Created by zhengzw on 2017/12/1.
 */
class MixActivity : AppCompatActivity(), OnRefreshListener, OnLoadMoreListener {

    private var adapter: MixAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        ActivityUtil.initStatusBar(this)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
        irv_first.layoutManager = LinearLayoutManager(this)
        irv_first.setOnRefreshListener(this)
        irv_first.setOnLoadMoreListener(this)
        adapter = MixAdapter(object : MixAdapter.ItemClick {
            override fun onImageClick(v: View, info: ArrayList<ImageInfo>?, position: Int) {
                startActivity(Intent(this@MixActivity, DetailActivity::class.java).putExtra("index", position)
                        .putExtra("data", ItemModel(info)))
                overridePendingTransition(0, 0)
            }
        }, applicationContext)
        irv_first.iAdapter = adapter
        mLoadMoreRunnable.run()
    }

    override fun onRefresh() {
        irv_first.postDelayed(mRefreshRunnable, 300)
    }

    override fun onLoadMore() {
        (irv_first.loadMoreFooterView as LoadMoreFooterView).status = LoadMoreFooterView.Status.LOADING
        irv_first.postDelayed(mLoadMoreRunnable, 500)
    }

    private val mLoadMoreRunnable = Runnable {
        if (adapter != null) {
            (irv_first.loadMoreFooterView as LoadMoreFooterView).status = LoadMoreFooterView.Status.GONE
            adapter?.setData(adapter!!.getTest())
        }
    }
    private val mRefreshRunnable = Runnable { irv_first.setRefreshing(false) }

}
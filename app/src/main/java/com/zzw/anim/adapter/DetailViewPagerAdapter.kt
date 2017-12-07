package com.zzw.anim.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.zzw.anim.model.ItemModel
import com.zzw.anim.fragment.ImageDetailFragment


/**
 * 图片详情列表viewpager适配器
 * Created by zhengzw on 2017/9/21.
 */

class DetailViewPagerAdapter(fm: FragmentManager, private val mModel: ItemModel?, private val mEnterPosition: Int) : FragmentPagerAdapter(fm) {

    fun getmModel() = mModel
    override fun getItem(position: Int): Fragment {
        return ImageDetailFragment.getInstance(mModel?.mImageInfos?.get(position), position == mEnterPosition)
    }

    override fun getCount(): Int {
        if (mModel == null || mModel.mImageInfos == null) return 0
        return mModel.mImageInfos!!.size
    }
}

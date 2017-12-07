package com.zzw.anim.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import com.zzw.anim.R
import com.zzw.anim.model.ItemModel
import com.zzw.anim.util.GlideUtils

/**
 * grid适配器
 * Created by zhengzw on 2017/9/21.
 */

class GridAdapter(private val mImageWidth: Int, private val mClickListener: View.OnClickListener?) : RecyclerView.Adapter<GridAdapter.ItemHolder>() {

    private var mModel: ItemModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (holder.imageView.layoutParams.width != mImageWidth) {
            val lp = holder.imageView.layoutParams as FrameLayout.LayoutParams
            lp.width = mImageWidth
            lp.height = mImageWidth
            holder.imageView.layoutParams = lp
        }
        GlideUtils.load(holder.itemView.context, mModel!!.mImageInfos!![position].url!!, holder.imageView, mImageWidth, mImageWidth)
        holder.imageView.setTag(R.id.tag, position)
        if (mClickListener != null)
            holder.imageView.setOnClickListener(mClickListener)
    }

    override fun getItemCount(): Int {
        if (mModel == null || mModel!!.mImageInfos == null) return 0
        return mModel!!.mImageInfos!!.size
    }

    fun setData(instance: ItemModel) {
        mModel = instance
        notifyDataSetChanged()
    }

    fun getmModel(): ItemModel? {
        return mModel
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById(R.id.iv_item_grid) as ImageView
    }
}

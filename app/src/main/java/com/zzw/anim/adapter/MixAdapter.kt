package com.zzw.anim.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zzw.anim.R
import com.zzw.anim.model.CommunityModel
import com.zzw.anim.model.ImageInfo
import com.zzw.anim.util.GlideUtils
import com.zzw.anim.util.Utils
import com.zzw.anim.view.NineGridlayout
import java.util.*

/**
 * 九宫格适配器
 * Created by zhengzw on 2017/12/1.
 */
class MixAdapter(var click: ItemClick, context: Context) : RecyclerView.Adapter<MixAdapter.Holder>() {
    var mData: ArrayList<CommunityModel>? = null
    private val WIDTH = Utils.dp2px(context, 40)

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        if (holder is Holder) {
            GlideUtils.loadRoundDefault(holder.itemView.context!!, mData!![position].USERHEAD, R.drawable.icon_user_head,
                    WIDTH / 2, holder.mHeadIv, WIDTH, WIDTH)
            holder.mTitleTv.text = mData!![position].TOPICTITLE
            holder.mNameTv.text = mData!![position].USERNAME
            holder.mTimeTv.text = mData!![position].TOPICTIME
            if (mData!![position].list == null || mData!![position].list!!.isEmpty()) {
                holder.mNineLayout.visibility = View.GONE
            } else {
                holder.mNineLayout.visibility = View.VISIBLE
                holder.mNineLayout.setImagesData(position, mData!![position].list)
            }
            holder.mNineLayout.setItemClick(mNineClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_exun_community, parent, false))

    private var mNineClickListener: NineGridlayout.ItemClick? = object : NineGridlayout.ItemClick {
        override fun onClick(view: View, pGroupPosition: Int, itemPosition: Int) {
            click.onImageClick(view, mData!![pGroupPosition].list, itemPosition)
        }
    }

    /**
     * 加载数据
     */
    fun setData(data: ArrayList<CommunityModel>) {
        if (mData == null) {
            mData = data
            notifyDataSetChanged()
        } else {
            val size = mData?.size ?: 0
            mData?.addAll(data)
            notifyItemRangeInserted(size, mData!!.size - size)
        }
    }

    /**
     * 测试数据
     */
    fun getTest(): ArrayList<CommunityModel> {
        val title = arrayOf("云镝告诉你，丝绸之路的伟大进程中，每个人都在牺牲",
                "生命的轻与重 -------- 评《复仇在我》和《楢山节考》"
                , "1500多张电影截图等你来欣赏，日后也会持续更新的！",
                "《最高逼格电影推荐大比拼》", "圆珠笔素描影视剧中一些经典角色")
        val URLS = "http://www.86ps.com/imgWeb/psd/hf_gd/GD_0"
        val names = arrayOf("风一样的男人", "天地匆匆", "一夜一哭楼", "满地跑", "凤凰楼")
        val wh = arrayOf(intArrayOf(800, 600), intArrayOf(400, 280), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 258), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 286), intArrayOf(400, 256), intArrayOf(400, 300), intArrayOf(400, 260), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(300, 300), intArrayOf(400, 286), intArrayOf(400, 286), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(347, 300), intArrayOf(215, 300), intArrayOf(400, 263), intArrayOf(200, 300), intArrayOf(400, 263), intArrayOf(220, 300), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(225, 300), intArrayOf(317, 300), intArrayOf(400, 284), intArrayOf(400, 252), intArrayOf(400, 284), intArrayOf(400, 286), intArrayOf(400, 286), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(233, 300), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 300), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 263), intArrayOf(400, 228), intArrayOf(400, 263), intArrayOf(400, 241))

        val data = ArrayList<CommunityModel>()
        for (i in 1..5) {
            val list: ArrayList<ImageInfo>?
            list = (10 * i..10 * i + i).mapTo(ArrayList<ImageInfo>()) { ImageInfo(URLS + it + ".jpg", "", wh[it - 1][0], wh[it - 1][1], null) } //初始化原图尺寸,默认0
            data.add(CommunityModel(title[i % 4], if (i % 2 == 0) "09:00" else "9月30日", names[i % 4], "", list))
        }
        return data
    }

    override fun getItemCount() = mData?.size ?: 0
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val mTitleTv = view.findViewById(R.id.tv_item_exun_community_title) as TextView
        val mNineLayout = view.findViewById(R.id.nglyt_item_exun_community) as NineGridlayout
        val mNameTv = view.findViewById(R.id.tv_item_exun_community_name) as TextView
        val mTimeTv = view.findViewById(R.id.tv_item_exun_community_time) as TextView
        val mHeadIv = view.findViewById(R.id.iv_item_exun_community_head) as ImageView
    }

    interface ItemClick {
        fun onImageClick(v: View, info: ArrayList<ImageInfo>?, position: Int)
    }
}
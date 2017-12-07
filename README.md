# imageShow
图片浏览器，动画展示大图，支持手势移动缩放退出防微信效果，以及支持移动不缩放退出防今日头条效果

# 应用截图
![app](https://user-images.githubusercontent.com/34303048/33700480-852675a2-db54-11e7-8221-6e023b64dd89.gif)

# 使用
# ImageInfo表示一张图片的信息
data class ImageInfo(var url: String?, //原图地址
                     var thumbUrl: String?, //缩略图地址
                     var width: Int = 0, //原图长度
                     var height: Int = 0, //原图宽度
                     var mLocationModel: LocationModel?) //缩略图所在屏幕位置
                     
# 动画之前，要先设置mLocationModel才能平滑的动画，否则从屏幕中间开始动画

1. 初始化单个缩略图屏幕位置
mImageInfo.mLocationModel = ActivityUtil.getLocation(v)

2. 初始化多个缩略图屏幕位置
  for (i in 0..childCount - 1) {
       val view = getChildAt(i)
       listData?.get(i)?.mLocationModel = ActivityUtil.getLocation(view, loc)
     }
     
# 跳转到详情页
 startActivity(Intent(this, DetailActivity::class.java).putExtra("index", position)
                            .putExtra("data", mGridAdapter?.getmModel()))
                    overridePendingTransition(0, 0)
     

# imageShow
图片浏览器，动画展示大图，支持手势移动缩放退出防微信效果，以及支持移动不缩放退出防今日头条效果

# 应用截图
![app](https://user-images.githubusercontent.com/34303048/33700480-852675a2-db54-11e7-8221-6e023b64dd89.gif)

# 使用
# ImageInfo表示一张图片的信息
```javascript
data class ImageInfo(var url: String?, //原图地址
                     var thumbUrl: String?, //缩略图地址
                     var width: Int = 0, //原图长度
                     var height: Int = 0, //原图宽度
                     var mLocationModel: LocationModel?) //缩略图所在屏幕位置
```
# 动画之前，要先设置mLocationModel才能平滑的动画，否则从屏幕中间开始动画

1. 初始化单个缩略图屏幕位置
```javascript
mImageInfo.mLocationModel = ActivityUtil.getLocation(v)
``` 
2. 初始化多个缩略图屏幕位置
```javascript
  for (i in 0..childCount - 1) {
       val view = getChildAt(i)
       listData?.get(i)?.mLocationModel = ActivityUtil.getLocation(view, loc)
     }
```      
# 跳转到详情页
```javascript
 startActivity(Intent(this, DetailActivity::class.java).putExtra("index", position)
                            .putExtra("data", ItemModel实例))
                    overridePendingTransition(0, 0)
```      
# 手势控件 
在ImageDetailFragment中加载UpDownRelativeLayout控件，并初始化动作
```javascript
  /**
     * @param view         设置PhotoView
     * *
     * @param mFinalHeight 原图高度
     * *
     * @param listener     移动监听器
     * @param  mIsScalMove 上下移动时是否缩放,默认true移动缩放类似微信 ,false代表不缩放效果类似今日头条
     * @param  mIsMoveUpFinished  上移是否能退出，默认false只能向下移动退出
     */
 fun initData(view: PhotoView, mFinalHeight: Int, listener: PhotoViewMoveListener, mIsScalMove: Boolean = true
                 , mIsMoveUpFinished: Boolean = false)
  ``` 
# 缩略图进入退出动画
 ```javascript                
     /**
     * 开始进入或退出动画
     * 进入动画: 如果有上一界面缩略图的位置则从那个位置进行动画，否则从屏幕中间开始动画
     * 退出动画：如果有上一界面缩略图的位置则退出至那个位置 ，否则退回到屏幕中间直至消失
     * @param isShow true=进入动画，
     * @param isThumb true代表缩略图进行动画， false代表原图进行动画
     */
    fun startThumbAnim(isShow: Boolean, isThumb: Boolean = true) {}
``` 

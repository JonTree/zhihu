# 知乎Pure
******
[启动页面]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-22-35-801_com.example.tr.png
[News界面]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-22-47-567_com.example.tr.png
[话题界面]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-22-50-605_com.example.tr.png
[热榜界面]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-23-04-494_com.example.tr.png
[栏目具体]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-23-14-877_com.example.tr.png
[评论加载未完成]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-23-17-876_com.example.tr.png
[评论加载完成]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-23-29-678_com.example.tr.png
[未点击具体评论]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-24-26-818_com.example.tr.png
[Web加载完成]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-18-23-27-068_com.example.tr.png
[评论赞按钮]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-22-09-23-928_com.example.tr.png
[顶部查询]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-22-09-53-808_com.example.tr.png
[ViewFlipper]:https://github.com/TreeWhoAmI/zhihu/blob/master/image/Screenshot_2019-02-25-23-38-39-335_com.example.tr.png
### 启动页面
*******
* 因为学长给的知乎Api里面，启动页面的Api似乎不能用，本来我有想过自己去找Api来替代，最后效果并不是很好，所以最后还是舍弃了，决定的简约纯净的App还是特别让人舒心的，后来在开发途中发现了如果把这个我自定义的ProgressBar如果放在这感觉还是很好的，最终效果图就是这样子
> ![启动页面]
### 主界面
* 进入应用内部后就是这个样子，其实无论是页面还是图标我都是直接从现在的知乎模仿过来的，所以图标方面基本不犯愁，甚至最开始我把整个现在知乎的界面全部布局实现了出来后来等完成之后，一看Api貌似并不多，而且寒假也马上就要完了，就只好挨个实现Api最后删除多余的布局，Activity，最后就成了这个样子
> ![News界面]
* 中间这个ViewPager是代码量最多的，你也许想不到，我感觉最让人难受的是顶部的这个查询按钮，其实最开始我是不准备，因为这个日期方面，要检查日期格式方面是比较难受的，最后用SimpleDateFormat的实例，并先调用这个方法setLenient（）使不自动纠正日期的不对，然后用parse（）来分析，格式不对自然就扔出错误，当然，这个方法来源于百度，然后还有其他一些比较简单的逻辑判断来使覆盖用户比较多变的输入，所以这个地方是我if else 出现得最多的一个地方
> ![顶部查询]
然后就是中间这个Top_story的显示，是使用的ViewFlipper来实现的，这里不能左右滑动，本来我是写了的，但是后来因为发现滑动ViewFlipper时ViewPager也会滑动，体验并不好，所以直接用了按钮来替代，并且稍微增大了上下页按钮的占布局比例，以免不小心点击进入新闻详情，并且，每个ViewFlipper页面当中其实还镶嵌了一个ViewFlipper用来我从短评中挑选的长度合适的评论，来滚动显示。这也是看到知乎上这种显示有的灵感。
![ViewFlipper]
左右滑动分别是，栏目和热门新闻
![话题界面]  ![热榜界面]

# Reader
一个小说阅读器，组件化项目，使用jsoup抓取网站数据


1.完成aac架构（room + viewModel + dataBinding + lifeCycle）

2.完成组件化（包括ARouter跳转，资源文件的合并等等）

3.使用autoSize进行屏幕适配

4.使用适配器模式实现全局通用的Loading(仿照 齐翊 https://juejin.im/post/5c9649145188252d665f5229)

5.BaseActivity中使用Toolbar实现全局通用的标题栏

6.search组件实现了通过书名关键字搜索书籍，并展示到RecyclerView中

7.实现了搜索历史功能，使用room数据库存储历史记录

8.6和7是一个使用aac架构的完整例子

9.所有数据需要通过repository来处理

10.main需要修改，目前只有ARouter跳转到search的功能

11.http组件暂时未用到（内部实现可能有问题，还未测试过）

12.parse组件中只有search功能现在是测试过的

13.common组件中添加了一些工具类





##Android开发社交软件

没想到自己在GitHub上的处女作是献给之前同样没接触过的Android。希望小组成员对于我乱乱的代码风格见谅，我会尽力打上注释der~

###所用资源的详细说明
bilibili:https://www.bilibili.com/video/av63406977?p=38
基本是完全按照他说的搭的框架

框架是Android+环信即时云服务器+sqlite
服务器账号我到时会共享，有时候服务器反应慢会报用户名密码错误，请放心，真不是我代码的错误
同时sqlite下载的文件我也会分享，目前本地还没用到（以后估计会用），基本是为了和环信服务器交互使用的。

Android studio版本：3.6
Android SDK版本：API28
请务必调对，否则报错爆到烦死你。在sdk manager里可以全局修改。其余错误请自行百度。
下载的时候注意不要把Android studio下到C盘，会爆炸。之后我会补上如果C盘上还是有Android的东西，移动哪些文件。壕们当我没说，自行使用C盘。

其他如果还有什么问题可以和我对线~~

###功能简介
1.登录注册（√）
2.联系人和群，包括增删改查（√）
3.会话，包括群和联系人（√）
4.朋友圈，包括封面的更换
5.设置，包括对信息的修改
 
###使用说明
基本同微信，个别页面有区别。注册名字必须唯一，名字和ID相同因此不能有特殊符号

###目前存在的问题
1.在多个终端间互相发送好友邀请正常，但是单个终端发送完后切换账号就接受不到邀请了，反而是将邀请又发给了邀请人
2.群组里进入删除模式后，本应点击空白就能返回到正常的群详情页面，但是现在没有加减号


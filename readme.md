##流程控件##
**如何使用**

	将要使用该library的项目的build.gradle（module project）文件做如下修改：

	{
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	并将build.gradle（module app）的依赖配置做如下修改：
	dependencies {
	        implementation 'com.github.LiKunTao:ProgressView:v1.0.1'
	}

**如何运行demo**
	
	将demo下载后找到build.gradle（module app）文件，将
	apply plugin: 'com.android.application'
	和
	applicationId "com.lkt.pview.progerssveiw"的注释放开，
	并将apply plugin: 'com.android.library'注释掉，即可运行。


**随便说说**

	最近在项目中有一个关于工作流程的需求，前期需求是流程节点固定12个，所以在显示流程节点时根据返回的节点的数量用代码动态控制显示节点的数量，此时对应的activity中关于流程节点的代码变得非常复杂，代码多但是都是成员变量，维护非常困难。后期需求变动，被拒绝的审批流程回到上一个节点，此时还是12个节点，但是部分节点会重复出现，导致原来的实现思路作废。

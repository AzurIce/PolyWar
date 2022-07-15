# PolyWar-Draft

## 小学期项目说明（初稿）

> 2022/06/27 11:32
> 陈旭东
> 自选项目，独立完成：
> 采用Java语言，实现一个基于网络、多线程的图形用户界面的游戏。

1. 基本功能：70%
   i. 软件分层结构（分包组织，不同层的类定义在不同的包中） 5分
   ii. 代码规范性 5分
   iii. 程序基本功能（含代码检查）60分
   （1） 采用技术：GUI, Exception, Thread, network, I/O stream, Collection
   （2） 功能演示 （卡死/闪退/重启:扣5）
   （3） GUI（含菜单）
   （4） 多线程+网络交互
   （5） 数据存储：文件或数据库

2. 扩展功能：30%
   单项10分，含代码检查。基本功能扣分可以从扩展功能补。
   i. 完整的工具栏+状态栏，各5分
   ii. 统计图
   iii. 使用数据库存储相关信息
   iv. 视频 / 合理音频和音效
   v. 文档处理，如PDF、Excel、Word等
   vi. Android/鸿蒙/iOS App
   vii. 其它（认定标准：功能实用+有一定代码量支撑）

3. 交付物要求
   a) 开题报告（项目描述、功能点、总体技术方案）
   b) 日报，按天提交
   c) 结题报告（需求、设计：技术方案、实现：类图、用户手册、项目总结）
   d) 源代码、可运行程序环境（含必要的资源文件）、运行录屏（小于50M，过大视频可以用"格式工厂"等相关软件转码后再提交）：验收答辩后提交，未提交会没有答辩成绩。

---

## PolyWar

类似坦克大战的 C/S 模式多人联机对战游戏。

### 一、游戏流程：

创建房间，加入房间，房主开始游戏。

玩家初始随机生成在地图中，地图中存在障碍物、随时间随机刷新的道具、可利用的机关，玩家可以移动、发射子弹，玩家中弹时降低血量，血量降低为0时玩家死亡，一定时间后开始缩圈，在圈外的玩家会受到持续伤害，最后存活的玩家获胜。

### 二、功能点：

#### 基本功能（70pts）

- 功能完成（15pts）✅

  卡死/闪退/重启：-5pts

- GUI（15pts）✅

  含菜单，可以是游戏菜单

- 网络对战（15pts）✅

  多线程 + 网络实时交互

  无实时对战（10pts）

  仅采用点对点的模式（10pts）

- 数据保存（15pts）✅

  文件或数据库

- 代码规范（5pts）✅

- 代码架构（5pts）✅

#### 扩展功能（30pts）

- 部署在云端（5pts）✅
- 多组同时网络对战✅
- 统计图
- 数据库✅
- 音频（5pts）+音效（5pts）/视频✅
- Excel/Word/PDF
- Android/HarmonyOS/IOS 的开发
- 其他，与游戏功能相关，且有一定代码量支撑

- 功能点的代码抽查，无法解释扣5.

![评分](./评分.png)

### 三、开发计划

```mermaid
gantt
    title PolyWar
    dateFormat  YYYY-MM-DD
    section 整体项目
    PolyWar											   :2022-07-04, 11d
    section 立项
    立项           									 :done, a1, 2022-07-04, 1d
    section 客户端
    基础类												:done, basic1_c, after a1, 1d
    玩家移动										   :done, move, after a1, 2d
	碰撞    									    	 :done, basic2_c, after basic1_c, 1d
    摄像机移动										  :done, basic2_c, after basic1_c, 1d
    渲染、逻辑分离										:done, basic3_c, after basic2_c, 1d
    UI封装（窗口类、页面类、控件类以及它们之间的管理） :done, basic3_c, after basic2_c, 1d
    基本的网络连接										:done, basic_network_c, after basic3_c, 1d
    精致完成主界面										:done, ui_main, after basic3_c, 1d
    导弹发射、血量					   					:done, missle, after basic_s, 1d
    对战数据图表										 :data_c_stats, after basic_s, 2d
    回放功能										   :replay, after data_s, 1d
    点缀&优化										   :done, after replay, 1d
    section 服务端
    基本的网络连接										:done, basic_network_s, after basic3_c, 1d
    封包、网络架构										:done, packet, after basic3_c, 2d
    房间												 :done, room, after basic_network_s, 2d
    地图数据										   :done, map_s, after packet, 1d
    游戏												 :done, basic_s, after packet, 1d
    对战数据记录										 :done, data_c, after basic_s, 2d
    对战数据-数据库  									:done, data_s, after basic_s, 2d
    点缀&优化										   :done, after data_s, 2d
    section 功能类
    数学（向量等）										:done, math, 2022-07-04, 2d
    柏林噪声          								   :done, noise, 2022-07-04, 1d
    Marching Squares        					   	   :done, map_generate1, after noise, 1d
    地形生成										   :done, map_generate2, 2022-07-04, 3d
    网络包												:done, packet, after basic3_c, 2d
    
```

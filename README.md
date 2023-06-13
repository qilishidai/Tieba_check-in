
<h1 align="center">
百度贴吧云函数自动签到
</h1>




## 工具简介

百度贴吧自动签到，**2023年6月10日仍然正常使用**。

可以通过阿里云函数，腾讯云函数，Linux服务器，windows部署。每天定时对百度贴吧关注的贴吧进行签到，并推送签到结果到企业微信应用或Server酱。

## 使用说明

### 一、获取运行所需的 Cookies

1. **Star 本项目**
2. 获取 百度Cookies
3. 浏览器打开**无痕模式**后并登录 [百度贴吧](https://tieba.baidu.com/
4. 按 F12 打开 「开发者工具」 找到 应用程序/Application -\> 存储 -\> Cookies
5. 找到 `BDUSS` 项，并复制值，后面需要用到。

![image-20230409213414282](img/image-20230409213414282.png)

### 二、获取推送key

如果不需要推送或者嫌配置麻烦可以跳过这一步，缺点是不能自动将签到结果推送到自己手机。如果需要推送签到结果2.1或者2.2选一个进行操作。推荐2.1.

#### 2.1Server 酱推送

1. 前往 [sct.ftqq.com](https://sct.ftqq.com/sendkey)点击登入，创建账号。

2. 点击点[SendKey](https://sct.ftqq.com/sendkey) ，生成一个 Key 变量名为 

3. [配置消息通道](https://sct.ftqq.com/forward) ，选择方糖服务号，保存即可。

   在这里为了简单，使用方糖服务号推送。server酱并不推荐这个通道，如图。但是这个是最简单的，配置无门槛，需要其它通道推送，可以自行查看[配置消息通道](https://sct.ftqq.com/forward) 的说明。

   ![image-20230610005402480](/img/123)

4. 复制下SendKey，在后面需要使用。![image-20230610005516855](/img/image-20230610005516855.png)



#### 2.2企业微信应用推送

2022年6月20日之后新创建的应用，需要额外配置可信IP。且可信IP不可公用，不再推荐此通道。如果你之前创建的应用，强烈推荐此通道，推送次数无限制。不用点开消息即可查看情况。具体配置方法和获取相关请求数据方法查阅Server 酱通道设置中的企业微信应用推送。查看方法为，进入[配置消息通道](https://sct.ftqq.com/forward) 页面，选择企业微信应用消息通道，在下方就可以看到详细的配置方法了。根据该页面的教程，复制出*企业ID、应用ID/AgentId、推送UID*。后面需要使用



### 三、部署

#### 3.1 使用 阿里云部署（推荐）

##### 1、登录[阿里云函数计算](https://account.aliyun.com/login/login.htm?oauth_callback=https%3A%2F%2Ffcnext.console.aliyun.com%2Foverview&lang=zh)支付宝扫码登录,并创建服务已经函数。

![image-20230409220823849](/img/image-20230409220823849.png)



![image-20230409220910265](/img/image-20230409220910265.png)



![image-20230409220929318](/img/image-20230409220929318.png)



##### 2、选择运行环境

![image-20230610012116937](/img/image-20230610012116937.png)

##### 3、填写函数相关信息

![image-20230610012717285](/img/image-20230610012717285.png)

##### 4、启动命令填写



根据需要选择其中一种输入形式执行以下命令：

#### 方式一：不推送消息，执行以下命令：

```shell
java -jar Tieb_check-in_code/Tieb_check-in.jar BDUSS 
```

其中，BDUSS 需要替换成你的百度账号的 BDUSS 值，多个账号之间使用 # 分割。

#### 方式二：使用 Server 酱推送消息到微信，执行以下命令：

```shell
java -jar Tieb_check-in_code/Tieb_check-in.jar BDUSS SendKey 
```

其中，BDUSS 需要替换成你的百度账号的 BDUSS 值，多个账号之间使用 # 分割。SendKey 需要替换成你在 Server 酱网站上申请的推送密钥。

#### 方式三：使用企业微信应用推送消息，执行以下命令：

```shell
java -jar Tieb_check-in_code/Tieb_check-in.jar BDUSS 企业ID 企业应用secret 企业应用的id 
```

其中，BDUSS 需要替换成你的百度账号的 BDUSS 值，多个账号之间使用 # 分割。企业ID、企业应用secret 和 企业应用的id 需要替换成你的企业微信应用的相关信息。

**注意**

***1.启动命令太长会被阿里云截断，可以通过创建多个云函数或者写一个启动脚本解决***

***2.注意jar文件路径，路径不对会造成文件找不到导致报错，上面路径仅供参考，以实际为准***

————————

##### 5、设置触发器

![image-20230610012949291](/img/image-20230610012949291.png)



#### 3.2 使用 windows平台部署

使用该方式要求电脑每天都使用，并且保证每天电脑有一段时间是开机状态。适合使用电脑上班的人使用。

##### 1、测试是否正常使用

确认已经安装了 Java 运行环境（JRE）

下载了 Tieb_check-in.jar 文件

打开命令行窗口或终端，进入 Tieb_check-in.jar 文件所在目录

根据3.1第4点的方法填写命令，执行。测试是否有问题

执行命令后，程序会自动执行签到，并在控制台输出签到结果。

##### 2、每天定时自动执行

如果需要每天定时执行签到任务，可以使用 Windows 系统的计划任务程序来实现：

此电脑，右键

打开“任务计划程序”，创建一个新任务

![image-20230610013850350](/img/image-20230610013850350.png)

在“触发器”选项卡中，设置每天定时执行任务的时间

在“操作”选项卡中，添加一个新操作，选择“启动程序”，并设置程序路径为 Java 安装路径下的 bin 目录下的java.exe，程序参数为 Tieb_check-in.jar 的绝对路径和需要传入的参数(参考3.1第4点)。

另外也可以写一个启动脚本进行启动

![image-20230610013953579](/img/image-20230610013953579.png)

保存任务后，系统会在每天设定的时间自动执行签到任务。



## 免责声明

1. 本项目仅为编程学习及交流，请与24小时内删除。出现什么纠纷与作者无关。
1. 本工具不会记录你的任何敏感信息，也不会上传到任何服务器上。（例如用户的 cookies 数据，cookies 数据均存在用户自己的设备上）
2. 本工具不会记录任何执行过程中来自贴吧的数据信息
3. 本工具执行过程中产生的日志，仅会在使用者自行配置推送渠道后进行推送。日志中不包含任何用户敏感信息。
4. 如果你使用了第三方修改的，打包的本工具代码，请注意甄别，有可能会对你账号造成损失，同时也不要把自己的敏感信息（账号，cookies等）提供给他人。（**网络安全教育普及任重而道远**）
5. 代码是屎山代码，有机会再重构。

## 致谢

代码参考自[tieba4j](https://github.com/gengwx/tieba4j)感谢你的代码


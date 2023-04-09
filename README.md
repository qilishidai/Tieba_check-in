<div align="center">
<h1 align="center">
百度贴吧云函数自动签到
</h1>



## 工具简介

百度贴吧自动签到。云函数版，**2023年4月9日仍然正常使用**

## 使用说明

### 获取运行所需的 Cookies

1. **Fork 本项目**
2. **获取 百度Cookies**
3. 浏览器打开**无痕模式**后并登录 [百度贴吧](https://tieba.baidu.com/
4. 按 F12 打开 「开发者工具」 找到 应用程序/Application -\> 存储 -\> Cookies
5. 找到 `BDUSS` 项，并复制值，后面需要用到。

![image-20230409213414282](\img\image-20230409213414282.png)


### 一、使用 阿里云部署（推荐）

1、登录[阿里云函数计算](https://account.aliyun.com/login/login.htm?oauth_callback=https%3A%2F%2Ffcnext.console.aliyun.com%2Foverview&lang=zh)支付宝扫码登录

![image-20230409220823849](\img\image-20230409220823849.png)



![image-20230409220910265](\img\image-20230409220910265.png)



![image-20230409220929318](\img\image-20230409220929318.png)

![image-20230409223048929](C:\Users\Hello\AppData\Roaming\Typora\typora-user-images\image-20230409223048929.png)



![image-20230409223239584](C:\Users\Hello\AppData\Roaming\Typora\typora-user-images\image-20230409223239584.png)

![image-20230409223327940](C:\Users\Hello\AppData\Roaming\Typora\typora-user-images\image-20230409223327940.png)

……不想写文档了，过几天再写。后面就是创建一个java环境，然后通过命令行的方式传入参数调用。然后设置触发器，每天自动执行。启动参数参考。貌似阿里云启动命令有长度限制，多账户目前会被截断造成问题，单账号没问题。可以通过创建多个云函数的方式临时解决。

```shell
java -jar Tieb_check-in_code/Tieb_check-in.jar F1dkE4NC1mMHJqYmdMZkJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACQ3MmQkNzJkOW ww414bb eVyXn4eBw5GtOdwQ4 1000002
```



### 二、本地调用 Tieb_check-in.jar 的教程

### 前置条件

确认已经安装了 Java 运行环境（JRE）

下载了 Tieb_check-in.jar 文件

### 步骤

打开命令行窗口或终端，进入 Tieb_check-in.jar 文件所在目录

根据需要选择其中一种输入形式执行以下命令：

#### 方式一：不推送消息，执行以下命令：

Copy code

```shell
java -jar Tieb_check-in.jar BDUSS 
```

其中，BDUSS 需要替换成你的百度账号的 BDUSS 值，多个账号之间使用 # 分割。

#### 方式二：使用 Server 酱推送消息到微信，执行以下命令：

Copy code

```shell
java -jar Tieb_check-in.jar BDUSS SendKey 
```

其中，BDUSS 需要替换成你的百度账号的 BDUSS 值，多个账号之间使用 # 分割。SendKey 需要替换成你在 Server 酱网站上申请的推送密钥。

#### 方式三：使用企业微信应用推送消息，执行以下命令：

```shell
java -jar Tieb_check-in.jar BDUSS 企业ID 企业应用secret 企业应用的id 
```

其中，BDUSS 需要替换成你的百度账号的 BDUSS 值，多个账号之间使用 # 分割。企业ID、企业应用secret 和 企业应用的id 需要替换成你的企业微信应用的相关信息。

执行命令后，程序会自动执行签到，并在控制台输出签到结果。

### 每天定时自动执行

如果需要每天定时执行签到任务，可以使用 Windows 系统的计划任务程序来实现：

此电脑，右键

打开“任务计划程序”，创建一个新任务

在“触发器”选项卡中，设置每天定时执行任务的时间

在“操作”选项卡中，添加一个新操作，选择“启动程序”，并设置程序路径为 Java 安装路径下的 bin 目录，程序参数为 Tieb_check-in.jar 的绝对路径和需要传入的参数

保存任务后，系统会在每天设定的时间自动执行签到任务。



## 订阅执行结果

### Server 酱推送

目前Server 酱推送的消息通道支持以下渠道

- 企业微信应用消息
- Android，
- Bark iOS，
- 企业微信群机器人
- 钉钉群机器人
- 飞书群机器人
- 自定义微信测试号
- 方糖服务号

1. 前往 [sct.ftqq.com](https://sct.ftqq.com/sendkey)点击登入，创建账号。
2. 点击点[SendKey](https://sct.ftqq.com/sendkey) ，生成一个 Key 变量名为 
3. [配置消息通道](https://sct.ftqq.com/forward) ，选择方糖服务号，保存即可。
4. 复制下SendKey，步骤2或者步骤1使用



### 企业微信应用推送

2022年6月20日之后新创建的应用，需要额外配置可信IP。且可信IP不可公用，不再推荐此通道。如果你之前创建的应用，强烈推荐此通道，推送次数无限制。不用点开消息即可查看情况。具体配置方法和获取相关请求数据方法查阅Server 酱通道设置中的企业微信应用推送，过几天再来补全文档。





## 免责声明

1. 本工具不会记录你的任何敏感信息，也不会上传到任何服务器上。（例如用户的 cookies 数据，cookies 数据均存在用户自己的设备上）
2. 本工具不会记录任何执行过程中来自贴吧的数据信息
3. 本工具执行过程中产生的日志，仅会在使用者自行配置推送渠道后进行推送。日志中不包含任何用户敏感信息。
4. 如果你使用了第三方修改的，打包的本工具代码，请注意甄别，有可能会对你账号造成损失，同时也不要把自己的敏感信息（账号，cookies等）提供给他人。（**网络安全教育普及任重而道远**）
5. 代码是屎山代码，有机会再重构。

## 致谢

一部分代码参考自https://github.com/gengwx/tieba4j，然后重写了推送部分。感谢你的代码

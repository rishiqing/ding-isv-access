# 部署指南
1.前提准备
        公网服务器,假设IP地址为127.0.0.1
        TOMCAT端口假设为8080
        MYSQL数据库
        消息中间件MQ
        (工程目前只依赖了 TOMCAT,MYSQL,ACTIVEMQ这三个中间件,如果不满足请自行解决)




2.部署接入工程
      1).下载代码 (代码分析见后续)
        github地址为
        https://github.com/hetaoZhong/ding-isv-common.git    公共方法类库
        https://github.com/hetaoZhong/ding-isv-access.git      开放平台对接工程
        https://github.com/hetaoZhong/ding-isv-app.git      钉钉微应用DEMO(比较简单.仅供参考)
        https://github.com/hetaoZhong/ding-isv-channel.git      钉钉服务窗应用DEMO(即将上传)




      2).打包发布 ding-isv-common 工程至maven仓库
         打包发布 ding-isv-access的api model至maven仓库
         (如果觉得代码曾引入jar包不爽,也可以把ding-isv-access.依赖的本地jar包上传到maven仓库)


      3).新建MYSQL数据库,名称为ding_isv_access。(如果不喜欢这个名称,可自行更换)
          将 ding-isv-access 工程中的 db_sql.sql 文件执行到mysql数据库
          如果发现执行过程中出现 (创建DB和表的过程中出现Unknown character set: 'utf8mb4'),
          请升级mysql版本至>5.5,或者将db_sql.sql文件中的utf8mb4编码更改成utf8

       4).修改ding-isv-access的配置文件,auto-config.xml. 各种配置项的属性看注释自行理解


       5).打包编译 ding-isv-access 。 mvn clean package -Dmaven.test.skip=true
         打包之前看一下个人目录之下antx.properties是否存在，如果存在需要清理一下 rm -rf ~/antx.properties

       6).将打包好的ding-isv-access .war放入tomcat启动。
          访问 http://127.0.0.1:8080/ding-isv-access/checkpreload.htm 返回success 表示启动完毕


3.开发者平台注册套件
    1) 在开发者平台注册套件的时候，填写回调地址http://127.0.0.1:8080/ding-isv-access/suite/create
        在代码中可以发现suite/create该路径。 里面的suitesecret和token可以更换。也可以直接在开发者后台输入代码中的值


    2) 套件注册成功，在db中插入套件信息
         insert into isv_suite(id, gmt_create, gmt_modified, suite_name, suite_key, suite_secret, encoding_aes_key, token, event_receive_url)
         values(1, '2016-03-14 18:08:09', '2016-03-14 18:08:09', 'X套件', 'suitekeyX', 'suitesecretX','suiteAesKeyX', 'suiteTokenX', '');
         或者运行代码中的测试用例插入
    3)数据插入之后,修改套件地址         http://127.0.0.1:8080/ding-isv-access/suite/callback/{suiteKey} 占位符请自行替换

    4)观察服务端log.
          /home/mint/logs/ding-isv-access/isv-crm-access.log               该log记录了tomcat的ding-isv-access工程启动情况
         /home/mint/logs/ding-isv-access/biz/http_request_helper.log  该log记录了所有通过httpclient开放平台请求的http记录
          /home/mint/logs/ding-isv-access/biz/http_invoke.log               该log记录了所有通过sdk开放平台请求的http记录
         /home/mint/logs/ding-isv-access/biz/task.log                             该log记录了所有quzrtz任务日志。包括定时生成suitetoken
         /home/mint/logs/ding-isv-access/biz/suite_callback.log             该log记录了所有开放平台调用套件回调信息的日志
         /home/mint/logs/ding-isv-access/biz/monitor.log                        该log记录了suitetokensuiteticket是否正常接收,正常更新


4.创建微应用
        在开发者后台创建微应用，创建完成之后注意需要在db中插入微应用信息：
        insert into isv_app (id, gmt_create, gmt_modified, suite_key, app_id)
        values (1, now(), now(), 'suiteKeyX', 'xxxx');
        保证app与suite的关联关系，在jsskdapi授权等场景下会根据appid查suite key


5.生成先下部署二维码(该过程要求开发者后台的关联的钉钉企业必须是认证企业)
        在开发者后台生成线下部署二维码,
        第一次生成二维码的时候,建议不要勾选 开启序列号校验选项。
        之后再次生成二维码可以勾选
        生成二维码的时候,钉钉会把套件为指定的一家企业开通,开发者需要观察服务端日志。 


6.二维码开生成成功,可以扫码开通套件啦


7.钉钉微应用简单DEMO
    1)代码比较简单。仅仅实现了dd.config流程。
        可以修改文件中的corpid.授权该套件的企业的corpid即可。 在调试过程中自己修改
       文件地址为https://github.com/hetaoZhong/ding-isv-app/blob/master/web/src/main/webapp/microapp.html
    2)更多demo在更新中... 前端不会,多包含
    
# 修改说明

## 企业授权开通时集成（企业集成）

使用JMS消息队列的方式进行异步集成。

## 用户授权访问时集成（用户集成）

由于需要使用用户集成后的返回数据，因此只能使用同步集成


# 钉钉发布注意事项

## log4j日志文件配置

修改log4j.xml文件名为log4j.dev.xml
修改log4j.prod.xml文件名称为log4j.xml

# 钉钉主程序和job程序发布说明：
## 钉钉主程序

- 注释掉spring-task.xml的quartzScheduler bean，不启动quartz计时器
- 注释掉spring-queue.xml的suitCallbackMessageListener监听器和jmsContainer bean，不配置队列监听器

## 钉钉job程序

- 取消注释spring-task.xml的quartzScheduler bean，启动quartz计时器
- 取消注释spring-queue.xml的suitCallbackMessageListener监听器和jmsContainer bean，配置队列监听器

## 跳过maven的test，直接运行package打包
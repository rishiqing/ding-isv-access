# 问题说明

##  PropertyPlaceholderConfigurer配置无法注入问题
1  使用PropertyPlaceholderConfigurer注入配置时，发现在dataSource中的${jdbc.username}和${jdbc.password}等均无法正常注入。查找原因，是由于myBatis的配置会在解析jdbc.username和jdbc.password之前读取配置。
  链接参考：[http://www.oschina.net/question/188964_32305](http://www.oschina.net/question/188964_32305)
  
  最终，修改了spring-dao.xml文件中的sqlSessionFactory bean的名称为mySqlSessionFactory解决问题。
  一旦使用sqlSessionFactory这个作为bean的id，居然就会出问题，spring也是够了。。。fffffffffuck
  
# 开发流程

## 查看/修改基础配置文件

文件位于%HOME%/ddauth-config.properties

## 启动activemq队列

E:\home_space\apache-activemq-5.14.1\bin\win64\wrapper.exe

## 修改spring-task.xml文件，设置定时任务

启用job
E:\workspace\idea_space\ding-isv-access\biz\src\main\resources\spring-task.xml

## 修改spring-queue.xml文件，启用activemq listener

启用activemq listener
E:\workspace\idea_space\ding-isv-access\biz\src\main\resources\spring-queue.xml

## 修改log4j.xml文件，配置日志路径

E:\workspace\idea_space\ding-isv-access\web\src\main\webapp\WEB-INF\log4j.xml

## 在钉钉开发者后台中配置，确保访问ip在白名单中

## IEDA中启动tomcat7


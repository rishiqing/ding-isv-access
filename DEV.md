# 问题说明

##  PropertyPlaceholderConfigurer配置无法注入问题
1  使用PropertyPlaceholderConfigurer注入配置时，发现在dataSource中的${jdbc.username}和${jdbc.password}等均无法正常注入。查找原因，是由于myBatis的配置会在解析jdbc.username和jdbc.password之前读取配置。
  链接参考：[http://www.oschina.net/question/188964_32305](http://www.oschina.net/question/188964_32305)
  
  最终，修改了spring-dao.xml文件中的sqlSessionFactory bean的名称为mySqlSessionFactory解决问题。
  一旦使用sqlSessionFactory这个作为bean的id，居然就会出问题，spring也是够了。。。fffffffffuck
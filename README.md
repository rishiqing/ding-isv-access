# ding-isv-access


rm -rf ~/antx.properties
mvn clean package -Dmaven.test.skip=true


数据库名称是大写的

# ding-isv-access


步骤一:创建DB
1.不要调整sql文件中创建表的顺序.quartz是有表外检约束的。和quartz相关的table名称都是大写的

创建DB和表的过程中出现,Unknown character set: 'utf8mb4'
1.更新mysql版本至>5.5
2.使用db_sql.utf8.sql




查看监控
cat   /home/mint/logs/ding-isv-access/biz/monitor.log
  "monitor get token faile"




cat   /home/mint/logs/ding-isv-access/biz/task.log



2016-08-13 14:30:00,070 INFO  SuiteTokenGenerateJob.executeInternal:27 - logEvent:开始traceId:820a54bb-d009-450f-bbb9-afdd8a59ee5d		msg:套件TOKEN生成任务执行开始	nextFireTime:Sat Aug 13 15:00:00 CST 2016
2016-08-13 14:30:00,075 ERROR SuiteTokenGenerateJob.executeInternal:36 - logEvent:结束traceId:820a54bb-d009-450f-bbb9-afdd8a59ee5d		msg:查询套件信息失败	errCode:-1	errMsg:系统繁忙




package com.dingtalk.isv.access.web.listener;

import com.dingtalk.isv.access.bizex.dingpush.service.DbAuthCheckService;
import com.dingtalk.isv.common.log.format.LogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.concurrent.*;

/**
 * @author Wallace Mao
 * Date: 2018-10-29 11:49
 */
public class DbAuthCheckApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger bizLogger = LoggerFactory.getLogger("CORP_LOCK_LOGGER");
    private static final Long EXECUTE_INIT_DELAY_MILLS = 300L;
    private static final Long EXECUTE_DELAY_MILLS = 3000L;
    private static boolean isStarted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(!isStarted){
            System.out.println("====db auth check starting...");
            XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext)contextRefreshedEvent.getApplicationContext();
            final DbAuthCheckService dbAuthCheckService = (DbAuthCheckService)xmlWebApplicationContext.getBean("dbAuthCheckService");

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try{
                        dbAuthCheckService.checkDingEvent();
                    } catch (Exception e){
                        bizLogger.warn(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                                "db check 发生错误：" + e.toString()
                        ));
                    }
                }
            }, EXECUTE_INIT_DELAY_MILLS, EXECUTE_DELAY_MILLS, TimeUnit.MILLISECONDS);

            System.out.println("====db auth check started");
            isStarted = true;
        }
    }
}

package com.dingtalk.isv.access.biz.event.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 企业授权成功的mq listener
 * Created by Wallace on 2017/1/7.
 */
public class CorpAuthSuiteListener implements MessageListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("MQ_LISTENER_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(SuiteCallbackListener.class);
    @Override
    public void onMessage(Message message) {
        //TODO 同步更新到日事清服务器

    }
}

package com.dingtalk.isv.rsq.biz.event.mq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wallace on 17-4-26.
 */
public class RsqSyncMessage implements MessageCreator {

    private String suiteKey;
    private String corpId;

    public RsqSyncMessage(String suiteKey, String corpId) {
        this.suiteKey = suiteKey;
        this.corpId = corpId;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        MapMessage message = session.createMapMessage();
        message.setString("suiteKey", this.suiteKey);
        message.setString("corpId", this.corpId);
        return message;
    }

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}

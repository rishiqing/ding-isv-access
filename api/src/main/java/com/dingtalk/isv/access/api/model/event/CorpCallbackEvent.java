package com.dingtalk.isv.access.api.model.event;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.event.mq.SuiteCallBackMessage;

import java.io.Serializable;

/**
 * 企业回调事件
 * Created by Wallace on 2017/1/3.
 */
public class CorpCallbackEvent implements Serializable {
    private String suiteKey;
    private SuiteCallBackMessage.Tag tag;
    private JSONObject eventJSON;

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public SuiteCallBackMessage.Tag getTag() {
        return tag;
    }

    public void setTag(SuiteCallBackMessage.Tag tag) {
        this.tag = tag;
    }

    public JSONObject getEventJSON() {
        return eventJSON;
    }

    public void setEventJSON(JSONObject eventJSON) {
        this.eventJSON = eventJSON;
    }

    @Override
    public String toString() {
        return "CorpCallbackEvent{" +
                ", suiteKey='" + suiteKey + '\'' +
                ", tag='" + tag + '\'' +
                ", eventJSON='" + eventJSON + '\'' +
                '}';
    }
}

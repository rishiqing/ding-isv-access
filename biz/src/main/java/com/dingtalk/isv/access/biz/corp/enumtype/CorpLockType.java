package com.dingtalk.isv.access.biz.corp.enumtype;

/**
 * Created by Wallace on 2016/12/6.
 */
public enum CorpLockType {

    TOKEN("corpToken"),
    JSAPI_TICKET("corpJsapiTicket"),
    CHN_TOKEN("corpChannelJsapiTicket"),
    CHN_JSAPI_TICKET("corpChannelJsapiTicket");


    private final String key;

    private CorpLockType(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static CorpLockType getCorpLockType(String key){
        CorpLockType[] authFaileTypeArr = CorpLockType.values();
        for (CorpLockType o : authFaileTypeArr) {
            if (o.getKey().equalsIgnoreCase(key)) {
                return o;
            }
        }
        return null;
    }
}

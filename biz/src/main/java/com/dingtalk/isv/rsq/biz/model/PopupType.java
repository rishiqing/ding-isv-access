package com.dingtalk.isv.rsq.biz.model;

/**
 * 1  老用户升级提醒：2200年1月1日
 * 2  三日到期提醒：用户点击之后的第四天
 * 3  服务到期提醒：不做处理
 * 4  超员提醒：不做处理
 * Created by Wallace on 2016/12/6.
 */
public enum PopupType {

    OLD_UPGRADE("OLD_UPGRADE"),
    SOON_EXPIRE("SOON_EXPIRE"),
    EXPIRE("EXPIRE"),
    OVERLOAD("OVERLOAD");


    private final String key;

    private PopupType(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static PopupType getPopupType(String key){
        PopupType[] arr = PopupType.values();
        for (PopupType o : arr) {
            if (o.getKey().equalsIgnoreCase(key)) {
                return o;
            }
        }
        return null;
    }
}

package com.dingtalk.isv.access.api.model.event.mq;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mint on 16-7-17.
 */
public class SuiteCallBackMessage implements MessageCreator {

    public enum Tag {

        //  通讯录用户增加
        USER_ADD_ORG("user_add_org"),

        //  通讯录用户更改
        USER_MODIFY_ORG("user_modify_org"),

        //  通讯录用户离职
        USER_LEAVE_ORG("user_leave_org"),

        //  通讯录用户被设为管理员
        ORG_ADMIN_ADD("org_admin_add"),

        //  通讯录用户被取消设置为管理员
        ORG_ADMIN_REMOVE("org_admin_remove"),

        //  通讯录企业部门创建
        ORG_DEPT_CREATE("org_dept_create"),

        //  通讯录企业部门修改
        ORG_DEPT_MODIFY("org_dept_modify"),

        //  通讯录企业部门删除
        ORG_DEPT_REMOVE("org_dept_remove"),

        //  企业被解散
        ORG_REMOVE("org_remove"),

        /**客户更新*/
        CRM_CUSTOMER_UPDATE("crm_customer_update"),
        /**crm电话客户联系人*/
        CRM_CONTACT_CALL("crm_contact_call"),

        CHAT_ADD_MEMBER("chat_add_member"),

        CHAT_REMOVE_MEMBER("chat_remove_member"),

        CHAT_QUIT("chat_quit"),

        CHAT_UPDATE_OWNER("chat_update_owner"),

        CHAT_UPDATE_TITLE("chat_update_title"),

        CHAT_DISBAND("chat_disband"),

        CHAT_DISBAND_MICROAPP("chat_disband_microapp"),

        /** 日志微应用发表crm相关的日志 **/
        REPORT_ADD_CRM_REPORT("report_add_crm_report") ;

        private final String key;

        Tag(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }


        public static  List<String> getAllTag(){
            List<String> tagList = new ArrayList<String>();
            Tag[] tagArr = Tag.values();
            for (Tag o : tagArr) {
                tagList.add(o.getKey());
            }
            return tagList;
        }
    }


    private JSONObject jsonObject;
    private Tag tag;

    public SuiteCallBackMessage(JSONObject jsonObject, Tag tag) {
        this.jsonObject = jsonObject;
        this.tag = tag;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        MapMessage message = session.createMapMessage();
        message.setString("jsonObject", this.getJsonObject().toJSONString());
        message.setString("tag", this.getTag().toString());
        return message;
    }


    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }


    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

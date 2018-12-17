package com.dingtalk.isv.access.biz.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 发送消息相关的工具类
 * User: user 毛文强
 * Date: 2017/9/27
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class MessageUtil {
    private static final String RISHIQING = "日事清";
    private static final String DEFAULT_HEAD_BG = "FF458CDA";
    public static MessageBody parseOAMessage(JSONObject json){
        MessageBody.OABody oaBody = new MessageBody.OABody();

        oaBody.setMessage_url(json.getString("message_url"));
        if(json.containsKey("pc_message_url")){
            oaBody.setPc_message_url(json.getString("pc_message_url"));
        }

        if(json.containsKey("head")){
            MessageBody.OABody.Head head = new MessageBody.OABody.Head();
            JSONObject jsonHead = json.getJSONObject("head");
            //  head.text最多10个字符
            String title = jsonHead.getString("text");
            if(title.length() > 10){
                title = title.substring(0, 10);
            }
            head.setText(title);
            head.setBgcolor(jsonHead.getString("bgcolor"));
            oaBody.setHead(head);
        }

        if(json.containsKey("body")){
            MessageBody.OABody.Body body = new MessageBody.OABody.Body();
            JSONObject jsonBody = json.getJSONObject("body");

            if(jsonBody.containsKey("title")){
                body.setTitle(jsonBody.getString("title"));
            }

            if(jsonBody.containsKey("form")){
                List<MessageBody.OABody.Body.Form> list = new ArrayList<MessageBody.OABody.Body.Form>();
                JSONArray formArray = jsonBody.getJSONArray("form");
                Iterator it = formArray.iterator();
                while(it.hasNext()){
                    MessageBody.OABody.Body.Form form = new MessageBody.OABody.Body.Form();
                    JSONObject object = (JSONObject) it.next();
                    form.setKey(object.getString("key"));
                    form.setValue(object.getString("value"));
                    list.add(form);
                }
                body.setForm(list);
            }

            if(jsonBody.containsKey("rich")){
                MessageBody.OABody.Body.Rich rich = new MessageBody.OABody.Body.Rich();
                JSONObject object = jsonBody.getJSONObject("rich");
                rich.setNum(object.getString("num"));
                rich.setUnit(object.getString("unit"));
                body.setRich(rich);
            }
            if(jsonBody.containsKey("content")){
                body.setContent(jsonBody.getString("content"));
            }
            if(jsonBody.containsKey("image")){
                body.setImage(jsonBody.getString("image"));
            }

            if(jsonBody.containsKey("file_count")){
                body.setFile_count(jsonBody.getString("file_count"));
            }
            if(jsonBody.containsKey("author")){
                body.setAuthor(jsonBody.getString("author"));
            }
            oaBody.setBody(body);
        }

        return oaBody;
    }

    /**
     * 解析日事清后台直接发送OA的消息
     * 格式如下：
     {
       "title" : "领奖通知",
       "description" : "<div class=\"gray\">2016年9月26日</div> <div class=\"normal\">恭喜你抽中iPhone 7一台，领奖码：xxxx</div><div class=\"highlight\">请于2016年10月10日前联系行政同事领取</div>",
       "url" : "URL",
       "btntxt":"更多"
     }
     * @param json
     * @return
     */
    public static MessageBody parseRsqOAMessage(JSONObject json){
        MessageBody.OABody oaBody = new MessageBody.OABody();
        String url = json.getString("url");
        String title = json.getString("title");
        String desc = json.getString("description");
        String btntxt = json.getString("btntxt");

        oaBody.setMessage_url(url);
        if(json.containsKey("pc_message_url")){
            oaBody.setPc_message_url(json.getString("pc_message_url"));
        }

        MessageBody.OABody.Head head = new MessageBody.OABody.Head();
        head.setText(RISHIQING);
        head.setBgcolor(DEFAULT_HEAD_BG);
        oaBody.setHead(head);

        MessageBody.OABody.Body body = new MessageBody.OABody.Body();
        body.setTitle(title);

        //  desc可能为富文本，从中提取出文字
        body.setContent(Jsoup.parse(desc).body().text());

        oaBody.setBody(body);
        return oaBody;
    }

    /**
     * 将orgContent加上时间提醒的参数，并转换成String返回
     * @param orgContent
     * @return
     */
    public static String remindText(JSONObject orgContent, String remind) {
        JSONObject body = orgContent.getJSONObject("body");
        if (body.containsKey("content")) {
            body.remove("content");
        }
        body.put("content", remind);
        return JSON.toJSONString(orgContent);
    }

    private static MessageBody parseActionCardMessage(JSONObject json){
        MessageBody.ActionCardBody body = new MessageBody.ActionCardBody();
        body.setTitle(json.getString("title"));
        body.setMarkdown(json.getString("markdown"));
        if(json.containsKey("single_title")){
            body.setSingle_title(json.getString("single_title"));
        }
        if(json.containsKey("single_url")){
            body.setSingle_url(json.getString("single_url"));
        }
        if(json.containsKey("btn_orientation")){
            body.setBtn_orientation(json.getString("btn_orientation"));
        }
        if(json.containsKey("btn_json_list")){
            List<MessageBody.ActionCardBody.Button> btnList = new ArrayList<MessageBody.ActionCardBody.Button>();
            JSONArray btnArray = json.getJSONArray("btn_json_list");
            Iterator it = btnArray.iterator();
            while(it.hasNext()){
                MessageBody.ActionCardBody.Button btn = new MessageBody.ActionCardBody.Button();
                JSONObject object = (JSONObject) it.next();
                btn.setTitle(object.getString("title"));
                btn.setAction_url(object.getString("action_url"));
                btnList.add(btn);
            }
            body.setBtn_json_list(btnList);
        }
        return body;
    }

    private static MessageBody parseTextMessage(JSONObject orgContent){
        MessageBody.TextBody body = new MessageBody.TextBody();
        body.setContent(orgContent.getString("content"));
        return body;
    }

    private static MessageBody parseLinkMessage(JSONObject json){
        MessageBody.LinkBody body = new MessageBody.LinkBody();
        body.setMessageUrl(json.getString("messageUrl"));
        body.setPicUrl(json.getString("picUrl"));
        body.setTitle(json.getString("title"));
        body.setText(json.getString("title"));
        return body;
    }

    /**
     * 通用方法，根据orgContent做转换
     * @param orgContent
     * @return
     */
    public static MessageBody parseMessage(JSONObject orgContent){
        String msgType = orgContent.getString("msgtype");
        JSONObject msgJson = orgContent.getJSONObject(msgType);
        if("action_card".equals(msgType)){
            return parseActionCardMessage(msgJson);
        }else if("oa".equals(msgType)){
            return parseOAMessage(msgJson);
        }else if("link".equals(msgType)){
            return parseLinkMessage(msgJson);
        }else{
            return parseTextMessage(msgJson);
        }
    }

    public static void main(String[] args) {
        JSONObject json = JSON.parseObject("{message_url: \"aaaaaaa\", head: {text: 'ttttext',bgcolor:'FFAAAAAA'}}");
        MessageBody body = parseOAMessage(json);
        System.out.println(JSON.toJSONString(body));
    }
}

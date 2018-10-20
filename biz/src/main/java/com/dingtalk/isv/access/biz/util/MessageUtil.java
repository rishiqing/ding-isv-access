package com.dingtalk.isv.access.biz.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.corp.MessageBody;

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
            //  TODO  未实现
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

        MessageBody.OABody.Head head = new MessageBody.OABody.Head();
        head.setText(RISHIQING);
        head.setBgcolor(DEFAULT_HEAD_BG);
        oaBody.setHead(head);

        MessageBody.OABody.Body body = new MessageBody.OABody.Body();
        body.setTitle(title);
        body.setContent(desc);

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

    public static void main(String[] args) {
        JSONObject json = JSON.parseObject("{message_url: \"aaaaaaa\", head: {text: 'ttttext',bgcolor:'FFAAAAAA'}}");
        MessageBody body = parseOAMessage(json);
        System.out.println(JSON.toJSONString(body));
    }
}

package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.api.model.corp.ChatInfo;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;
import java.util.List;

@OpenService
public abstract interface ChatService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/chat/create", resultJsonKey="chatid")
  public abstract String createChat(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "name") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "owner") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "useridlist") List<String> paramList)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/chat/update")
  public abstract void updateChat(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "chatid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "name") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "owner") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "add_useridlist") List<String> paramList1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "del_useridlist") List<String> paramList2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/chat/get", resultJsonKey="chat_info")
  public abstract ChatInfo getChat(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "chatid") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/chat/bind")
  public abstract void bindMicroapp(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "chatid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "agentid") String paramString3)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/chat/unbind")
  public abstract void unbindMicroapp(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "chatid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "agentid") String paramString3)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/chat/send")
  public abstract void sendMessageToChat(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "chatid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "sender") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "msgtype") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "${runtime_key}") MessageBody paramMessageBody)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\ChatService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
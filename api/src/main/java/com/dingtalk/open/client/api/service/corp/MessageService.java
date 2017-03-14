package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.dingtalk.open.client.api.model.corp.MessageSendResult;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;

@OpenService
public abstract interface MessageService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/message/send_to_conversation", resultJsonKey="receiver")
  public abstract String sendToNormalConversation(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "sender") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "cid") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "msgtype") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "${runtime_key}") MessageBody paramMessageBody)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/message/send")
  public abstract MessageSendResult sendToCorpConversation(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "touser") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "toparty") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "agentid") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "msgtype") String paramString5, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "${runtime_key}") MessageBody paramMessageBody)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\MessageService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
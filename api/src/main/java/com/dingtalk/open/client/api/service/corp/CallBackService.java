package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;
import java.util.List;

@OpenService
public abstract interface CallBackService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/call_back/register_call_back")
  public abstract String registerCallBack(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "call_back_tag") List<String> paramList, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "token") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "aes_key") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "url") String paramString4)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/call_back/get_call_back")
  public abstract String getCallBack(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/call_back/update_call_back")
  public abstract String updateCallBack(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "call_back_tag") List<String> paramList, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "token") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "aes_key") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "url") String paramString4)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/call_back/delete_call_back")
  public abstract String deleteCallBack(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/call_back/get_call_back_failed_result")
  public abstract String getCallBackFailedResult(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\CallBackService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
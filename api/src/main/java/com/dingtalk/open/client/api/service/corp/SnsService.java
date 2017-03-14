package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;

@OpenService
public abstract interface SnsService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/sns/gettoken", resultJsonKey="access_token")
  public abstract String getToken(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "appid") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "appsecret") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/sns/get_persistent_code")
  public abstract String getPersistentCode(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "tmp_auth_code") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/sns/get_sns_token", resultJsonKey="access_token")
  public abstract String getSnsToken(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "openid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "persistent_code") String paramString3)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/sns/getuserinfo")
  public abstract String getUserInfo(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "sns_token") String paramString)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\SnsService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
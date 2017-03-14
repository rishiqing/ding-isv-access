package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;

@OpenService
public abstract interface SsoService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/sso/gettoken", resultJsonKey="access_token")
  public abstract String getSSOToken(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "corpid") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "corpsecret") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/sso/getuserinfo", resultJsonKey="access_token")
  public abstract String getSSOUserinfo(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "code") String paramString2)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\SsoService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
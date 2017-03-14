package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;

@OpenService
public abstract interface MicroappService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/microapp/create", resultJsonKey="id")
  public abstract Long microappCreate(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "appIcon") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "appName") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "appDesc") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "homepageUrl") String paramString5, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "pcHomepageUrl") String paramString6, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "ompLink") String paramString7)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\MicroappService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
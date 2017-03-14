package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.api.model.corp.UploadResult;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;
import java.io.File;

@OpenService
public abstract interface MediaService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/media/upload")
  public abstract UploadResult uploadMediaFile(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "type") String paramString2, @ParamAttr(location = ParamAttr.Location.FILE, paramKey = "media") File paramFile)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\MediaService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
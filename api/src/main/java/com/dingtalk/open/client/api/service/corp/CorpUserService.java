package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.dingtalk.open.client.api.model.corp.CorpUserList;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;
import java.util.List;
import java.util.Map;

@OpenService
public abstract interface CorpUserService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/user/get")
  public abstract CorpUserDetail getCorpUser(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "userid") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/user/create", resultJsonKey="userid")
  public abstract String createCorpUser(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "userid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "name") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "orderInDepts") Map<Long, Long> paramMap, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "department") List<Long> paramList, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "position") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "mobile") String paramString5, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "tel") String paramString6, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "workPlace") String paramString7, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "remark") String paramString8, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "email") String paramString9, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "jobnumber") String paramString10, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "isHide") Boolean paramBoolean1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "isSenior") Boolean paramBoolean2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "extattr") Map<String, String> paramMap1)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/user/update", resultJsonKey="userid")
  public abstract String updateCorpUser(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "userid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "name") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "orderInDepts") Map<Long, Long> paramMap, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "department") List<Long> paramList, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "position") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "mobile") String paramString5, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "tel") String paramString6, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "workPlace") String paramString7, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "remark") String paramString8, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "email") String paramString9, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "jobnumber") String paramString10, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "isHide") Boolean paramBoolean1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "isSenior") Boolean paramBoolean2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "extattr") Map<String, String> paramMap1)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/user/delete")
  public abstract CorpUserDetail deleteCorpUser(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "userid") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/user/list")
  public abstract CorpUserDetailList getCorpUserList(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "department_id") long paramLong, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "offset") Long paramLong1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "size") Integer paramInteger, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "order") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/user/simplelist")
  public abstract CorpUserList getCorpUserSimpleList(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "department_id") long paramLong, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "offset") Long paramLong1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "size") Integer paramInteger, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "order") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/user/batchget")
  public abstract CorpUserDetailList getCorpUserListByUserids(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "useridlist") List<String> paramList)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/user/batchdelete")
  public abstract CorpUserDetailList batchdeleteCorpUserListByUserids(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "useridlist") List<String> paramList)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/user/getuserinfo")
  public abstract CorpUserDetail getUserinfo(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "code") String paramString2)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\CorpUserService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
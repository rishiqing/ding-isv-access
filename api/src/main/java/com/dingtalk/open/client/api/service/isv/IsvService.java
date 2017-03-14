package com.dingtalk.open.client.api.service.isv;

import com.dingtalk.open.client.api.model.isv.CorpAgent;
import com.dingtalk.open.client.api.model.isv.CorpAuthInfo;
import com.dingtalk.open.client.api.model.isv.CorpAuthSuiteCode;
import com.dingtalk.open.client.api.model.isv.CorpAuthToken;
import com.dingtalk.open.client.api.model.isv.SuiteToken;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;
import java.util.List;

@OpenService
public abstract interface IsvService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/get_suite_token")
  public abstract SuiteToken getSuiteToken(@ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "suite_key") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "suite_secret") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "suite_ticket") String paramString3)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/get_permanent_code")
  public abstract CorpAuthSuiteCode getPermanentCode(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "suite_access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "tmp_auth_code") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/get_corp_token")
  public abstract CorpAuthToken getCorpToken(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "suite_access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "auth_corpid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "permanent_code") String paramString3)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/get_auth_info")
  public abstract CorpAuthInfo getAuthInfo(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "suite_access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "suite_key") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "auth_corpid") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "permanent_code") String paramString4)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/get_agent")
  public abstract CorpAgent getAgent(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "suite_access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "suite_key") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "auth_corpid") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "agentid") String paramString4)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/activate_suite")
  public abstract void activateSuite(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "suite_access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "suite_key") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "auth_corpid") String paramString3)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/service/set_corp_ipwhitelist")
  public abstract void setCorpIpwhitelist(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "suite_access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "auth_corpid") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "ip_whitelist") List<String> paramList)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\isv\IsvService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
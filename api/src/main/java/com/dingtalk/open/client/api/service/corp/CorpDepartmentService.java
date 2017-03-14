package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenAPI.HttpMethod;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ParamAttr.Location;
import com.dingtalk.open.client.common.ServiceException;
import java.util.List;

@OpenService
public abstract interface CorpDepartmentService
{
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="department/list", resultJsonKey="department")
  public abstract List<Department> getDeptList(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "id") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="department/get")
  public abstract DepartmentDetail getDeptDetail(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "id") String paramString2)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/department/create", resultJsonKey="id")
  public abstract String deptCreate(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "name") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "parentid") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "order") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "createDeptGroup") Boolean paramBoolean)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.POST, uriPath="/department/update")
  public abstract String deptUpdate(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "id") Long paramLong, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "name") String paramString2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "parentid") String paramString3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "order") String paramString4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "createDeptGroup") Boolean paramBoolean1, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "autoAddUser") Boolean paramBoolean2, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "deptManagerUseridList") String paramString5, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "deptHiding") Boolean paramBoolean3, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "deptPerimits") String paramString6, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "userPerimits") String paramString7, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "outerDept") Boolean paramBoolean4, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "outerPermitDepts") String paramString8, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "outerPermitUsers") String paramString9, @ParamAttr(location = ParamAttr.Location.JSON_CONTENT, paramKey = "orgDeptOwner") String paramString10)
    throws ServiceException;
  
  @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="department/delete")
  public abstract DepartmentDetail deptDelete(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String paramString, @ParamAttr(location = ParamAttr.Location.URL, paramKey = "id") Long paramLong)
    throws ServiceException;
}


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\service\corp\CorpDepartmentService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
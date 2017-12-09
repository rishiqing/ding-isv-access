package com.dingtalk.open.client.api.service.corp;

import com.dingtalk.open.client.api.model.corp.CorpAdminList;
import com.dingtalk.open.client.common.OpenAPI;
import com.dingtalk.open.client.common.OpenService;
import com.dingtalk.open.client.common.ParamAttr;
import com.dingtalk.open.client.common.ServiceException;

@OpenService
public interface CorpAdminService {
    @OpenAPI(httpMethod=OpenAPI.HttpMethod.GET, uriPath="/user/get_admin")
    CorpAdminList getCorpAdminList(@ParamAttr(location = ParamAttr.Location.URL, paramKey = "access_token") String accessToken)
            throws ServiceException;
}

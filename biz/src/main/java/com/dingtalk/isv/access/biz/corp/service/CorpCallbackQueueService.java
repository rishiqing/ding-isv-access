package com.dingtalk.isv.access.biz.corp.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.event.CorpCallbackEvent;
import com.dingtalk.isv.access.biz.corp.enumtype.CorpLockType;
import com.dingtalk.isv.access.biz.corp.model.CorpLockDO;
import com.dingtalk.isv.common.model.ServiceResult;

/**
 * 公司通讯录回调事件队列相关方法
 * Created by Wallace on 2017/1/4.
 */
public interface CorpCallbackQueueService {

    /**
     * 将通讯录回调保存到本地
     * @param jsonObject
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> saveCorpCallbackQueue(JSONObject jsonObject, String suiteKey);

    /**
     * 保存通讯录回调失败的信息
     * @param jsonObject
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> saveCorpCallbackFail(JSONObject jsonObject, String suiteKey);

        /**
         * 新增用户，从钉钉获取同时保存到本地，然后更新到第三方服务器
         * @param userIdArray
         * @param corpId
         * @param suiteKey
         * @return
         */
    public ServiceResult<Void> addUser(String[] userIdArray, String corpId, String suiteKey);

    /**
     * 更改用户，从钉钉获取同时更新到本地，然后更新到第三方服务器
     * @param userIdArray
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> changeUser(String[] userIdArray, String corpId, String suiteKey);

    /**
     * 删除用户，从钉钉获取，在本地做删除（移入删除表）， 然后更新到第三方服务器
     * @param userIdArray
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> deleteUser(String[] userIdArray, String corpId, String suiteKey);

    /**
     * 切换用户的管理员状态，从服务器获取用户，然后保存到本地，在更新到第三方服务器
     * @param userIdArray
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> toggleUserAdmin(String[] userIdArray, String corpId, String suiteKey);

    /**
     * 新增部门，保存到本地，然后更新到第三方服务器
     * @param deptIdArray
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> addDepartment(Long[] deptIdArray, String corpId, String suiteKey);

    /**
     * 更新部门，保存到本地，然后更新到第三方服务器
     * @param deptIdArray
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> changeDepartment(Long[] deptIdArray, String corpId, String suiteKey);

    /**
     * 删除部门，在本地做删除，然后推送
     * @param deptIdArray
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> deleteDepartment(Long[] deptIdArray, String corpId, String suiteKey);

}

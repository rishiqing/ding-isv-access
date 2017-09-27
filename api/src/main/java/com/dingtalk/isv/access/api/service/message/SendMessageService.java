package com.dingtalk.isv.access.api.service.message;


import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.MessageBody;

import java.util.List;

/**
 * 企业对套件授权service
 */
public interface SendMessageService {
    /**
     * 发送单人oa消息
     * @param suiteKey
     * @param corpId
     * @param appId
     * @param message
     * @return
     */
    public ServiceResult<Void> sendOAMessageToUser(String suiteKey, String corpId, Long appId, String msgType, List<String> staffIdList, List<Long> deptIdList, MessageBody message);

    /**
     * 发送文字消息
     * @param suiteKey
     * @param corpId
     * @param appId
     * @param msgType
     * @param staffIdList
     * @param deptIdList
     * @param message
     * @return
     */
    public ServiceResult<Void> sendMessageToUser(String suiteKey, String corpId, Long appId, String msgType, List<String> staffIdList, List<Long> deptIdList, MessageBody message);

    /**
     * 发送普通消息
     * @see <a href="https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.HQ7owL&treeId=385&articleId=104974&docType=1">https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.HQ7owL&treeId=385&articleId=104974&docType=1</a>
     * @param suiteKey
     * @param corpId
     * @param sender
     * @param cid
     * @param msgType
     * @param message
     * @return
     */
    public ServiceResult<Void> sendNormalMessage(String suiteKey, String corpId, String sender, String cid, String msgType, MessageBody message);


    /**
     * 异步发送企业通知消息
     * @see <a href="https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.qZt5wv&treeId=385&articleId=28915&docType=2">https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.qZt5wv&treeId=385&articleId=28915&docType=2</a>
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<Void> sendCorpMessageAsync(String suiteKey, String corpId, Long appId, String msgType, Boolean toAllUser, List<String> staffIdList,  List<Long> deptIdList, MessageBody message);
}

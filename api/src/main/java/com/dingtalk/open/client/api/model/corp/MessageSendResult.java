/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class MessageSendResult implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String invaliduser;
/*    */   private String invalidparty;
/*    */   
/*    */   public String getInvaliduser() {
/* 12 */     return this.invaliduser;
/*    */   }
/*    */   
/*    */   public void setInvaliduser(String invaliduser) {
/* 16 */     this.invaliduser = invaliduser;
/*    */   }
/*    */   
/*    */   public String getInvalidparty() {
/* 20 */     return this.invalidparty;
/*    */   }
/*    */   
/*    */   public void setInvalidparty(String invalidparty) {
/* 24 */     this.invalidparty = invalidparty;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\MessageSendResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
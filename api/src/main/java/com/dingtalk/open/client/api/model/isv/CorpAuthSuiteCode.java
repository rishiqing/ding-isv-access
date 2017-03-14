/*    */ package com.dingtalk.open.client.api.model.isv;
/*    */ 
/*    */ public class CorpAuthSuiteCode
/*    */ {
/*    */   private String permanent_code;
/*    */   private AuthCorpInfo auth_corp_info;
/*    */   
/*    */   public String getPermanent_code() {
/*  9 */     return this.permanent_code;
/*    */   }
/*    */   
/*    */   public void setPermanent_code(String permanent_code) {
/* 13 */     this.permanent_code = permanent_code;
/*    */   }
/*    */   
/*    */   public AuthCorpInfo getAuth_corp_info() {
/* 17 */     return this.auth_corp_info;
/*    */   }
/*    */   
/*    */   public void setAuth_corp_info(AuthCorpInfo auth_corp_info) {
/* 21 */     this.auth_corp_info = auth_corp_info;
/*    */   }
/*    */   
/*    */   public static class AuthCorpInfo {
/*    */     private String corpid;
/*    */     private String corp_name;
/*    */     
/*    */     public String getCorpid() {
/* 29 */       return this.corpid;
/*    */     }
/*    */     
/*    */     public void setCorpid(String corpid) {
/* 33 */       this.corpid = corpid;
/*    */     }
/*    */     
/*    */     public String getCorp_name() {
/* 37 */       return this.corp_name;
/*    */     }
/*    */     
/*    */     public void setCorp_name(String corp_name) {
/* 41 */       this.corp_name = corp_name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\isv\CorpAuthSuiteCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
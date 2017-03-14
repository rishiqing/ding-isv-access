/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class UploadResult implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String type;
/*    */   private String media_id;
/*    */   private Long created_at;
/*    */   
/*    */   public String getType() {
/* 13 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 17 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getMedia_id() {
/* 21 */     return this.media_id;
/*    */   }
/*    */   
/*    */   public void setMedia_id(String media_id) {
/* 25 */     this.media_id = media_id;
/*    */   }
/*    */   
/*    */   public Long getCreated_at() {
/* 29 */     return this.created_at;
/*    */   }
/*    */   
/*    */   public void setCreated_at(Long created_at) {
/* 33 */     this.created_at = created_at;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\UploadResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
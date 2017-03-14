/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class CorpUser implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String userid;
/*    */   private String name;
/*    */   private Boolean active;
/*    */   
/*    */   public String getUserid() {
/* 13 */     return this.userid;
/*    */   }
/*    */   
/*    */   public void setUserid(String userid) {
/* 17 */     this.userid = userid;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 25 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Boolean getActive() {
/* 29 */     return this.active;
/*    */   }
/*    */   
/*    */   public void setActive(Boolean active) {
/* 33 */     this.active = active;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 38 */     int prime = 31;
/* 39 */     int result = 1;
/* 40 */     result = 31 * result + (this.active == null ? 0 : this.active.hashCode());
/* 41 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 42 */     result = 31 * result + (this.userid == null ? 0 : this.userid.hashCode());
/* 43 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 48 */     if (this == obj) return true;
/* 49 */     if (obj == null) return false;
/* 50 */     if (getClass() != obj.getClass()) return false;
/* 51 */     CorpUser other = (CorpUser)obj;
/* 52 */     if (this.active == null) {
/* 53 */       if (other.active != null) return false;
/* 54 */     } else if (!this.active.equals(other.active)) return false;
/* 55 */     if (this.name == null) {
/* 56 */       if (other.name != null) return false;
/* 57 */     } else if (!this.name.equals(other.name)) return false;
/* 58 */     if (this.userid == null) {
/* 59 */       if (other.userid != null) return false;
/* 60 */     } else if (!this.userid.equals(other.userid)) return false;
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\CorpUser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
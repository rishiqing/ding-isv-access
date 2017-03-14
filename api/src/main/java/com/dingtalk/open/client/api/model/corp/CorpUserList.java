/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class CorpUserList implements java.io.Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Boolean hasMore;
/*    */   private List<CorpUser> userlist;
/*    */   
/*    */   public Boolean isHasMore()
/*    */   {
/* 13 */     return this.hasMore;
/*    */   }
/*    */   
/*    */   public void setHasMore(Boolean hasMore) {
/* 17 */     this.hasMore = hasMore;
/*    */   }
/*    */   
/*    */   public List<CorpUser> getUserlist() {
/* 21 */     return this.userlist;
/*    */   }
/*    */   
/*    */   public void setUserlist(List<CorpUser> userlist) {
/* 25 */     this.userlist = userlist;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 30 */     int prime = 31;
/* 31 */     int result = 1;
/* 32 */     result = 31 * result + (this.hasMore == null ? 0 : this.hasMore.hashCode());
/* 33 */     result = 31 * result + (this.userlist == null ? 0 : this.userlist.hashCode());
/* 34 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 39 */     if (this == obj) return true;
/* 40 */     if (obj == null) return false;
/* 41 */     if (getClass() != obj.getClass()) return false;
/* 42 */     CorpUserList other = (CorpUserList)obj;
/* 43 */     if (this.hasMore == null) {
/* 44 */       if (other.hasMore != null) return false;
/* 45 */     } else if (!this.hasMore.equals(other.hasMore)) return false;
/* 46 */     if (this.userlist == null) {
/* 47 */       if (other.userlist != null) return false;
/* 48 */     } else if (!this.userlist.equals(other.userlist)) return false;
/* 49 */     return true;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\CorpUserList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
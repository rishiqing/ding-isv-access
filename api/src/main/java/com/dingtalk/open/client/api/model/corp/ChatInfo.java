/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class ChatInfo implements java.io.Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String name;
/*    */   private String owner;
/*    */   private List<String> useridlist;
/*    */   private List<String> agentidlist;
/*    */   
/*    */   public String getName()
/*    */   {
/* 15 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 19 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getOwner() {
/* 23 */     return this.owner;
/*    */   }
/*    */   
/*    */   public void setOwner(String owner) {
/* 27 */     this.owner = owner;
/*    */   }
/*    */   
/*    */   public List<String> getUseridlist() {
/* 31 */     return this.useridlist;
/*    */   }
/*    */   
/*    */   public void setUseridlist(List<String> useridlist) {
/* 35 */     this.useridlist = useridlist;
/*    */   }
/*    */   
/*    */   public List<String> getAgentidlist() {
/* 39 */     return this.agentidlist;
/*    */   }
/*    */   
/*    */   public void setAgentidlist(List<String> agentidlist) {
/* 43 */     this.agentidlist = agentidlist;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 48 */     int prime = 31;
/* 49 */     int result = 1;
/* 50 */     result = 31 * result + (this.agentidlist == null ? 0 : this.agentidlist.hashCode());
/* 51 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 52 */     result = 31 * result + (this.owner == null ? 0 : this.owner.hashCode());
/* 53 */     result = 31 * result + (this.useridlist == null ? 0 : this.useridlist.hashCode());
/* 54 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 59 */     if (this == obj) return true;
/* 60 */     if (obj == null) return false;
/* 61 */     if (getClass() != obj.getClass()) return false;
/* 62 */     ChatInfo other = (ChatInfo)obj;
/* 63 */     if (this.agentidlist == null) {
/* 64 */       if (other.agentidlist != null) return false;
/* 65 */     } else if (!this.agentidlist.equals(other.agentidlist)) return false;
/* 66 */     if (this.name == null) {
/* 67 */       if (other.name != null) return false;
/* 68 */     } else if (!this.name.equals(other.name)) return false;
/* 69 */     if (this.owner == null) {
/* 70 */       if (other.owner != null) return false;
/* 71 */     } else if (!this.owner.equals(other.owner)) return false;
/* 72 */     if (this.useridlist == null) {
/* 73 */       if (other.useridlist != null) return false;
/* 74 */     } else if (!this.useridlist.equals(other.useridlist)) return false;
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\ChatInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
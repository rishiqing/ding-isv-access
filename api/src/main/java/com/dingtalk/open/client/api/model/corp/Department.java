/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Department implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Long id;
/*    */   private String name;
/*    */   private Long parentid;
/*    */   private Boolean createDeptGroup;
/*    */   private Boolean autoAddUser;
/*    */   
/*    */   public Long getId() {
/* 15 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Long id) {
/* 19 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 27 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Long getParentid() {
/* 31 */     return this.parentid;
/*    */   }
/*    */   
/*    */   public void setParentid(Long parentid) {
/* 35 */     this.parentid = parentid;
/*    */   }
/*    */   
/*    */   public Boolean getCreateDeptGroup() {
/* 39 */     return this.createDeptGroup;
/*    */   }
/*    */   
/*    */   public void setCreateDeptGroup(Boolean createDeptGroup) {
/* 43 */     this.createDeptGroup = createDeptGroup;
/*    */   }
/*    */   
/*    */   public Boolean getAutoAddUser() {
/* 47 */     return this.autoAddUser;
/*    */   }
/*    */   
/*    */   public void setAutoAddUser(Boolean autoAddUser) {
/* 51 */     this.autoAddUser = autoAddUser;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\Department.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
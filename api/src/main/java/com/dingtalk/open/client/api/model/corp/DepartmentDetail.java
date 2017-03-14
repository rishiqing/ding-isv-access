/*    */ package com.dingtalk.open.client.api.model.corp;
/*    */ 
/*    */ public class DepartmentDetail extends Department
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Long order;
/*    */   private Boolean deptHiding;
/*    */   private String deptPerimits;
/*    */   private String orgDeptOwner;
/*    */   private String deptManagerUseridList;
/*    */   
/*    */   public Long getOrder() {
/* 13 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setOrder(Long order) {
/* 17 */     this.order = order;
/*    */   }
/*    */   
/*    */   public Boolean getDeptHiding() {
/* 21 */     return this.deptHiding;
/*    */   }
/*    */   
/*    */   public void setDeptHiding(Boolean deptHiding) {
/* 25 */     this.deptHiding = deptHiding;
/*    */   }
/*    */   
/*    */   public String getDeptPerimits() {
/* 29 */     return this.deptPerimits;
/*    */   }
/*    */   
/*    */   public void setDeptPerimits(String deptPerimits) {
/* 33 */     this.deptPerimits = deptPerimits;
/*    */   }
/*    */   
/*    */   public String getOrgDeptOwner() {
/* 37 */     return this.orgDeptOwner;
/*    */   }
/*    */   
/*    */   public void setOrgDeptOwner(String orgDeptOwner) {
/* 41 */     this.orgDeptOwner = orgDeptOwner;
/*    */   }
/*    */   
/*    */   public String getDeptManagerUseridList() {
/* 45 */     return this.deptManagerUseridList;
/*    */   }
/*    */   
/*    */   public void setDeptManagerUseridList(String deptManagerUseridList) {
/* 49 */     this.deptManagerUseridList = deptManagerUseridList;
/*    */   }
/*    */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\DepartmentDetail.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
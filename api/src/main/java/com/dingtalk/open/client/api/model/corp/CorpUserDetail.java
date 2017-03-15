/*     */ package com.dingtalk.open.client.api.model.corp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CorpUserDetail extends CorpUser implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String tel;
/*     */   private String workPlace;
/*     */   private String remark;
/*     */   private String mobile;
/*     */   private String email;
/*     */   private String orderInDepts;
/*     */   private Boolean isAdmin;
/*     */   private Boolean isBoss;
/*     */   private String dingId;
/*     */   private String isLeaderInDepts;
/*     */   private Boolean isHide;
/*     */   private List<Long> department;
/*     */   private String position;
/*     */   private String avatar;
/*     */   private String jobnumber;
/*     */   private Map<String, String> extattr;
            private String unionId;
/*     */   
/*     */   public String getTel()
/*     */   {
/*  29 */     return this.tel;
/*     */   }
/*     */   
/*     */   public void setTel(String tel) {
/*  33 */     this.tel = tel;
/*     */   }
/*     */   
/*     */   public String getWorkPlace() {
/*  37 */     return this.workPlace;
/*     */   }
/*     */   
/*     */   public void setWorkPlace(String workPlace) {
/*  41 */     this.workPlace = workPlace;
/*     */   }
/*     */   
/*     */   public String getRemark() {
/*  45 */     return this.remark;
/*     */   }
/*     */   
/*     */   public void setRemark(String remark) {
/*  49 */     this.remark = remark;
/*     */   }
/*     */   
/*     */   public String getMobile() {
/*  53 */     return this.mobile;
/*     */   }
/*     */   
/*     */   public void setMobile(String mobile) {
/*  57 */     this.mobile = mobile;
/*     */   }
/*     */   
/*     */   public String getEmail() {
/*  61 */     return this.email;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/*  65 */     this.email = email;
/*     */   }
/*     */   
/*     */   public String getOrderInDepts() {
/*  69 */     return this.orderInDepts;
/*     */   }
/*     */   
/*     */   public void setOrderInDepts(String orderInDepts) {
/*  73 */     this.orderInDepts = orderInDepts;
/*     */   }
/*     */   
/*     */   public Boolean getIsAdmin() {
/*  77 */     return this.isAdmin;
/*     */   }
/*     */   
/*     */   public void setIsAdmin(Boolean isAdmin) {
/*  81 */     this.isAdmin = isAdmin;
/*     */   }
/*     */   
/*     */   public Boolean getIsBoss() {
/*  85 */     return this.isBoss;
/*     */   }
/*     */   
/*     */   public void setIsBoss(Boolean isBoss) {
/*  89 */     this.isBoss = isBoss;
/*     */   }
/*     */   
/*     */   public String getDingId() {
/*  93 */     return this.dingId;
/*     */   }
/*     */   
/*     */   public void setDingId(String dingId) {
/*  97 */     this.dingId = dingId;
/*     */   }
/*     */   
/*     */   public String getIsLeaderInDepts() {
/* 101 */     return this.isLeaderInDepts;
/*     */   }
/*     */   
/*     */   public void setIsLeaderInDepts(String isLeaderInDepts) {
/* 105 */     this.isLeaderInDepts = isLeaderInDepts;
/*     */   }
/*     */   
/*     */   public Boolean getIsHide() {
/* 109 */     return this.isHide;
/*     */   }
/*     */   
/*     */   public void setIsHide(Boolean isHide) {
/* 113 */     this.isHide = isHide;
/*     */   }
/*     */   
/*     */   public List<Long> getDepartment() {
/* 117 */     return this.department;
/*     */   }
/*     */   
/*     */   public void setDepartment(List<Long> department) {
/* 121 */     this.department = department;
/*     */   }
/*     */   
/*     */   public String getPosition() {
/* 125 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setPosition(String position) {
/* 129 */     this.position = position;
/*     */   }
/*     */   
/*     */   public String getAvatar() {
/* 133 */     return this.avatar;
/*     */   }
/*     */   
/*     */   public void setAvatar(String avatar) {
/* 137 */     this.avatar = avatar;
/*     */   }
/*     */   
/*     */   public String getJobnumber() {
/* 141 */     return this.jobnumber;
/*     */   }
/*     */   
/*     */   public void setJobnumber(String jobnumber) {
/* 145 */     this.jobnumber = jobnumber;
/*     */   }
/*     */   
/*     */   public Map<String, String> getExtattr() {
/* 149 */     return this.extattr;
/*     */   }
/*     */   
/*     */   public void setExtattr(Map<String, String> extattr) {
/* 153 */     this.extattr = extattr;
/*     */   }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /*     */
/*     */   public int hashCode()
/*     */   {
/* 158 */     int prime = 31;
/* 159 */     int result = super.hashCode();
/* 160 */     result = 31 * result + (this.avatar == null ? 0 : this.avatar.hashCode());
/* 161 */     result = 31 * result + (this.department == null ? 0 : this.department.hashCode());
/* 162 */     result = 31 * result + (this.dingId == null ? 0 : this.dingId.hashCode());
/* 163 */     result = 31 * result + (this.email == null ? 0 : this.email.hashCode());
/* 164 */     result = 31 * result + (this.extattr == null ? 0 : this.extattr.hashCode());
/* 165 */     result = 31 * result + (this.isAdmin == null ? 0 : this.isAdmin.hashCode());
/* 166 */     result = 31 * result + (this.isBoss == null ? 0 : this.isBoss.hashCode());
/* 167 */     result = 31 * result + (this.isHide == null ? 0 : this.isHide.hashCode());
/* 168 */     result = 31 * result + (this.isLeaderInDepts == null ? 0 : this.isLeaderInDepts.hashCode());
/* 169 */     result = 31 * result + (this.jobnumber == null ? 0 : this.jobnumber.hashCode());
/* 170 */     result = 31 * result + (this.mobile == null ? 0 : this.mobile.hashCode());
/* 171 */     result = 31 * result + (this.orderInDepts == null ? 0 : this.orderInDepts.hashCode());
/* 172 */     result = 31 * result + (this.position == null ? 0 : this.position.hashCode());
/* 173 */     result = 31 * result + (this.remark == null ? 0 : this.remark.hashCode());
/* 174 */     result = 31 * result + (this.tel == null ? 0 : this.tel.hashCode());
/* 175 */     result = 31 * result + (this.workPlace == null ? 0 : this.workPlace.hashCode());
              result = 31 * result + (this.unionId == null ? 0 : this.unionId.hashCode());
/* 176 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 181 */     if (this == obj) return true;
/* 182 */     if (!super.equals(obj)) return false;
/* 183 */     if (getClass() != obj.getClass()) return false;
/* 184 */     CorpUserDetail other = (CorpUserDetail)obj;
/* 185 */     if (this.avatar == null) {
/* 186 */       if (other.avatar != null) return false;
/* 187 */     } else if (!this.avatar.equals(other.avatar)) return false;
/* 188 */     if (this.department == null) {
/* 189 */       if (other.department != null) return false;
/* 190 */     } else if (!this.department.equals(other.department)) return false;
/* 191 */     if (this.dingId == null) {
/* 192 */       if (other.dingId != null) return false;
/* 193 */     } else if (!this.dingId.equals(other.dingId)) return false;
/* 194 */     if (this.email == null) {
/* 195 */       if (other.email != null) return false;
/* 196 */     } else if (!this.email.equals(other.email)) return false;
/* 197 */     if (this.extattr == null) {
/* 198 */       if (other.extattr != null) return false;
/* 199 */     } else if (!this.extattr.equals(other.extattr)) return false;
/* 200 */     if (this.isAdmin == null) {
/* 201 */       if (other.isAdmin != null) return false;
/* 202 */     } else if (!this.isAdmin.equals(other.isAdmin)) return false;
/* 203 */     if (this.isBoss == null) {
/* 204 */       if (other.isBoss != null) return false;
/* 205 */     } else if (!this.isBoss.equals(other.isBoss)) return false;
/* 206 */     if (this.isHide == null) {
/* 207 */       if (other.isHide != null) return false;
/* 208 */     } else if (!this.isHide.equals(other.isHide)) return false;
/* 209 */     if (this.isLeaderInDepts == null) {
/* 210 */       if (other.isLeaderInDepts != null) return false;
/* 211 */     } else if (!this.isLeaderInDepts.equals(other.isLeaderInDepts)) return false;
/* 212 */     if (this.jobnumber == null) {
/* 213 */       if (other.jobnumber != null) return false;
/* 214 */     } else if (!this.jobnumber.equals(other.jobnumber)) return false;
/* 215 */     if (this.mobile == null) {
/* 216 */       if (other.mobile != null) return false;
/* 217 */     } else if (!this.mobile.equals(other.mobile)) return false;
/* 218 */     if (this.orderInDepts == null) {
/* 219 */       if (other.orderInDepts != null) return false;
/* 220 */     } else if (!this.orderInDepts.equals(other.orderInDepts)) return false;
/* 221 */     if (this.position == null) {
/* 222 */       if (other.position != null) return false;
/* 223 */     } else if (!this.position.equals(other.position)) return false;
/* 224 */     if (this.remark == null) {
/* 225 */       if (other.remark != null) return false;
/* 226 */     } else if (!this.remark.equals(other.remark)) return false;
/* 227 */     if (this.tel == null) {
/* 228 */       if (other.tel != null) return false;
/* 229 */     } else if (!this.tel.equals(other.tel)) return false;
/* 230 */     if (this.workPlace == null) {
/* 231 */       if (other.workPlace != null) return false;
/* 232 */     } else if (!this.workPlace.equals(other.workPlace)) return false;
              if (this.unionId == null) {
                  if (other.unionId != null) return false;
              } else if (!this.unionId.equals(other.unionId)) return false;
/* 233 */     return true;
/*     */   }
/*     */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\CorpUserDetail.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
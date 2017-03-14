/*     */ package com.dingtalk.open.client.api.model.isv;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class CorpAuthInfo
/*     */ {
/*     */   private AuthCorpInfo auth_corp_info;
/*     */   private AuthUserInfo auth_user_info;
/*     */   private AuthInfo auth_info;
/*     */   
/*     */   public static class Agent
/*     */   {
/*     */     private String agent_name;
/*     */     private Long agentid;
/*     */     private Long appid;
/*     */     private String logo_url;
/*     */     
/*     */     public String getAgent_name() {
/*  19 */       return this.agent_name;
/*     */     }
/*     */     
/*     */     public void setAgent_name(String agent_name) {
/*  23 */       this.agent_name = agent_name;
/*     */     }
/*     */     
/*     */     public Long getAgentid() {
/*  27 */       return this.agentid;
/*     */     }
/*     */     
/*     */     public void setAgentid(Long agentid) {
/*  31 */       this.agentid = agentid;
/*     */     }
/*     */     
/*     */     public Long getAppid() {
/*  35 */       return this.appid;
/*     */     }
/*     */     
/*     */     public void setAppid(Long appid) {
/*  39 */       this.appid = appid;
/*     */     }
/*     */     
/*     */     public String getLogo_url() {
/*  43 */       return this.logo_url;
/*     */     }
/*     */     
/*     */     public void setLogo_url(String logo_url) {
/*  47 */       this.logo_url = logo_url;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AuthInfo
/*     */   {
/*     */     private List<Agent> agent;
/*     */     
/*     */     public List<Agent> getAgent() {
/*  56 */       return this.agent;
/*     */     }
/*     */     
/*     */     public void setAgent(List<Agent> agent) {
/*  60 */       this.agent = agent;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AuthUserInfo
/*     */   {
/*     */     private String userId;
/*     */     
/*     */     public String getUserId() {
/*  69 */       return this.userId;
/*     */     }
/*     */     
/*     */     public void setUserId(String userId) {
/*  73 */       this.userId = userId;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AuthCorpInfo
/*     */   {
/*     */     private String corp_logo_url;
/*     */     private String corp_name;
/*     */     private String corpid;
/*     */     private String industry;
/*     */     private String invite_code;
/*     */     private String license_code;
/*     */     private String invite_url;
/*     */     
/*     */     public String getCorp_logo_url() {
/*  88 */       return this.corp_logo_url;
/*     */     }
/*     */     
/*     */     public void setCorp_logo_url(String corp_logo_url) {
/*  92 */       this.corp_logo_url = corp_logo_url;
/*     */     }
/*     */     
/*     */     public String getCorp_name() {
/*  96 */       return this.corp_name;
/*     */     }
/*     */     
/*     */     public void setCorp_name(String corp_name) {
/* 100 */       this.corp_name = corp_name;
/*     */     }
/*     */     
/*     */     public String getCorpid() {
/* 104 */       return this.corpid;
/*     */     }
/*     */     
/*     */     public void setCorpid(String corpid) {
/* 108 */       this.corpid = corpid;
/*     */     }
/*     */     
/*     */     public String getIndustry() {
/* 112 */       return this.industry;
/*     */     }
/*     */     
/*     */     public void setIndustry(String industry) {
/* 116 */       this.industry = industry;
/*     */     }
/*     */     
/*     */     public String getInvite_code() {
/* 120 */       return this.invite_code;
/*     */     }
/*     */     
/*     */     public void setInvite_code(String invite_code) {
/* 124 */       this.invite_code = invite_code;
/*     */     }
/*     */     
/*     */     public String getLicense_code() {
/* 128 */       return this.license_code;
/*     */     }
/*     */     
/*     */     public void setLicense_code(String license_code) {
/* 132 */       this.license_code = license_code;
/*     */     }
/*     */     
/*     */     public String getInvite_url() {
/* 136 */       return this.invite_url;
/*     */     }
/*     */     
/*     */     public void setInvite_url(String invite_url) {
/* 140 */       this.invite_url = invite_url;
/*     */     }
/*     */   }
/*     */   
/*     */   public AuthCorpInfo getAuth_corp_info() {
/* 145 */     return this.auth_corp_info;
/*     */   }
/*     */   
/*     */   public void setAuth_corp_info(AuthCorpInfo auth_corp_info) {
/* 149 */     this.auth_corp_info = auth_corp_info;
/*     */   }
/*     */   
/*     */   public AuthUserInfo getAuth_user_info() {
/* 153 */     return this.auth_user_info;
/*     */   }
/*     */   
/*     */   public void setAuth_user_info(AuthUserInfo auth_user_info) {
/* 157 */     this.auth_user_info = auth_user_info;
/*     */   }
/*     */   
/*     */   public AuthInfo getAuth_info() {
/* 161 */     return this.auth_info;
/*     */   }
/*     */   
/*     */   public void setAuth_info(AuthInfo auth_info) {
/* 165 */     this.auth_info = auth_info;
/*     */   }
/*     */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\isv\CorpAuthInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
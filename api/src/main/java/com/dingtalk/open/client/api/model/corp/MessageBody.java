/*     */ package com.dingtalk.open.client.api.model.corp;
/*     */ 
/*     */ import com.dingtalk.open.client.common.ParamAttr;
/*     */ import com.dingtalk.open.client.common.ParamAttr.Location;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class MessageBody implements Serializable
/*     */ {
/*     */   @ParamAttr(location=ParamAttr.Location.JSON_CONTENT, paramKey="text")
/*     */   public static class TextBody extends MessageBody
/*     */   {
/*     */     private String content;
/*     */     
/*     */     public String getContent()
/*     */     {
/*  17 */       return this.content;
/*     */     }
/*     */     
/*     */     public void setContent(String content) {
/*  21 */       this.content = content;
/*     */     }
/*     */   }
/*     */   
/*     */   @ParamAttr(location=ParamAttr.Location.JSON_CONTENT, paramKey="image")
/*     */   public static class ImageBody extends MessageBody
/*     */   {
/*     */     private String media_id;
/*     */     
/*     */     public String getMedia_id() {
/*  31 */       return this.media_id;
/*     */     }
/*     */     
/*     */     public void setMedia_id(String media_id) {
/*  35 */       this.media_id = media_id;
/*     */     }
/*     */   }
/*     */   
/*     */   @ParamAttr(location=ParamAttr.Location.JSON_CONTENT, paramKey="voice")
/*     */   public static class VoiceBody extends MessageBody
/*     */   {
/*     */     private String media_id;
/*     */     
/*     */     public String getMedia_id() {
/*  45 */       return this.media_id;
/*     */     }
/*     */     
/*     */     public void setMedia_id(String media_id) {
/*  49 */       this.media_id = media_id;
/*     */     }
/*     */   }
/*     */   
/*     */   @ParamAttr(location=ParamAttr.Location.JSON_CONTENT, paramKey="file")
/*     */   public static class FileBody extends MessageBody
/*     */   {
/*     */     private String media_id;
/*     */     
/*     */     public String getMedia_id() {
/*  59 */       return this.media_id;
/*     */     }
/*     */     
/*     */     public void setMedia_id(String media_id) {
/*  63 */       this.media_id = media_id;
/*     */     }
/*     */   }
/*     */   
/*     */   @ParamAttr(location=ParamAttr.Location.JSON_CONTENT, paramKey="link")
/*     */   public static class LinkBody extends MessageBody
/*     */   {
/*     */     private String messageUrl;
/*     */     private String picUrl;
/*     */     private String title;
/*     */     private String text;
/*     */     
/*     */     public String getMessageUrl() {
/*  76 */       return this.messageUrl;
/*     */     }
/*     */     
/*     */     public void setMessageUrl(String messageUrl) {
/*  80 */       this.messageUrl = messageUrl;
/*     */     }
/*     */     
/*     */     public String getPicUrl() {
/*  84 */       return this.picUrl;
/*     */     }
/*     */     
/*     */     public void setPicUrl(String picUrl) {
/*  88 */       this.picUrl = picUrl;
/*     */     }
/*     */     
/*     */     public String getTitle() {
/*  92 */       return this.title;
/*     */     }
/*     */     
/*     */     public void setTitle(String title) {
/*  96 */       this.title = title;
/*     */     }
/*     */     
/*     */     public String getText() {
/* 100 */       return this.text;
/*     */     }
/*     */     
/*     */     public void setText(String text) {
/* 104 */       this.text = text;
/*     */     }
/*     */   }
/*     */   
/*     */   @ParamAttr(location=ParamAttr.Location.JSON_CONTENT, paramKey="oa")
/*     */   public static class OABody extends MessageBody
/*     */   {
/*     */     private String message_url;
/*     */     private Head head;
/*     */     private Body body;
/*     */     
/*     */     public String getMessage_url() {
/* 116 */       return this.message_url;
/*     */     }
/*     */     
/*     */     public void setMessage_url(String message_url) {
/* 120 */       this.message_url = message_url;
/*     */     }
/*     */     
/*     */     public Head getHead() {
/* 124 */       return this.head;
/*     */     }
/*     */     
/*     */     public void setHead(Head head) {
/* 128 */       this.head = head;
/*     */     }
/*     */     
/*     */     public Body getBody() {
/* 132 */       return this.body;
/*     */     }
/*     */     
/*     */     public void setBody(Body body) {
/* 136 */       this.body = body;
/*     */     }
/*     */     
/*     */     public static class Head implements Serializable
/*     */     {
/*     */       private String bgcolor;
/*     */       private String text;
/*     */       
/*     */       public String getBgcolor() {
/* 145 */         return this.bgcolor;
/*     */       }
/*     */       
/*     */       public void setBgcolor(String bgcolor) {
/* 149 */         this.bgcolor = bgcolor;
/*     */       }
/*     */       
/*     */       public String getText() {
/* 153 */         return this.text;
/*     */       }
/*     */       
/*     */       public void setText(String text) {
/* 157 */         this.text = text;
/*     */       }
/*     */     }
/*     */     
/*     */     public static class Body implements Serializable
/*     */     {
/*     */       private String title;
/*     */       private String content;
/*     */       private String image;
/*     */       private String file_count;
/*     */       private String author;
/*     */       private List<Form> form;
/*     */       private Rich rich;
/*     */       
/*     */       public static class Form implements Serializable
/*     */       {
/*     */         private String key;
/*     */         private String value;
/*     */         
/*     */         public String getKey()
/*     */         {
/* 178 */           return this.key;
/*     */         }
/*     */         
/*     */         public void setKey(String key) {
/* 182 */           this.key = key;
/*     */         }
/*     */         
/*     */         public String getValue() {
/* 186 */           return this.value;
/*     */         }
/*     */         
/*     */         public void setValue(String value) {
/* 190 */           this.value = value;
/*     */         }
/*     */       }
/*     */       
/*     */       public static class Rich implements Serializable
/*     */       {
/*     */         private String num;
/*     */         private String unit;
/*     */         
/*     */         public String getNum()
/*     */         {
/* 201 */           return this.num;
/*     */         }
/*     */         
/*     */         public void setNum(String num) {
/* 205 */           this.num = num;
/*     */         }
/*     */         
/*     */         public String getUnit() {
/* 209 */           return this.unit;
/*     */         }
/*     */         
/*     */         public void setUnit(String unit) {
/* 213 */           this.unit = unit;
/*     */         }
/*     */       }
/*     */       
/*     */       public String getTitle()
/*     */       {
/* 219 */         return this.title;
/*     */       }
/*     */       
/*     */       public void setTitle(String title) {
/* 223 */         this.title = title;
/*     */       }
/*     */       
/*     */       public String getContent() {
/* 227 */         return this.content;
/*     */       }
/*     */       
/*     */       public void setContent(String content) {
/* 231 */         this.content = content;
/*     */       }
/*     */       
/*     */       public String getImage() {
/* 235 */         return this.image;
/*     */       }
/*     */       
/*     */       public void setImage(String image) {
/* 239 */         this.image = image;
/*     */       }
/*     */       
/*     */       public String getFile_count() {
/* 243 */         return this.file_count;
/*     */       }
/*     */       
/*     */       public void setFile_count(String file_count) {
/* 247 */         this.file_count = file_count;
/*     */       }
/*     */       
/*     */       public String getAuthor() {
/* 251 */         return this.author;
/*     */       }
/*     */       
/*     */       public void setAuthor(String author) {
/* 255 */         this.author = author;
/*     */       }
/*     */       
/*     */       public List<Form> getForm() {
/* 259 */         return this.form;
/*     */       }
/*     */       
/*     */       public void setForm(List<Form> form) {
/* 263 */         this.form = form;
/*     */       }
/*     */       
/*     */       public Rich getRich() {
/* 267 */         return this.rich;
/*     */       }
/*     */       
/*     */       public void setRich(Rich rich) {
/* 271 */         this.rich = rich;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              F:\temp\client-sdk.api-1.0.0-SNAPSHOT.jar!\com\dingtalk\open\client\api\model\corp\MessageBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
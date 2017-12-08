package com.dingtalk.open.client.api.model.corp;

import com.google.common.base.Objects;

import java.io.Serializable;

public class CorpAdmin implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userid;
    private Integer sys_level;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getSys_level() {
        return sys_level;
    }

    public void setSys_level(Integer sys_level) {
        this.sys_level = sys_level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorpAdmin corpAdmin = (CorpAdmin) o;
        return Objects.equal(userid, corpAdmin.userid) &&
                Objects.equal(sys_level, corpAdmin.sys_level);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (this.userid == null ? 0 : this.userid.hashCode());
        result = prime * result + (this.sys_level == null ? 0 : this.sys_level.hashCode());
        return result;
    }
}

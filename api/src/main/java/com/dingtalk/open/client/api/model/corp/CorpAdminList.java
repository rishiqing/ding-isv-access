package com.dingtalk.open.client.api.model.corp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user 毛文强
 * Date: 2017/11/30
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public class CorpAdminList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CorpUser> adminList;

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (this.adminList == null ? 0 : this.adminList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CorpAdminList)) return false;
        CorpAdminList other = (CorpAdminList)obj;
        if(this.adminList == null){
            if(other.adminList != null) return false;
        }else if(!this.adminList.equals(other.adminList)){
            return false;
        }
        return true;
    }
}

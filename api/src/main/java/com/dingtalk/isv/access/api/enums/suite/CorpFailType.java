package com.dingtalk.isv.access.api.enums.suite;

/**
 * 企业除授权key之外，其他失败类型
 * Created by Wallace on 17-1-3.
 */
public enum CorpFailType {

    /**获取根部门失败**/
    GET_ROOT_DEPT("get_root_dept"),
    /**获取递归字部门失败**/
    GET_CHILDREN_DEPTS("get_children_depts"),
    /**获取部门成员失败**/
    GET_DEPT_STAFF("get_dept_staff"),
    //  将本地的部门信息同步到ISV（比如日事清）中失败
    PUT_ISV_DEPT("put_rsq_dept"),
    //  将本地的部门成员信息同步到ISV（比如日事清）中失败
    PUT_ISV_DEPT_STAFF("put_rsq_dept_staff");

    private final String key;

    private CorpFailType(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static CorpFailType getCorpFailType(String key){
        CorpFailType[] corpFaileTypeArr = CorpFailType.values();
        for (CorpFailType o : corpFaileTypeArr) {
            if (o.getKey().equalsIgnoreCase(key)) {
                return o;
            }
        }
        return null;
    }
}
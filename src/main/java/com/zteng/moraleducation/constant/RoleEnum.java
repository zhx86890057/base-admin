package com.zteng.moraleducation.constant;

public enum RoleEnum {
    ADMIN("admin","学校管理员"),
    LEADER("leader", "值周领导"),
    TEACHER("teacher", "值周老师"),
    HEAD_TEACHER("headTeacher", "班主任"),
    CLASS_TEACHER("classTeacher", "任课老师"),
            ;
    private String code;
    private String name;
    RoleEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

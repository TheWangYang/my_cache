package com.github.thewangyang.my_cache.core.constant.enums;


//创建cache删除类型枚举类
//删除类型
public enum MyCacheRemoveType {

    //枚举删除类型：过期、淘汰
    //定义枚举类中包含的枚举元素
    EXPIRE("expire", "过期"), EVICT("evict", "淘汰");

    //定义成员变量
    private final String code;
    private final String desc;

    //创建构造函数
    MyCacheRemoveType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    //code getter函数
    public String getCode(){
        return code;
    }

    //desc getter函数
    public String getDesc(){
        return desc;
    }

    @Override
    public String toString(){
        //返回toString函数的结果
        return "CacheRemoveType{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

}

package com.github.thewangyang.my_cache.core.constant.enums;

/**
 * 定义删除类型枚举类
 * @author thewangyang
 * @since 0.0.1
 */
public enum CacheRemoveType {

    EXPIRE("expire", "过期"),
    EVICT("evict", "淘汰"),;

    private final String code;
    private final String desc;


    CacheRemoveType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    @Override
    public String toString() {
        return "CacheRemoveType{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

}

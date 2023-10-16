package com.github.thewangyang.my_cache.core.model;


import java.util.Arrays;


// 定义持久化cache对象的持久化Entry类
// 其中记录了aof对象的详细信息
public class PersistAofEntry {

    //定义参数信息
    public Object [] params;

    //定义方法名称
    public String methodName;

    //新建对象实例
    public static PersistAofEntry newInstance(){
        return new PersistAofEntry();
    }

    //params的getter方法
    public Object[] getParams() {
        return params;
    }

    //params的setter方法

    public void setParams(Object[] params) {
        this.params = params;
    }


    //定义methodName的getter和setter方法
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    //重写toString()方法

    @Override
    public String toString() {
        return "PersistAofEntry{" +
                "params=" + Arrays.toString(params) +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}

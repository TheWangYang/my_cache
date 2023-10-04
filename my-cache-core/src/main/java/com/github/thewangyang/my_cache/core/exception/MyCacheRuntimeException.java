package com.github.thewangyang.my_cache.core.exception;


//定义缓存运行时异常
//继承自RuntimeException
public class MyCacheRuntimeException extends RuntimeException{
    //原始构造函数
    public MyCacheRuntimeException(){

    }

    //定义包含参数的构造函数
    public MyCacheRuntimeException(String message){
        super(message);
    }

    //定义包含两个参数的构造函数
    //其中第二个参数为抛出异常的原因
    public MyCacheRuntimeException(String message, Throwable cause){
        super(message, cause);
    }

    //定义只包含cause的构造函数
    public MyCacheRuntimeException(Throwable cause){
        super(cause);
    }

    //定义包含四个参数的构造函数
    public MyCacheRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

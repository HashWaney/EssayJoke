package com.hc.baselibrary.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//放在什么位置 ElementType.METHOD 方法上面
@Retention(RetentionPolicy.RUNTIME)//是编译时检测还是运行时检测
public @interface PermissionSucceed {
    public int requestCode();//请求码
}
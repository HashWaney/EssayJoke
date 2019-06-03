package com.hc.baselibrary.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PermissionUtils {
    //这个类里面所有的都是静态方法  所以不能让别人去new对象
    private  PermissionUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断其是不是6.0以上的版本
     * Marshmallow 棉花糖 6.0
     * @return
     */
    public static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void executeSucceedMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();

        for(Method method:methods){
            Log.i(TAG, "executeSucceedMethod: method" + method);
            //获取该方法上面有没有打这个成功的标记
            PermissionSucceed succeedMethod = method.getAnnotation(PermissionSucceed.class);
            if(succeedMethod != null){
                //代表该方法打了标记
                //并且我们的请求码必须requestCode 一样

                int methodCode = succeedMethod.requestCode();
                if(methodCode == requestCode){
                    //这个就是我们要找的成功方法
                    //反射执行该方法
                    Log.i(TAG, "executeSucceedMethod: 找到了该方法：" + method);
                    executeMethod(method ,object);
                }
            }
        }


    }

    /**
     * 反射执行该方法
     * @param method
     * @param object
     */

    private static void executeMethod(Method method, Object object) {
        //反射执行方法，第一个是传该方法是属于哪个类 第二个参数是传参数
        try {
            method.setAccessible(true);
            method.invoke(object , new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     * @param object  Activity or fragment
     * @param requestPermissions
     * @return 没有授予过的权限
     */
    public static List<String> getDeniedPermissions(Object object, String[] requestPermissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission : requestPermissions){
            //把拒绝的权限加入到集合
            if(ContextCompat.checkSelfPermission(getActivity(object) ,requestPermission) == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(requestPermission);
            };
        }

        return  deniedPermissions;

    }

    /**
     * 获取Context
     * @param object
     * @return
     */
    public static Activity getActivity(Object object) {
        if(object instanceof Activity){
            return (Activity)object;
        }
        if(object instanceof Fragment){
            return ((Fragment) object).getActivity();
        }
        return null;
    }

    /**
     * 执行失败的方法
     * @param object
     * @param requestCode
     */
    public static void executFailMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();

        for(Method method:methods){
            Log.i(TAG, "executFailMethod: method" + method);
            //获取该方法上面有没有打这个失败的标记
            PermissionFail failMethod = method.getAnnotation(PermissionFail.class);
            if(failMethod != null){
                //代表该方法打了标记
                //并且我们的请求码必须requestCode 一样

                int methodCode = failMethod.requestCode();
                if(methodCode == requestCode){
                    //这个就是我们要找的成功方法
                    //反射执行该方法
                    Log.i(TAG, "executFailMethod: 找到了失败的方法：" + method);
                    executeMethod(method ,object);
                }
            }
        }
    }
}

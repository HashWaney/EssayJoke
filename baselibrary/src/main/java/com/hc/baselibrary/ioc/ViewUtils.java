package com.hc.baselibrary.ioc;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by hui on 2016/10/8.
 */
public class ViewUtils {

    public static void inject(Activity activity){
        // 处理ViewById注解
        findViewById(activity,new ViewFinder(activity));
        // 处理OnClick注解
        onClick(activity,new ViewFinder(activity));
    }


    public static void inject(View view) {
        findViewById(view,new ViewFinder(view));
        onClick(view,new ViewFinder(view));
    }

    /**
     * 用在Fragment
     */
    public static void inject(Object handleObj, View view) {
        findViewById(handleObj,new ViewFinder(view));
        onClick(handleObj,new ViewFinder(view));
    }


    /**
     * 处理OnClick注解
     */
    private static void onClick(final Object handleObj, ViewFinder viewFinder) {
        // findViewById  setOnClick
        // 1.获取该Activity的所有方法
        Class<?> clazz = handleObj.getClass();

        Method[] methods = clazz.getDeclaredMethods();

        // 2.遍历方法获取所有的值
        for (final Method method:methods){
            // 2.1 获取OnClick注解
            OnClick onClick = method.getAnnotation(OnClick.class);

            // 2.2 该方法上是否有OnClick注解
            if(onClick != null){
                // 2.3 获取OnClick里面所有的值
                int[] viewIds = onClick.value();// @OnClick({R.id.text_view,R.id.button})

                // 2.4 先findViewById , setOnclick
                for (int viewId:viewIds){
                    // 先findViewById
                    final View view = viewFinder.findViewById(viewId);
                    // 后设置setOnclick
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 首先需要判断 方法是否需要检测网络
                            CheckNet checkNet = method.getAnnotation(CheckNet.class);
                            // 判断有没有配置需要检测网络的注解
                            if(checkNet != null){
                                // 需要检测网络
                                if(!NetManagerUtil.isOpenNetwork(v.getContext())){
                                    // 当前无网络
                                    Toast.makeText(v.getContext(), "当前无网络~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }


                            // 3.反射调用原来配置了OnClick的方法
                            method.setAccessible(true);// 私有的方法

                            try {
                                method.invoke(handleObj);// 调用无参的方法
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    method.invoke(handleObj,view);// 调用有参的方法 view 代表当前点击的View
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * 解决注解ViewByID
     *
     * viewFinder findViewById
     * handleObj 从哪个类里面反射
     */
    private static void findViewById(Object handleObj, ViewFinder viewFinder) {
        // 1.获取该Activity的所有属性
        Class<?> clazz = handleObj.getClass();

        //  clazz.getFields() 获取public
        Field[] fields =  clazz.getDeclaredFields();// 获取该类的所有属性  包括私有等等

        // 2.循环判断该属性上是否有ViewById注解
        for(Field field:fields){
            //  2.1  判断该属性上是否有ViewById注解
            //  2.1 获取ViewByID注解
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById != null){
                // 该属性上有viewById
                // 2.2 获取   @ViewById(R.id.text_view)  R.id.text_view的值
                int viewId = viewById.value();

                // 3.利用activity的findViewById 动态注入给属性
                View view = viewFinder.findViewById(viewId);

                // 3.1 动态注入给属性
                try {
                    // java.lang.IllegalAccessException: access to field not allowed
                    field.setAccessible(true);// 私有属性可以动态注入
                    // activity 该属性在哪一个类   view 给该属性设置的值
                    field.set(handleObj,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

package com.github.codingdong.reflection;

import com.github.codingdong.reflection.dialect.AbstractDialect;
import com.github.codingdong.reflection.parser.Parser;
import com.github.codingdong.reflection.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>Description: </p>
 *
 * @author dbx
 * @date 2020/6/17 16:38
 * @since JDK1.8
 */
public class DialectFactory {

    private static final Map<String, Class<?>> DIALECT_MAP = new HashMap<>();

    public void registerClassByPackageName(String packageName) {

        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);

        for (Class<?> clazz : classes) {

            if (AbstractDialect.class.isAssignableFrom(clazz)) {
                try {
                    AbstractDialect dialect = (AbstractDialect) clazz.newInstance();

                    DIALECT_MAP.put(dialect.getAliasName(), clazz);

                } catch (Exception e) {
                    e.printStackTrace();
                    // log.warn("实例化对象失败");
                }
            }
        }
    }

    public void registerClassByPackageName(String packageName,Class<?> aClass) {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);

        for (Class<?> clazz : classes) {

            if (aClass.isAssignableFrom(clazz)) {
                try {
                    Object obj =  clazz.newInstance();
                    Method[] methods = obj.getClass().getMethods();
                    String aliasName = "";
                    for (Method method : methods) {
                        if ("getAliasName".equals(method.getName())) {
                            aliasName = (String)method.invoke(obj);
                        }
                    }

                    DIALECT_MAP.put(aliasName, clazz);

                } catch (Exception e) {
                    e.printStackTrace();
                    // log.warn("实例化对象失败");
                }
            }
        }
    }

    public void registerClassByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {

        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);

        for (Class<?> clazz : classes) {

            if (clazz.isAnnotationPresent(annotationClass)) {//指定类型的注解是否存在
                //获取该类型上的注解
                Annotation annotation = clazz.getAnnotation(annotationClass);

                if (annotation != null) {
                    Method[] methods = annotation.annotationType().getDeclaredMethods();

                    if (methods.length == 1) {//只有一个属性方法

                        if (!methods[0].isAccessible()) {
                            //setAccessible是启用和禁用访问安全检查的开关
                            methods[0].setAccessible(true);
                        }

                        try {
                            String aliasName = (String) methods[0].invoke(annotation);
                            DIALECT_MAP.put(aliasName, clazz);

                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

        }
    }

    public Class<?> getRegisterClass(String aliasName) {
        return DIALECT_MAP.get(aliasName);
    }
}



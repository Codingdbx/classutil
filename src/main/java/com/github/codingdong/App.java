package com.github.codingdong;

import com.github.codingdong.reflection.DialectFactory;
import com.github.codingdong.reflection.parser.Parser;

import java.lang.reflect.Method;

/**
 * <p>Description: </p>
 *
 * @author dbx
 * @date 2020/6/21 11:47
 * @since JDK1.8
 */
public class App {

    public static void main(String[] args) {
        System.out.println("start scan package---------------");
        DialectFactory factory = new DialectFactory();

        //注册所有类型
        factory.registerClassByAnnotation("com.github.codingdong.reflection.parser", Parser.class);

        Class<?> mysql = factory.getRegisterClass("mysql");

        if (mysql != null) {
            try {
                Object obj = mysql.newInstance();

                Method[] deals = mysql.getMethods();

                deals[0].invoke(obj);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

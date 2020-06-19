package com.github.codingdong;

import com.github.codingdong.reflection.AbstractDialect;
import com.github.codingdong.reflection.DialectFactory;

import java.net.URL;

/**
 * <p>Description: </p>
 *
 * @author dbx
 * @date 2020/6/19 15:10
 * @since JDK1.8
 */
public class App {

    public static void main(String[] args) {

        App app = new App();
        URL url = app.getClass().getResource("/test.txt");

        System.out.println("resources------------"+ url.getFile());

        System.out.println("start---------------");
        DialectFactory factory = new DialectFactory();

        //注册所有类型
        factory.registerDialect("com.github.codingdong.reflection.dialect");

        Class<?> mysql = factory.getDialectClass("mysql");

        AbstractDialect dialect;
        try {
            if (mysql != null) {
                dialect = (AbstractDialect)mysql.newInstance();
                String aliasName = dialect.getAliasName();

                dialect.deal();

                System.out.println("aliasName---" + aliasName);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

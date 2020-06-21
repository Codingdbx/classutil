package com.github.codingdong;

import static org.junit.Assert.assertTrue;

import com.github.codingdong.reflection.dialect.AbstractDialect;
import com.github.codingdong.reflection.DialectFactory;
import com.github.codingdong.reflection.parser.MysqlParser;
import com.github.codingdong.reflection.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }



    @Test
    public void testJarFile() {

        try {
            URL url = new URL("jar:file:/C:/Users/Administrator/Desktop/classutil-1.0-SNAPSHOT.jar!/");

            String protocol = url.getProtocol();


            JarFile jar = new JarFile("C:\\Users\\Administrator\\Desktop\\classutil-1.0-SNAPSHOT.jar");
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // 如果是以/开头的
                if (name.charAt(0) == '/') {
                    // 获取后面的字符串
                    name = name.substring(1);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        AppTest app = new AppTest();
        URL url = app.getClass().getResource("/test.class");

        URL url2 = app.getClass().getResource(File.separatorChar+"test.class");

        char a ='/';
        char b =File.separatorChar;

        int c = a;
        int d = b;
        int k ='\\';

        System.out.println("resources------------"+ url.getFile());

        System.out.println("start---------------");
        DialectFactory factory = new DialectFactory();

        //注册所有类型
        factory.registerClassByPackageName("com.github.codingdong.reflection.dialect");

        Class<?> mysql = factory.getRegisterClass("mysql");

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

    @Test
    public void test2() {
        System.out.println("start---------------");
        DialectFactory factory = new DialectFactory();

        //注册所有类型
        factory.registerClassByPackageName("com.github.codingdong.reflection.dialect",AbstractDialect.class);

        Class<?> mysql = factory.getRegisterClass("mysql");

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

    @Test
    public void testAnnotation() {

        System.out.println("start---------------");
        DialectFactory factory = new DialectFactory();

        //注册所有类型
        factory.registerClassByAnnotation("com.github.codingdong.reflection.parser",Parser.class);

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

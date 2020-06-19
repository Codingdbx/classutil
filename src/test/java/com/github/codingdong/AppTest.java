package com.github.codingdong;

import static org.junit.Assert.assertTrue;

import com.github.codingdong.reflection.AbstractDialect;
import com.github.codingdong.reflection.DialectFactory;
import org.junit.Test;

import java.io.IOException;
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
        DialectFactory factory = new DialectFactory();

        //注册所有类型
        factory.registerDialect("com.github.codingdong.reflection.dialect");

        Class<?> mysql = factory.getDialectClass("mysql");

        AbstractDialect dialect;
        try {
            if (mysql != null) {
                dialect = (AbstractDialect) mysql.newInstance();
                String aliasName = dialect.getAliasName();

                dialect.deal();

                System.out.println("aliasName---" + aliasName);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}

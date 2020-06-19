package com.github.codingdong.reflection.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>Description: </p>
 *
 * @author dbx
 * @date 2020/6/18 9:30
 * @since JDK1.8
 */
public final class ClassUtil {

    /**
     * 扫描某个包名下的所有class文件或是扫描jar文件
     *
     * @param packageName 包名
     * @return 所有 Class<?>
     */
    public static Set<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, true);
    }

    /**
     * 扫描某个包名下的所有class文件或是扫描jar文件
     *
     * @param packageName 包名
     * @param recursive  是否循环递归
     * @return 所有 Class<?>
     */
    public static Set<Class<?>> scanPackage(String packageName, boolean recursive) {

        Set<Class<?>> classes = new HashSet<>();

        String packageDirName = packageName.replace('.', '/');

        System.out.println("packageDirName---------------" + packageDirName);
        Enumeration<URL> urls;
        try {
            urls = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (urls.hasMoreElements()) {

                // 获取下一个元素
                URL url = urls.nextElement();
                System.out.println("url---------------" + url.toString());
                // 得到协议的名称
                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    System.err.println("jar类型的扫描");
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    //jar方式
                    findClassesInPackageByJar(jar, packageName, packageDirName, recursive, classes);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有class
     *
     * @param packageName 包名
     * @param packagePath 所在路径
     * @param recursive   是否递归子文件
     * @param classes     Class<?>
     */
    private static void findClassesInPackageByFile(String packageName, String packagePath, boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }

        // 自定义过滤规则 如果可以循环递归(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));

        if (files == null) {
            return;
        }

        for (File file : files) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    //classes.add(Class.forName(packageName + '.' + className));

                    //经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));

                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 查找jar中的class文件
     * @param jar
     * @param packageName
     * @param packageDirName
     * @param recursive
     * @param classes
     */
    private static void findClassesInPackageByJar(JarFile jar,String packageName,String packageDirName, boolean recursive, Set<Class<?>> classes){
        // 从此jar包 得到一个枚举类
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

            // 如果前半部分和定义的包名相同
            if (name.startsWith(packageDirName)) {
                int index = name.lastIndexOf('/');
                // 如果以"/"结尾 是一个包
                if (index != -1) {
                    // 获取包名 把"/"替换成"."
                    packageName = name.substring(0, index).replace('/', '.');
                }
                // 如果可以循环递归下去 并且是一个包
                if (index != -1 || recursive) {
                    // 如果是一个.class文件 而且不是目录
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        // 去掉后面的".class" 获取真正的类名
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        try {
                            // 添加到classes
                            //classes.add(Class.forName(packageName + '.' + className));
                            classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            // log.info("添加用户自定义视图类错误 找不到此类的.class文件");
                        }
                    }
                }
            }
        }
    }

}

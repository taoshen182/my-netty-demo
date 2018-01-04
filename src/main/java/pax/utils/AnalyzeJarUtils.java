package pax.utils;

import org.apache.log4j.Logger;
import tool.dao.BizObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Created by want on 2017/6/26.
 */
public class AnalyzeJarUtils {
    Logger logger = Logger.getLogger(AnalyzeJarUtils.class);

    public static List<String> getAllClassNamesByPath(String file_path) throws IOException, ClassNotFoundException {
        return getAllClassNamesByFile(new File(file_path));
    }

    public static List<String> getAllClassNamesByFile(File jar_file) throws IOException, ClassNotFoundException {
        List<String> nameList = new ArrayList<>();

        //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
        URL url1 = jar_file.toURI().toURL();
        URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());


        //通过jarFile和JarEntry得到所有的类
        JarFile jar = new JarFile(jar_file);
        //返回zip文件条目的枚举
        Enumeration<JarEntry> enumFiles = jar.entries();
        JarEntry entry;
        //测试此枚举是否包含更多的元素
        while (enumFiles.hasMoreElements()) {
            entry = (JarEntry) enumFiles.nextElement();
            if (entry.getName().indexOf("META-INF") < 0) {
                String classFullName = entry.getName();
                if (!classFullName.endsWith(".class")) {
                    classFullName = classFullName.substring(0, classFullName.length() - 1);
                } else {
                    //去掉后缀.class
                    String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                    Class<?> myclass = myClassLoader.loadClass(className);
                    //打印类名
                    System.out.println("*****************************");
                    System.out.println("全类名:" + className);
                    nameList.add(className);
                    //得到类中包含的属性
//                    Method[] methods = myclass.getMethods();
//                    for (Method method : methods) {
//                        String methodName = method.getName();
//                        System.out.println("方法名称:" + methodName);
//                        Class<?>[] parameterTypes = method.getParameterTypes();
//                        for (Class<?> clas : parameterTypes) {
//                            // String parameterName = clas.getName();
//                            String parameterName = clas.getSimpleName();
//                            System.out.println("参数类型:" + parameterName);
//                        }
//                        System.out.println("==========================");
//                    }
                }
            }
        }

        return nameList;
    }

    /**
     * 测试
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String jarFile = "C:\\Users\\want\\Desktop\\TMS资料\\PaxRule.jar";

        //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
        File f = new File(jarFile);
        URL url1 = f.toURI().toURL();
        URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());


        //通过jarFile和JarEntry得到所有的类
        JarFile jar = new JarFile(jarFile);
        //返回zip文件条目的枚举
        Enumeration<JarEntry> enumFiles = jar.entries();
        JarEntry entry;
        //测试此枚举是否包含更多的元素
        while (enumFiles.hasMoreElements()) {
            entry = (JarEntry) enumFiles.nextElement();
            if (entry.getName().indexOf("META-INF") < 0) {
                String classFullName = entry.getName();
                if (!classFullName.endsWith(".class")) {
                    classFullName = classFullName.substring(0, classFullName.length() - 1);
                } else {
                    //去掉后缀.class
                    String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                    Class<?> myclass = myClassLoader.loadClass(className);
                    //打印类名
                    System.out.println("*****************************");
                    System.out.println("全类名:" + className);

                    //得到类中包含的属性
                    Method[] methods = myclass.getMethods();
                    for (Method method : methods) {
                        String methodName = method.getName();
                        System.out.println("方法名称:" + methodName);
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (Class<?> clas : parameterTypes) {
                            // String parameterName = clas.getName();
                            String parameterName = clas.getSimpleName();
                            System.out.println("参数类型:" + parameterName);
                        }
                        System.out.println("==========================");
                    }
                }
            }
        }
    }
}


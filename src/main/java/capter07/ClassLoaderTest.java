package capter07;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类加载器与instanceof关键字演示
 * 同一个class，使用不同的类加载器加载，不想等
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".")+1)+".class";
                InputStream inputStream = getClass().getResourceAsStream(fileName);
                if(inputStream==null){
                    return super.loadClass(name);
                }
                byte[] b = new byte[1];
                try {
                    b = new byte[inputStream.available()];
                    inputStream.read(b);
                    return defineClass(name,b,0,b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        Object obj = classLoader.loadClass("capter07.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof ClassLoaderTest);
    }

    /**
     * 模仿双亲委派模型实现自己的类加载器
     */
    public static void myDIYClassLoader(){
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name,boolean resolve) throws ClassNotFoundException {
                //首先，检查请求的类是否已经被加载过
                Class c = findLoadedClass(name);
                if(c==null){
                    try {
                        c = super.loadClass(name);
                    }catch (ClassNotFoundException e){
                        //如果父类加载器抛出ClassNotFoundException
                        //说明父类加载器无法完成加载请求
                    }
                    if(c==null){
                        //在父类加载器无法加载的时候，再调用本身的findClass方法来进行类加载
                        c = findClass(name);
                    }
                }
                if(resolve){
                    resolveClass(c);
                }
                return c;
            }
        };
    }
}

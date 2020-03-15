package shengsiyuan.capter01;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自定义类加载器
 */
public class MyClassLoader extends ClassLoader{

    private String classLoaderName;
    private static final String extention = ".class";

    public MyClassLoader(String classLoaderName){
        super();//将系统类加载器作为父类加载器
        this.classLoaderName = classLoaderName;
    }

    public MyClassLoader(String classLoaderName,ClassLoader parent){
        super(parent);//指定父类加载器
        this.classLoaderName = classLoaderName;
    }

    @Override
    public String toString() {
        return "MyClassLoader{" +
                "classLoaderName='" + classLoaderName + '\'' +
                '}';
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = this.loadClassData(name);
        return this.defineClass(name,b,0,b.length);
    }

    private byte[] loadClassData(String name){
        // com.shengsiyuan.Test-->com/shengsiyuan/Test.class
        String path = name.replace(".","/")+extention;
        byte[] data = null;
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try{
            inputStream = new FileInputStream(new File(path));
            int d ;
            while (-1 !=(d = inputStream.read())){
                outputStream.write(d);
            }
            data = outputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null!=inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public void test(ClassLoader classLoader)throws Exception{
        Class<?> loadClass = classLoader.loadClass("shengsiyuan.capter01.Test1");
        Object instance = loadClass.newInstance();
        System.out.println(instance);
        ((Test1)instance).test();
        System.out.println(instance.getClass().getClassLoader());
    }

    public static void main(String[] args)throws Exception {
        MyClassLoader classLoader = new MyClassLoader("test1");
        classLoader.test(classLoader);
    }
}

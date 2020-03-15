package shengsiyuan.capter01;

/**
 * 调用classloader的loadClass方法只会加载类，不会对类进行初始化
 * 使用Class.forName()加载类会对类进行初始化
 */
public class Test7 {
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> clazz =  classLoader.loadClass("shengsiyuan.capter01.GL");
        System.out.println(clazz);
        clazz = Class.forName("shengsiyuan.capter01.GL");
        System.out.println(clazz);
    }
}
class GL{
    static {
        System.out.println("invoke static block");
    }
}

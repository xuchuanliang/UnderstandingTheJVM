package shengsiyuan.capter01;

/**
 * 1.静态代码块只有在类被初始化的时候会执行
 * 2.对于静态字段而言，使用静态字段时，只有直接定义了该静态字段的类才会被初始化
 * 3.当一个类被初始化的时候，要求其父类都已经初始化完毕
 * -XX:+TraceClassLoading，用于追踪类的加载信息并打印出来
 */
public class Test1 {
    public static void main(String[] args) {
        //Parent1初始化，Child1不会初始化
        System.out.println(Child1.parentName);
    }
    public void test(){
        System.out.println("tttt");
    }
}
class Parent1{
    static String parentName = "parent";
    static {
        System.out.println("Parent1 init");
    }
}
class Child1 extends Parent1{
    static String childName = "child";
    static {
        System.out.println("Child1 init");
    }
}

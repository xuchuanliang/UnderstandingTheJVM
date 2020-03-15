package shengsiyuan.capter01;

/**
 * 常量（final）在编译阶段会存入到这个常量池的方法所在的类的常量池中
 * 本质上，调用类并没有直接引用到这个定义常量的类，因此不会触发定义常量
 * 类的初始化，
 * 注意：这里指的是将常量存放到了Test2的常量池中，只有的Test2与Parent2没有任何关系，甚至，我们可以将Parent2的class文件删除
 */
public class Test2 {
    public static void main(String[] args) {
        System.out.println(Parent2.parentName);
    }
}

class Parent2{
    final static String parentName = "parent";
    static {
        System.out.println("Parent1 init");
    }
}

package capter07;

/**
 *
 */
public class Test {
    public static void main(String[] args){
        /**
         * 非主动使用类字段演示
         * 只会输出SubClass init!。对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过子类来引用父类中定义的静态字段，
         * 只会触发父类的初始化而不会触发子类的初始化。通过-XX:TraceClassLoading参数可以观察到在运行过程中类的加载
         */
//        System.out.println(SubClass.value);
        /**
         * 被动使用类字段演示二：通过数组定义来引用类，不会触发此类的初始化
         */
//        SuperClass[] superClasses = new SuperClass[10];

        /**
         * 被动使用类字段演示三：常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会
         * 触发定义常量的类的初始化
         * 因为在java源码中引用了ConstClass类中的常量HELLOWORLD，但其实在编译阶段通过常量传播优化，已经将此常量的
         * 值“hello world”存储到了Test类的常量池中，以后Test对常量ConstClass.HELLOWORLD的引用实际上都被转化成
         * Test类对自身常量池的引用了。也就是说，实际上Test的Class文件之中并没有ConstClass类的符号引用入口，这两个
         * 类在编译成Class之后就不存在任何联系了。
         */
        System.out.println(ConstClass.HELLOWORLD);
    }
}

/**
 * 被动使用类字段演示一：通过子类引用父类的静态字段，不会导致子类初始化
 */
class SuperClass{
    static {
        System.out.println("SuperClass init");
    }
    public static int value = 123;
}

class SubClass extends SuperClass{
    static {
        System.out.println("SubClass init!");
    }
}

/**
 *
 */
class ConstClass{
    static {
        System.out.println("ConstClass init!");
    }
    public static final String HELLOWORLD = "hello world";
}

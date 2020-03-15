package book.capter01;

/**
 * java虚拟机栈：描述的是java方法执行的内存模型：每个方法在执行的时候都会创建一个栈帧用于存储局部变量、操作数栈、动态链接、方法出入口等信息，
 * 每一个方法在调用直至执行完成的过程，对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。我们常说的java内存区分为栈内存和堆内存，这里说的栈内存
 * 指的就是java虚拟机栈中的局部变量表部分，局部变量表存放了编译器可知的各种基本数据类型、对象引用类型（指向对象起始地址的引用指针）和
 * returnAddress类型（指向了一条字节码指令的地址），java虚拟机栈也是线程私有的
 *
 * 通过不断增加方法执行的深度，增加虚拟机栈的深度导致异常 StackOverflowError
 * VM Args:-Xss128k    设置虚拟机栈的最大内存是128k
 * java.lang.StackOverflowError
 */
public class JavaVMStackSOF {

    private int i = 1;

    public static void main(String[] args){
        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
//        try{
//            javaVMStackSOF.stackLeak();
//        }catch (Throwable t){
//            System.out.println("栈的深度是："+javaVMStackSOF.i);
//            t.printStackTrace();
//        }
        javaVMStackSOF.stackLeakByThread();
    }

    public void stackLeak(){
        i++;
        stackLeak();
    }

    public void stackLeakByThread(){
        while (true){
            Thread thread = new Thread(()->{
                dontSto();
            });
        }
    }

    private void dontSto(){
        while (true){}
    }
}

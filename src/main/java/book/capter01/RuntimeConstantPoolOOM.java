package book.capter01;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法区和常量池：是各个线程共享的内存区域，用来存放被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。被称为永久代。
 * 方法区用于存放Class的相关信息，如类名、访问修饰符、常量池、字段描述、方法描述等
 * 常量池内存溢出
 * VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M
 * Exception in thread "main" java.lang.OutOfMemoryError: PermGen spance
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args){
        //使用List保持着常量池引用，避免Full GC回收常量池行为
        List<String> list = new ArrayList<>();
        int i=0;
        while (true){
            list.add(String.valueOf(i++).intern());
        }
    }
}

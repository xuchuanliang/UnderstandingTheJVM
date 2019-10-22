package capter08;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * @author xuchuanliangbt
 * @title: MethodHandleTest
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/10/1813:18
 * @Version
 */
public class MethodHandleTest {

    static class ClassA{
        public void println(String s){
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Throwable {
        Object obj = System.currentTimeMillis() % 2 ==0 ? System.out : new ClassA();
        getPrintlnMH(obj).invokeExact("xcl");
    }
    private static MethodHandle getPrintlnMH(Object obj) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(void.class,String.class);
        return lookup().findVirtual(obj.getClass(),"println",methodType).bindTo(obj);
    }
}

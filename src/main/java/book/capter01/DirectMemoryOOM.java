package book.capter01;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author xuchuanliangbt
 * @title: DirectMemoryOOM
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/9/1019:57
 * @Version
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024*1024;
    public static void main(String[] args) throws IllegalAccessException {
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);
          Unsafe unsafe = (Unsafe) field.get(null);
        while (true){
            unsafe.allocateMemory(_1MB);
        }
    }
}

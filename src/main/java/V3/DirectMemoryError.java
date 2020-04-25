package V3;

import sun.misc.Unsafe;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;

/**
 * 直接内存OOM异常
 */
public class DirectMemoryError {
    private static int _1MB = 1024*1024;

    /**
     * -Xmx20m -XX:MaxDirectMemory=20M -XX:+PrintGCDetails
     * @param args
     */
    public static void main(String[] args) throws IllegalAccessException {
        Field field = Unsafe.class.getDeclaredFields()[0];
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        while (true){
            unsafe.allocateMemory(_1MB);
        }
    }
}

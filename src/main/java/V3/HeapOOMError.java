package V3;

import java.util.ArrayList;
import java.util.List;

/**
 * 模仿jvm堆内存溢出
 */
public class HeapOOMError {
    /**
     * -Xmx20m -Xms20m -XX:+HeapDumpOnOutOfMemory -XX:+PrintGCDetails
     * @param args
     */
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        while (true){
            users.add(new User());
        }
    }
}
class User{
    private byte[] _1M = new byte[1024];
}

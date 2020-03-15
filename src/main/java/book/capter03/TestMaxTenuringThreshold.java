package book.capter03;

import java.util.Map;

/**
 * 通过设置对象晋升到老年代的年龄
 */
public class TestMaxTenuringThreshold {
    private static final int _1MB = 1024*1024;

    public static void main(String[] args){
        testTenuringThreshold();
        getAllStackTrace();
    }

    /**
     * VM args:-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     *
     * VM args解释：-verbose:gc表示输出java虚拟机中GC的详细情况
     * -Xms20M:表示限制虚拟机堆内存最小是20M
     * -Xmx20M:表示限制虚拟机堆内存最大是20M
     * -Xmn10M:表示限制虚拟机的新生代区域为10M（包括一个Eden区域和两个Survivor区域）
     * -XX:PrintGCDetails:表示打印出GC详细信息
     * -XX:SurvivorRatio=8表示Eden区域与一个Survivor区域比例是8:1
     * -XX:MaxTenuringThreshold=1表示限制对象晋升到老年代的年龄是1岁
     *
     * 此时堆内存一共20M，新生代区域10M，其中Eden区域8M，两个Survivor区域分别是1M，老年代10M
     *
     * GC日志解析：VM args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
         [GC (Allocation Failure) --[PSYoungGen: 6783K->6783K(9216K)] 14975K->14983K(19456K), 0.0027136 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [Full GC (Ergonomics) [PSYoungGen: 6783K->0K(9216K)] [ParOldGen: 8200K->8905K(10240K)] 14983K->8905K(19456K), [Metaspace: 3320K->3320K(1056768K)], 0.0041530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         Heap
         PSYoungGen      total 9216K, used 4342K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         eden space 8192K, 53% used [0x00000000ff600000,0x00000000ffa3d8a0,0x00000000ffe00000)
         from space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
         to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
         ParOldGen       total 10240K, used 8905K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         object space 10240K, 86% used [0x00000000fec00000,0x00000000ff4b2478,0x00000000ff600000)
         Metaspace       used 3327K, capacity 4556K, committed 4864K, reserved 1056768K
         class space    used 361K, capacity 392K, committed 512K, reserved 1048576K
     1.进行了一次Minor GC，进行GC后新生代GC前后占用分别是6783K->6783K，堆内存GC前后占用分别是14975K->14983K
     2.进行了第二次GC，此时由于Eden区域剩余的内存不足够分配4M的allocation3，所以进行了一次Full GC,也就是Stop the World，
     由于Eden中的对象年龄已经1岁，所以直接移到了老年代，并且成功在新生代分配了allocation3


     */
    public static void testTenuringThreshold(){
        byte[] allocation1,allocation2,allocation3;
        allocation1 = new byte[_1MB*4];
        allocation2 = new byte[_1MB*4];
        allocation3 = new byte[_1MB*4];
        allocation3 = null;
        allocation3 = new byte[_1MB*4];
    }

    public static void getAllStackTrace(){
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        allStackTraces.entrySet().forEach(e->{
            Thread key = e.getKey();
            StackTraceElement[] value = e.getValue();
            System.out.println("=======================key:"+key.getName());
            for(StackTraceElement element:value){
                System.out.println(element);
            }
        });
    }
}

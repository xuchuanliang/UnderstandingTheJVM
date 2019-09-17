package capter03;

/**
 * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class Allocation {
    private static final int _1MB = 1024*1024;

    public static void main(String[] args){
        testAllocation();
    }

    /**
     [GC (Allocation Failure) [PSYoungGen: 6783K->879K(9216K)] 6783K->4983K(19456K), 0.0046817 secs] [Times: user=0.06 sys=0.00, real=0.00 secs]
     Heap
     PSYoungGen      total 9216K, used 7427K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     eden space 8192K, 79% used [0x00000000ff600000,0x00000000ffc65010,0x00000000ffe00000)
     from space 1024K, 85% used [0x00000000ffe00000,0x00000000ffedbca0,0x00000000fff00000)
     to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
     ParOldGen       total 10240K, used 4104K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     object space 10240K, 40% used [0x00000000fec00000,0x00000000ff002020,0x00000000ff600000)
     Metaspace       used 3327K, capacity 4556K, committed 4864K, reserved 1056768K
     class space    used 361K, capacity 392K, committed 512K, reserved 1048576K

     GC日志解释：
     GC日志以GC或Full GC说明这次垃圾收集的停顿类型，如果有Full，则说明此次GC发生了Stop the World
     PSYoungGen标识垃圾收集器和发生垃圾收集的区域，PS表示Parallel Scavenge收集器，YoungGen表示新生代
     方括号内部6783K->879K(9216K)标识GC前该内存区域已使用容量-->GC后该区域已使用容量
     方括号外部后面的6783K->4983K(19456K)表示GC前java堆已使用容量->GC后java堆已使用容量（java堆总容量）

     内存分析：
     本方法使用VM参数后的jvm日志eden space 8192K,from space 1024K,to   space 1024K,可看出eden区域是8M，两个Survivor区域分别是1M，
     新生代总可用空间为9216KB（Eden区+1个Survivor区的总容量）

     垃圾收集器运行解释：
     执行方法时，当执行allocation4对象的语句时会发生一次Minor GC，这次GC的结果是新生代内存由6783K缩小至879K【日志中6783K->879K(9216K)】，
     而总内存占用没有怎么减少【日志中6783K->4983K(19456K)】，因为allocation1，allocation2，allocation3三个对象都是存活的，虚拟机机务没有找到
     可回收对象。这次GC发生的原因是给allocation4分配内存的时候，发现Eden已经被占用6MB，剩余空间已经不足
     以分配allocation4所需的4M内存（新生代总可用空间为9216KB（Eden区+1个Survivor区的总容量）），因此发生Minor GC.GC期间虚拟机又发现
     已有的3个2MB大小的对象全部无法放入Survivor空间（Survivor空间之后1MB大小），所以只好通过分配担保机制提前转移到老年代去。
     这次GC结束后，4MB的allocation4对象顺利分配在Eden中，因此程序执行完的结果是Eden占用4MB（被allocation4占用），Survivor空间，老年代
     被占用6MB（被allocation1，allocation2，allocation3占用）

     */
    public static void testAllocation(){
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2*_1MB];
        allocation2 = new byte[2*_1MB];
        allocation3 = new byte[2*_1MB];
        allocation4 = new byte[4*_1MB];
    }
}

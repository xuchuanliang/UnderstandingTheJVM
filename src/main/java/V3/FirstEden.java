package V3;

public class FirstEden {
    /**
     *
     *
     * @param args
     */
    private static int _1MB = 1024;

    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * 最大和最小堆内存是20M，新生代是10M，新生代中eden:from Survivor:to Survivor=8:1:1，即Eden区域是8M，from 1M， to 1M
     */
    private static void test1(){
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB]; // 出现一次Minor GC
    }

    /**
     * -verbose:gc -Xmx20m -Xms20m -Xmn10m -XX:PrintGCDetails -XX:SurvivorRatio=8 -XX:+PrintTenuringThreshold=1 -XX:+PrintTenuringDistribution
     */
    private static void test2(){
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4]; // 什么时候进入老年代决定于XX:MaxTenuringThreshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }
}

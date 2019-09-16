package capter02;

/**
 * 引用计数算法进行垃圾收集，无法解决循环引用问题
 */
public class TestGC {
    public Object instance;

    /**
     * 该字段只为对象在堆内存中能占据一定的内存空间，方便查看对象是否被回收
     */
    private byte[] bytes = new byte[2*1024*1024];

    public static void main(String[] args){
        TestGC testGCA = new TestGC();
        TestGC testGCB = new TestGC();
        testGCA.instance = testGCB;
        testGCB.instance = testGCA;
        //引用置空，让中断java虚拟机栈中的对象引用类型与堆中的对象中断
        testGCA = null;
        testGCB = null;
        //执行垃圾回收
        System.gc();
    }

}

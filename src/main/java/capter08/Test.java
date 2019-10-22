package capter08;

/**
 * placeholder能否被回收取决于局部变量表的Slot是否还存在关于placeholder数组对象的引用
 */
public class Test {

    public static void main(String[] args){
//        test1();
//        test2();
        test3();
    }

    /**
     *[GC (System.gc())  70740K->66343K(249344K), 0.0007738 secs]
     * [Full GC (System.gc())  66343K->66249K(249344K), 0.0042916 secs]
     */
    public static void test1(){
        byte[] bytes = new byte[64*1024*1024];
        System.gc();
    }

    /**
     *[GC (System.gc())  70740K->66343K(249344K), 0.0006417 secs]
     * [Full GC (System.gc())  66343K->66249K(249344K), 0.0041946 secs]
     */
    public static void test2(){
        {
            byte[] bytes = new byte[64*1024*1024];
        }
        System.gc();
    }

    /**
     *[GC (System.gc())  70740K->66327K(249344K), 0.0006417 secs]
     * [Full GC (System.gc())  66327K->713K(249344K), 0.0038888 secs]
     *
     *
     */
    public static void test3(){
        {
            byte[] bytes = new byte[64*1024*1024];
        }
        int i = 1;
        System.gc();
    }
}

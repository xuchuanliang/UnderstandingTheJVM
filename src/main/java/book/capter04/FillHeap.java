package book.capter04;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用JConsole中的内存界面查看Eden Space,Survivor Space,Tenured Gen，相当于jdk提供的jstat命令
 */
public class FillHeap {

    /**
     * 内存对象占位符，一个OOMbject大约64kb
     */
    static class OOMbject{
        public byte[] placeholder = new byte[64*1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMbject> list = new ArrayList<>();
        for(int i=0;i<num;i++){
            //延时，令jConsole的曲线更加明显
            Thread.sleep(50);
            list.add(new OOMbject());
        }

    }

    /**
     * VM args:-Xms100M -Xmx100M -XX:+UserSerialGC
     * 堆内存最小和最大内存空间是100M，使用SerialGC（单线程最基本的垃圾收集器）
     * 每50毫秒想堆内存填充一个64kb的对象，填充100次
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        fillHeap(1000);
        System.gc();
        System.out.println("end");

    }
}

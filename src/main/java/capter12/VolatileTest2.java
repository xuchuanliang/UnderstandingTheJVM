package capter12;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuchuanliangbt
 * @title: VolatileTest2
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/10/307:46
 * @Version
 */
public class VolatileTest2 {
    public static AtomicInteger integer = new AtomicInteger(0);
    public static void increase(){
        integer.incrementAndGet();
    }
    public static void main(String[] args){
        Thread[] threads = new Thread[20];
        for(int i=0;i<20;i++){
            threads[i] = new Thread(()->{
                for(int j = 0;j<2000;j++){
                    increase();
                }
            });
            threads[i].start();
        }
        while (Thread.activeCount()>1){
            Thread.yield();
        }
        System.out.println(integer.get());
    }
}

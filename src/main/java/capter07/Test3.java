package capter07;

/**
 * @author xuchuanliangbt
 * @title: Test3
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/10/177:53
 * @Version
 */
public class Test3 {
    public static void main(String[] args){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread()+" start");
                DeadLoopClass deadLoopClass = new DeadLoopClass();
                System.out.println(Thread.currentThread()+" end");
            }
        };
        new Thread(runnable,"thread1").start();
        new Thread(runnable,"thread2").start();
    }
}
class DeadLoopClass{
    static {
        if(true){
            System.out.println(Thread.currentThread()+" int DeadLoopClass");
            while (true){
            }
        }
    }
}

package book.capter12;

/**
 * volatile在并发情况下依然是不安全的
 */
public class VolatileTest {

    public static volatile int race = 0;

    public static void increase(){
        race++;
    }

    private static final int THREAD_COUNT = 20;

    /**
     * 此处每次输出的race结果都不一致，都是小于200000的数字，证明了volatile修饰的变量在并发情况下也是不安全的
     * 原因：increase()方法在Class文件中是由4条字节码指令构成，从字节码分析（javap -v VolatileTest.class）：该方法由getstatic,iconst_1,iadd,putstatic四个字节码指令构成，
     * 当getstatic指令把race的值取到栈顶时，volatile关键字保证了race的值在此时是正确的，但是在执行iconst_1,iadd这些指令的时候，其他线程
     * 可能已经把race的值加大了，而在操作数栈顶的值就变成了过期的数据，所以putstatic指令执行后就可能把较小的race值同步回主内存之中
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];
        for(int i=0;i<THREAD_COUNT;i++){
            threads[i] = new Thread(()->{
               for(int j=0;j<10000;j++){
                   increase();
               }
            });
            threads[i].start();
        }
        while (Thread.activeCount()>1){
            Thread.yield();
        }
        System.out.println(race);

    }
}

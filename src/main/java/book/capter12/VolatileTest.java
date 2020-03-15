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
        while (true){
            System.out.println(Thread.activeCount());
            Thread.sleep(100);
        }
//        while (Thread.activeCount()>1){
//
//        }
    }
}

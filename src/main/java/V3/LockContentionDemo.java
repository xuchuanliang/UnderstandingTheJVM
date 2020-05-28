package V3;

import java.io.Writer;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class LockContentionDemo {
    //模拟持有锁的时间跨度
    static long longDuration = 100;
    //模拟申请锁的频率
    static long lockAccessFrequency = 50;
    static ShareResource shareResource = new ShareResource();
    static int processNum = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[processNum];
        for(int i=0;i<processNum;i++){
            threads[i] = new Thread(()->{
                for(;;){
                    shareResource.access();
                    try {
                        Thread.sleep(lockAccessFrequency);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Stream.of(threads).forEach(t->t.start());
        TimeUnit.HOURS.sleep(1);

    }

    static class ShareResource{
        public synchronized void access(){
            try {
                //模拟实际操作耗时
                Thread.sleep(longDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

package V3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class VolatileTest {
    private static int num = 0;

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<100;i++){
            CompletableFuture.runAsync(()->{
               for(int j=0;j<20000;j++){
                   num++;
               }
            });
        }
        TimeUnit.SECONDS.sleep(30);
        System.out.println(num);
    }
}

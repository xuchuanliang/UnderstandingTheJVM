package capter04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * JConsole的线程页面，相当于jstack，查看线程状况
 */
public class LockThread {

    public static void createBusyThread(){
        Thread thread = new Thread(() -> {
            while (true) {}
        });
        thread.start();
    }

    public static void createLockThread(final Object lock){
        Thread thread = new Thread(()->{
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        bufferedReader.readLine();
        createBusyThread();
        bufferedReader.readLine();
        Object obj = new Object();
        createLockThread(obj);
    }

}

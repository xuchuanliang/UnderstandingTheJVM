package com.ant;

import sun.misc.Contended;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    @Contended
    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    /**
     * 死锁
     *
     * @throws InterruptedException
     */
    @org.junit.Test
    public void deadLock() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    synchronized (lock2) {
                        System.out.println("线程1获取到两把锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    synchronized (lock1) {
                        System.out.println("线程2获取到两把锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("main thread end");
    }

    /**
     * 解决死锁方式1：按照顺序获取锁，由小到大
     *
     * @throws InterruptedException
     */
    @org.junit.Test
    public void resolveDeadLock1() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1.hashCode() < lock2.hashCode() ? lock1 : lock2) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    synchronized (lock1.hashCode() > lock2.hashCode() ? lock1 : lock2) {
                        System.out.println("线程1获取到两把锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock1.hashCode() < lock2.hashCode() ? lock1 : lock2) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    synchronized (lock1.hashCode() > lock2.hashCode() ? lock1 : lock2) {
                        System.out.println("线程2获取到两把锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("main thread end");
    }

    /**
     * 解决死锁方式2：使用ReentrantLock非阻塞式获取锁
     *
     * @throws InterruptedException
     */
    private static ReentrantLock lock11 = new ReentrantLock();
    private static ReentrantLock lock12 = new ReentrantLock();

    @org.junit.Test
    public void resolveDeadLock2() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                if(lock11.tryLock(2, TimeUnit.SECONDS)){
                    TimeUnit.SECONDS.sleep(2);
                    if(lock12.tryLock(2,TimeUnit.SECONDS)){
                        System.out.println("线程1获取到两把锁");
                        lock12.unlock();
                    }
                    lock11.unlock();
                }
            } catch (InterruptedException e) {

            }finally {

            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                if(lock12.tryLock(1, TimeUnit.SECONDS)){
                    TimeUnit.SECONDS.sleep(2);
                    if(lock11.tryLock(1,TimeUnit.SECONDS)){
                        System.out.println("线程2获取到两把锁");
                        lock11.unlock();
                    }
                    lock12.unlock();
                }
            } catch (InterruptedException e) {
            }finally {
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("main thread end");
    }

    /**
     * 线程池
     */
    @org.junit.Test
    public void testThreadPool(){
        /**
         * corePoolSize
         * maximumPoolSize
         * keepAliveTime
         * 当有任务提交时，首先会创建线程，当线程数量等于corePoolSize数量时，会将新提交的任务存入Queue中，
         * 当Queue满了时候；若继续提交任务，会继续创建线程新的线程直接执行新提交的任务，当创建的线程数量等于maximumPoolSize时，再继续提交任务会执行拒绝策略
         * jdk内定的拒绝策略分为四种：
         * java.util.concurrent.ThreadPoolExecutor.AbortPolicy：抛出异常
         * java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy：交给调用者执行新任务
         * java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy：丢弃老的任务
         * java.util.concurrent.ThreadPoolExecutor.DiscardPolicy：丢弃新的任务
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,500,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5),new ThreadPoolExecutor.DiscardOldestPolicy());
        for(int i=0;i<20;i++){
            threadPoolExecutor.execute(new TestThreadPool(i+1));
            System.out.println("线程数量："+threadPoolExecutor.getPoolSize()+";任务队列数量："+threadPoolExecutor.getQueue().size()+"");
        }
    }

    private class TestThreadPool implements Runnable{
        private int num;
        public TestThreadPool(int num){
            this.num = num;
        }
        @Override
        public void run() {
            try {
                System.out.println(num+"---start");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @org.junit.Test
    public void testFinally(){
        System.out.println(get1());
    }

    private int get1(){
        int res = 0;
        int i = 10;
        try{
            res++;
            System.out.println(1/0);
            return res;
        }catch (Exception e){
            return i;
        }finally {
            res += 10;
            return res;
        }
    }

    @org.junit.Test
    public void testLongAdder() throws InterruptedException {
        final LongAdder longAdder = new LongAdder();
        long time = System.nanoTime();
        for(int i=0;i<2;i++){
            CompletableFuture.runAsync(()->{
                for(int j=0;j<Integer.MAX_VALUE;j++){
                    longAdder.increment();
                }
            });
        }
        System.out.println(longAdder.longValue()+":"+(System.nanoTime()-time));
        AtomicLong atomicLong = new AtomicLong(0);
        time = System.nanoTime();
        for(int i=0;i<2;i++){
            CompletableFuture.runAsync(()->{
                for(int j=0;j<Integer.MAX_VALUE;j++){
                    atomicLong.incrementAndGet();
                }
            });
        }
        TimeUnit.MINUTES.sleep(2);
        System.out.println(atomicLong.longValue()+":"+(System.nanoTime()-time));
    }

}

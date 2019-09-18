package capter04;

/**
 * 检测死锁
 */
public class TestSynAddRunalbe implements Runnable{

    int a,b;
    public TestSynAddRunalbe(int a,int b){
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        synchronized (Integer.valueOf(a)){
            synchronized (Integer.valueOf(b)){
                System.out.println(a+b);
            }
        }
    }

    public static void main(String[] args){
        for(int i=0;i<100;i++){
            new Thread(new TestSynAddRunalbe(1,2)).start();
            new Thread(new TestSynAddRunalbe(2,1)).start();
        }
    }
}

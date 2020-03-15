package book.capter04;

/**
 * @author xuchuanliangbt
 * @title: BTraceTest
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/9/1820:22
 * @Version
 */
public class BTraceTest {
    public int add(int a,int b){
        return a+b;
    }

    public static void main(String[] args) throws InterruptedException {
        BTraceTest bTraceTest = new BTraceTest();
        Thread.sleep(10000);
        System.out.println("start");
        for(int i=0;i<10;i++){
            int a = (int) Math.round(Math.random()*1000);
            int b = (int) Math.round(Math.random()*1000);
            System.out.println(bTraceTest.add(a,b));
        }
    }
}

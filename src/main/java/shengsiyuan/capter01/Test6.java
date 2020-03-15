package shengsiyuan.capter01;

/**
 * 输出的值是1，0
 * 原因：当调用Singleton.getInstance()是主动使用类，对Singleton类进行加载，连接，初始化
 * 在连接的准备阶段，会对静态变量进行赋初始值：count1=0，singleton=null，count2=0，
 * 然后进行初始化进行赋值阶段，当执行到new Singleton()时，会执行构造方法，执行完构造方法后count1=1，count2=2，
 * 然后执行到count2=0进行赋值，导致1被覆盖成0
 */
public class Test6 {
    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
        System.out.println(Singleton.count1);
        System.out.println(Singleton.count2);
    }
}

class Singleton{

    public static int count1;

    private static Singleton singleton = new Singleton();

    public static int count2 = 0;
    private Singleton(){
        count1++;
        count2++;
    }
    public static Singleton getInstance(){
        return singleton;
    }
}

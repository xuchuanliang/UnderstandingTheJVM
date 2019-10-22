package capter08;

public class StaticDispatch {
    static abstract class Human{

    }
    static class Man extends Human{}

    static class Woman extends Human{}

    public void sayHello(Human guy){
        System.out.println("hello guy");
    }

    public void sayHello(Man guy){
        System.out.println("hello man");
    }

    public void sayHello(Woman guy){
        System.out.println("hello woman");
    }

    /**
     * 打印结果是：
     * hello guy
     * hello guy
     * @param args
     */
    public static void main(String[] args){
        StaticDispatch staticDispatch = new StaticDispatch();
        Human man = new Man();
        Human woman = new Woman();
        staticDispatch.sayHello(man);
        staticDispatch.sayHello(woman);
    }
}

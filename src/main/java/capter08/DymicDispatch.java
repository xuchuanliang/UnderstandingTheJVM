package capter08;

/**
 * 动态分派
 */
public class DymicDispatch {
    static abstract class Human{
        protected abstract void say();
    }
    static class Man extends Human{

        @Override
        protected void say() {
            System.out.println("man");
        }
    }

    static class Woman extends Human{

        @Override
        protected void say() {
            System.out.println("woman");
        }
    }

    /**
     * 输出结果
     * man
     * woman
     * woman
     * @param args
     */
    public static void main(String[] args){
        Human man = new Man();
        Human woman = new Woman();
        man.say();
        woman.say();
        man = new Woman();
        man.say();
    }
}

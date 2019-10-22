package capter08;

public class Dispatch {
    static class QQ{}
    static class _360{}
    public static class Father{
        public void hardChoice(QQ arg){
            System.out.println("father qq");
        }
        public void hardChoice(_360 arg){
            System.out.println("father 360");
        }
    }
    public static class Son extends Father{
        @Override
        public void hardChoice(QQ arg) {
            System.out.println("son qq");
        }

        @Override
        public void hardChoice(_360 arg) {
            System.out.println("son 360");
        }
    }

    /**
     * 输出结果
     * father 360
     * son qq
     * @param args
     */
    public static void main(String[] args){
        Father father = new Father();
        Father son = new Son();
        father.hardChoice(new _360());
        son.hardChoice(new QQ());
    }
}

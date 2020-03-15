package book.capter02;

/**
 *1.对象可以在被GC的时候自救
 * 2.这种自救的机会之后一次，因为一个对象的finalize方法只会被系统调用一次
 *
 */
public class FinalizeEscapeGC {
    public static FinalizeEscapeGC SAVE_HOOK = null;

    private void isAlive(){
        System.out.println("yes i am alive :)");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize is running");
        SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();
        //对象第一次拯救自己
        SAVE_HOOK = null;
        System.gc();
        //因为finalize的执行优先级很低
        Thread.sleep(500);
        if(SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("i am dead :(");
        }
        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if(SAVE_HOOK != null){
            SAVE_HOOK.isAlive();
        }else {
            System.out.println("i am dead :(");
        }
    }
}

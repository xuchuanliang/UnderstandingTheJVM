package V3;

/**
 * 演示栈深度过大导致的StackOverflow
 */
public class StackOverflowError {
    /**
     * -Xss128k -XX:+PrintGCDetails
     * @param args
     */
    public static void main(String[] args) {
        try{
            StackOverflowError.stackLeak();
        }catch (Throwable throwable){
            System.out.println("+++++++++++++++++++++++"+deap);
        }
    }

    private static int deap = 1;

    public static void stackLeak(){
        deap++;
        stackLeak();
    }
}

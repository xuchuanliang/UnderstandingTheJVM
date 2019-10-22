package capter07;

/**
 * @author xuchuanliangbt
 * @title: Test2
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/10/177:47
 * @Version
 */
public class Test2 {
//    static {
//        i = 0;
//        System.out.println(i);
//    }
//    private static int i = 1;
//    public static void main(String[] args){
//        System.out.println(i);
//    }
    public static void main(String[]args){
        System.out.println(Sub.B);
    }
}
class Parent{
    public static int A = 1;
    static {
        A = 2;
    }
}

class Sub extends Parent{
    public static int B = A;
}

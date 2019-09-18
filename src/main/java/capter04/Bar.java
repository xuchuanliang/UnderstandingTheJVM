package capter04;

/**
 * @author xuchuanliangbt
 * @title: Bar
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/9/1818:34
 * @Version
 */
public class Bar {
    int a = 1;
    static int b = 2;
    public int sum(int c){
        return a + b + c;
    }

    /**
     * VM args:-XX:+PrintAssembly -Xcomp -XX:CompileCommand=dontinline,*Bar.sum -XX:CompileCommand=compileonly,*Bar.sum test.Bar
     *
     * @param args
     */
    public static void main(String[] args){
        new Bar().sum(3);
    }
}

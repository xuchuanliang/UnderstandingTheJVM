package book.capter10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * java的泛型是伪泛型，在编译阶段会把泛型擦除
 */
public class Test {
    public static void main(String[] args){
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c==d);
        System.out.println(e==f);
        System.out.println(c==(a+b));
        System.out.println(c.equals(a+b));
        System.out.println(g==(a+b));
        System.out.println(g.equals(a+b));
    }
}
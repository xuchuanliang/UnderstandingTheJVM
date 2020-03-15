package shengsiyuan.capter01;

import java.util.UUID;

/**
 */
public class Test4 {
    public static void main(String[] args) {
        Parent4[] parent4s = new Parent4[1];
        System.out.println(parent4s.getClass());
        System.out.println(parent4s.getClass().getSuperclass());
        Parent4[][] parent4s1 = new Parent4[1][1];
        System.out.println(parent4s1.getClass());
        System.out.println(parent4s1.getClass().getSuperclass());
    }
}

class Parent4{
    final static String parentName = UUID.randomUUID().toString();
    static {
        System.out.println("Parent1 init");
    }
}

package capter08;

import java.io.Serializable;

/**
 * 静态分派
 */
public class Overload {
//    public static void say(Object a){
//        System.out.println("hello Object");
//    }

//    public static void say(int a){
//        System.out.println("hello int");
//    }

//    public static void say(long a){
//        System.out.println("hello long");
//    }

//    public static void say(Character a){
//        System.out.println("hello character");
//    }

//    public static void say(char a){
//        System.out.println("hello char");
//    }

    public static void say(char...a){
        System.out.println("hello char...");
    }

//    public static void say(Serializable a){
//        System.out.println("hello serializable");
//    }

    public static void main(String[] args){
        say('a');
    }
}

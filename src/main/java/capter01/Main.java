package capter01;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuchuanliangbt
 * @title: Main
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/9/517:20
 * @Version
 */
public class Main {

    public static void main(String[] args) {
//        test1();
//        testOutMemory();
        try{
            testStackOut();
        }catch (Throwable e){
            System.out.println("最大深度"+i);
            e.printStackTrace();
        }
    }

    public static void test1() {
        /**
         * ==比较引用类型比较的是地址值是否相同
         * equals比较引用类型，默认也是比较地址值是否相同，而String类重写了equals方法，比较的是内容是否相同
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * 创建字符串内存详解：
         * public static void main(String[] args){
         *     String a = new String("abc");
         * }
         * 1.首先main方法进栈;
         * 2.在栈中定义一个对象a，去堆中开辟一个内存空间，将内存空间的引用赋值给a，"abc"是常量，然后去字符串常量池查看是否有abc字符串对象，
         * 如果没有则在字符串常量池中分配一个空间存放abc，并将其地址空间存入堆中new出来的空间中
         * public static void main(String[] args){
         *     String a = "abc";
         * }
         * 1.首先main方法进栈
         * 2.在栈中定义一个对象a，去字符串常量池中查看是否存在值为abc的字符串对象，如果没有则在字符串常量池中分配一个空间存放abc，将abc的地址
         * 赋值为a
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         */
        /**
         * String对象中的intern()方法详解
         * 作用是查看常量池中是否存在和调用方法的字符串内容一样的字符串，如果有，则返回常量池中的字符串的地址；如果没有，就在常量池中写入
         * 一个堆中该字符串对象的引用，指向堆中的该对象，并返回该引用的地址
         *
         */
        /**
         * 1.虚拟机栈中开辟空间存放a
         * 2.堆中开辟空间存放一个String对象，并将该对象的地址存放在虚拟机栈的a中
         * 3.去字符串常量池查看是否已经存在abc字符串对象，如果不存在则在字符串常量池创建并将对象的地址存放在第二步堆内存创建的对象中
         * 下面的则虚拟机栈中a存放的指针和b存放的指针不同，都是堆内存中新创建的对象；两个堆内存中存放的内容是相同的，都是字符串abc在字符串常量池的地址
         */
        String a = new String("abc");
        String b = new String("abc");
        //true，都是字符串常量池中abc字符串的地址
        System.out.println(a.intern() == b.intern());
        //false 字符串常量池中abc字符串地址！=堆内存创建b时开辟的空间地址
        System.out.println(a.intern() == b);
        //false
        System.out.println(a == b.intern());
        /*
         * intern方法会到常量池中查找是否存在该对象，如果存在，返回该对象。不存在的话就创建该对象并返回该对象(jdk1.6),(jdk1.7)
         * 会在常量池中存一个指向堆中的那个对象的引用。 不存在往往是String s3 = new String("1") + new
         * String("1");这种形式，会在堆中有一个s3指向的11的对象和常量池中的1对象
         * 在这里就是体现的堆中的内存地址不一样，但对应的同一个常量池中的string 第一个比较时常量池中的该对象和自身比较
         * 下面两个比较则是常量池中的对象和堆中的两个对象进行比较
         */
        String poolstr = "abc";
        // 直接从字符串常量池中获取
        System.out.println(a.intern() == poolstr);// true
        System.out.println(b.intern() == poolstr);// true
        /*
         * 这里新声明并赋值了一个poolstr，值为常量池中的字符串"abc",将它和a.intern()和b.inten()比较就是和自身比较
         */
        String str = new String("a") + new String("b");
        System.out.println(str.intern() == str);// true
        /*
         * str创建了3个对象，在堆中有一个"ab"，在常量池中有一个"a"和"b" 比较str.intern()和str会得到true
         * 在jdk1.7之后，会在常量池中存一个指向堆中的那个对象的引用。
         * 调用str.intern()会在常量池中存储一个指向堆中"ab"的引用，也就是说它和堆中的对象实际是等价的，因此==时返回true
         */
        String strtwo = "ab";
        System.out.println(strtwo == str);// true
        /*
         * 常量池中已存在ab，所以会直接将strtwo指向常量池中的"ab",即堆中str对象的引用，因此相等
         */
    }

    static class OOMObject{
        byte[] bytes = new byte[1024];
    }

    public static void testOutMemory(){
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true){
            list.add(new OOMObject());
        }
    }


    Boolean isItTrue(){
        return Boolean.TRUE;
    }

    static int i = 1;
    private static void testStackOut(){
        i++;
        testStackOut();
    }


}

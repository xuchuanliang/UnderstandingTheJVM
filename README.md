#《深入理解java虚拟机》
- java技术体系包括:java程序设计语言、各种硬件平台上的java虚拟机、class文件格式、java api类库、来自商业机构和开源社区的第三方java类库
- java程序设计语言+java虚拟机+java api类库称为jdk，是用于支持java程序最小的开发环境
- java api类库中的javaSE api子集和java虚拟机这两部分统称为JRE，JRE是支持javaSE程序运行的标准环境。
2019年8月15日 12:13:50 9/460
- 动态编译指的是在运行时编译，与之相对应的是事前编译，也叫静态编译
- JIT编译狭义来说是当某段代码即将被第一次执行时进行编译，因而叫及时编译，JIT编译是动态编译的一种特例，
- 自适应编译也是一种编译，但它通常执行时机比JIT编译迟，先让程序以某种方式运行起来，收集一些信息后再做动态编译
- 解释器是一条一条的解释执行源语言，比如php，javascript就是典型的解释性语言
- 编译器是把源代码整个翻译成目标代码，执行时不在需要编译器，直接在支持目标代码的平台上运行，这样执行效率比解释执行快很多。比如C语言代码被
编译成二进制代码（exe程序），在window平台上执行。

# 第二部分 自动内存管理机制
## Java内存区域与内存溢出异常
- java虚拟机运行期间管理的内存：程序计数器、java虚拟机栈、本地方法栈、方法区、堆
> 程序计数器：是一块很小的区域，可以看做是当前线程所执行的字节码的行号指示器，这块内存区域是线程私有
> java虚拟机栈：描述的是java方法执行的内存模型：每个方法在执行的时候都会创建一个栈帧用于存储局部变量、操作数栈、动态链接、方法出入口等信息，
每一个方法在调用直至执行完成的过程，对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。我们常说的java内存区分为栈内存和堆内存，这里说的栈内存
指的就是java虚拟机栈中的局部变量表部分，局部变量表存放了编译器可知的各种基本数据类型、对象引用类型（指向对象起始地址的引用指针）和
returnAddress类型（指向了一条字节码指令的地址），java虚拟机栈也是线程私有的
> 本地方法栈：与java虚拟机栈发挥的作用相似，只不过java虚拟机栈为虚拟机执行java方法，本地方法栈为虚拟机执行native方法（native方法是指在
java平台和本地C代码进行相互操作的API）
> java堆：java堆是java虚拟机管理内存中最大的一块，是线程共享的一块内存区域，在虚拟机启动时创建，用于存放对象实例，有时候叫GC堆，按照垃圾收集器
分代收集算法分为新生代和老年代，再细致分为Eden空间，From Survivor空间，To Survivor空间等；从内存分配角度线程共享的java堆中可能划分出
多个线程私有的分配缓冲区。
> 方法区：是各个线程共享的内存区域，用来存放被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。被称为永久代。
```java
package capter01;

/**
 * @author xuchuanliangbt
 * @title: Main
 * @projectName UnderstandingTheJVM
 * @description:
 * @date 2019/9/517:20
 * @Version
 */
public class Main {

    public static void main(String[] args){
        test1();
    }

    public static void test1(){
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
        System.out.println(a.intern()==b.intern());
        //false 字符串常量池中abc字符串地址！=堆内存创建b时开辟的空间地址
        System.out.println(a.intern()==b);
        //false
        System.out.println(a==b.intern());
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
}

```
- 直接内存：不是虚拟机运行时数据区的一部分，也不是java虚拟机规范中定义的内存区域，java nio引入基于通道与缓冲区的I/O方式，它可以使用
native函数库直接分配堆外内存，然后通过一个存储在java堆中的DirectByteBuffer对象作为这块内存的引用进行操作，避免了java堆和native堆来回复制数据。

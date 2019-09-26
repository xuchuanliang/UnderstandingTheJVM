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
> 程序计数器(线程私有)：是一块很小的区域，可以看做是当前线程所执行的字节码的行号指示器，这块内存区域是线程私有

> java虚拟机栈(线程私有)：描述的是java方法执行的内存模型：每个方法在执行的时候都会创建一个栈帧用于存储局部变量、操作数栈、动态链接、方法出入口等信息，
每一个方法在调用直至执行完成的过程，对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。我们常说的java内存区分为栈内存和堆内存，这里说的栈内存
指的就是java虚拟机栈中的局部变量表部分，局部变量表存放了编译器可知的各种基本数据类型、对象引用类型（指向对象起始地址的引用指针）和
returnAddress类型（指向了一条字节码指令的地址），java虚拟机栈也是线程私有的

> 本地方法栈(线程私有)：与java虚拟机栈发挥的作用相似，只不过java虚拟机栈为虚拟机执行java方法，本地方法栈为虚拟机执行native方法（native方法是指在
java平台和本地C代码进行相互操作的API）

> java堆：java堆是java虚拟机管理内存中最大的一块，是线程共享的一块内存区域，在虚拟机启动时创建，用于存放对象实例，有时候叫GC堆，按照垃圾收集器
分代收集算法分为新生代和老年代，再细致分为Eden空间，From Survivor空间，To Survivor空间等；从内存分配角度线程共享的java堆中可能划分出
多个线程私有的分配缓冲区。

> 方法区：是各个线程共享的内存区域，用来存放被虚拟机加载的Class信息如类名、访问修饰符、常量、静态变量、即时编译器编译后的代码等数据。被称为永久代。
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
- 在HotSpot虚拟机中，对象在内存中存储的布局可以分为3块区域：**对象头、实例数据、对齐填充**
- HotSpot虚拟机的对象头包括两部分信息，第一部分用于存储对象自身的运行时数据，如哈希吗、GC分代年龄、锁状态标志、线程持有的锁、偏向线程ID、
偏向时间戳等；另一个部分是类型指针，即对象指向他的类元数据的指针，虚拟机通过这个指针来确定对象是哪个类的实例。
- 虚拟机新建对象时涉及到两个方面问题：对象内存的分配以及创建对象时并发的处理，采取方式如下：对象内存分配两种方式：指针碰撞、空闲列表；
解决并发问题的两种方式：对分配内存空间的动作进行同步处理、把内存分配的工作按照线程划分在不同的空间里进行，即每个线程在java堆中预先分配
一小块内存，称为本地线程缓冲区（Thread Local Allocation Buffer TLAB）。
>对象的访问定位：java程序需要通过栈上reference数据来操作堆上的具体对象。由于reference类型在java虚拟机规范中只规定一个指向对象的引用，
>并没有定义这个引用应该用何种方式去定位、访问堆上对象的具体位置，所以对象访问方式主要有两种：句柄和直接指针。
>句柄访问：java堆中划分出一块内存作为句柄池，reference中存储的就是对象的句柄地址，而句柄中包含了对象的实例数据和类型数据各自的具体地址信息；
>直接访问：如果是直接访问方式，那么java堆中必须考虑如何放置访问类型数据的相关信息，而reference中存储的直接就是对象地址。
>使用句柄访问的好处就是reference中存储的是句柄地址，对象被移动或者改变，不需要改变reference中的信息，只需要修改句柄中的实例数据指针；
>使用直接访问的好处就是速度快，节省了一次指针定位的时间开销。
- -XX:+HeapDumpOnOutMemoryError：可以让虚拟机在出现内存溢出异常时Dump出当前的内存堆转储快照。
- -Xms：堆最小值；-Xmx：堆最大值
- -Xoxx和-Xss设置本地方法栈大小
>如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常
>如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError异常

# 垃圾收集器与内存分配策略
- 对象存活判定算法：引用计数算法和可达性分析算法
- 引用计数算法：给对象中添加一个引用计数器，每当有一个地方引用它时，计数器加一，当引用失效时，计数器减一；任何时刻计数器为0的对象就是不再
被使用的。但是引用计数器无法解决循环引用问题
- -XX:+PrintGC 输出GC日志；-XX:+PrintGCDetails 输出GC的详细日志；-XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）；
-XX:+PrintDataStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）；- XX:+PrintHeapAtGC：在进行GC的前后打印出堆的信息
-Xloggc:../logs/gc.log 日志文件的输出路径

- 可达性分析算法：通过一系列称为称为GC Roots的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径称为引用链，当一个对象到GC Roots
没有任何引用链相连（用图论的话来说，就是从GC Roots到这个对象不可达）时，则证明此对象是不可用的。
- java语言中，可被作为GC Roots的对象包括以下几种：1.虚拟机栈（栈帧中的本地变量表）中引用的对象；2.方法区中类静态属性引用的对象；3.方法区中
常量引用的对象；4.本地方法栈中JNI（即一般说的Native方法）引用的对象。
>在jdk1.2之前，java中引用的定义是如果reference类型的数据中存储的数值代表的是另一块内存的起始地址，就称这块内存代表着一个引用。在jdk1.2之后，
>java对引用进行扩充否，分为强引用、软引用、弱引用、虚引用。
>>强引用就是指在程序代码之中普遍存在的，类似于Object obj = new Object()，这类引用，只要强引用还在，垃圾收集器永远不会回收掉被引用的对象。
>>软引用是用来描述一些还有用但并非必须的对象。对于软引用的对象，在系统中将要发生内存溢出异常之前，将会把这些对象列进回收范围之中进行第二次回收。
如果这次回收还是没有足够的内存，才会抛出内存溢出异常。SoftReference类实现软引用。
>>弱引用也是用来描述必须对象的，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存到下一次垃圾收集发生之前。当垃圾收集器工作时，无论
当前内存是否足够，都会回收掉只被弱引用关联的对象。WeakReference类来实现弱引用。
>>虚引用也称为幽灵引用或者幻影引用，它是最弱的一种引用关系。一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来取得
一个对象实例。为一个对象设置虚引用关联的唯一目的就是能在这个对象被收集器回收时收到一个系统通知。PhantomReference类实现虚引用

- java虚拟机中使用可达性分析算法作为GC垃圾回收的算法
>在可达性算法分析中不可达的对象，也不会立即死亡，而是至少需要经过两次标记过程：如果对象在进行可达性分析过程中发现没有和GC ROOTs相连接
>的引用链，将会进行第一次标记并进行一次筛选，筛选的条件是有无必要执行finalize()方法，当对象没有覆盖finalize方法或者finalize方法已经被
>虚拟机调用过，则认为没有必要执行，finalize方法只会被调用一次。
>如果对象有必要执行finalize方法，则会把对象放在一个名为F-Queue的队列中，并在虚拟机自动建立的一个低优先级Finalizer线程中执行，如果对象在
>finalize方法中将自己赋值为某个类变量或者对象的成员变量，那么第二次标记时它会被移除出即将回收的集合，此时对象拯救了自己，如果对象这个时间
>还没有逃脱，那么基本上确定会被回收
```java
package capter02;

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

```

- 回收方法区（永久代）：永久代的垃圾收集主要包含废弃常量和无用类，废弃常量是指在永久代中的常量没有任何一个对象引用该常量，那么这个常量
会被清理出永久代；无用来的判定条件是：1.该类的所有的实例都已经被回收，也就是java堆中不存在该类的实例；2.加载该类的classloader已经被回收；
3.该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。

## 垃圾收集算法

- 标记-清除算法：算法分为标记和清除两个阶段，首先标记处出所有需要回收的对象（标记过程其实就是引用计数算法或可达性分析算法），在标记完成后
统一回收所有被标记的对象。
>标记-清除算法不足有两点：一个是效率问题，标记和清除这两个过程效率都不高；另一个是空间问题，标记清除后会产生大量不连续的内存碎片，空间碎片
>过多可能会导致以后在程序运行过程中需要分配较大的对象时，无法找到足够的连续内存而不得不提前触发一次垃圾收集动作。参见图标记-清除算法示意图

- 复制算法：为了解决效率问题，他将可用内存按照容量划分为大小相等的两块，每次只使用其中一块，当一块内存使用完后，将还存活着的对象复制到另一块
上面，然后将已经使用过的内存空间一次清理掉。这样使得每次都是对整个半区进行内存回收，内存分配时也就不用考虑内存碎片问题，只需要移动堆顶指针，
按照顺序分配内存即可，参见图复制算法示意图。
>现在的商业虚拟机都是采用这种收集算法来回收新生代，将内存分为较大的Eden空间和两块较小的Survivor空间，每次使用Eden和其中一块Survivor。
>当回收时，将Eden和Survivor中还存活的对象一次性复制到另一块Survivor区域，最后清理掉Eden和刚才用掉的Survivor空间。HotSpot虚拟机默认Eden空间
>和Survivor大小比例是8：1，当Survivor空间不够用时，需要依赖其他内存（指老年代）进行分配担保。如果另一块Survivor空间没有足够空间存放上一次
>新生代收集下来的存活对象，这些对象将直接通过分配担保机制进入老年代。

- 为什么需要两个Survivor区域，一个Eden区域：参见博客：https://blog.csdn.net/antony9118/article/details/51425581

- 标记-整理算法：老年代中可能出现对象都是100%存活的极端情况，根据老年代的特点，推出标记-整理算法，不是对可回收对象进行清理，而是让所有存活
对象都向一端移动，然后直接清理掉边界以外的内存，参见标记-整理算法示意图

- 分代收集算法：当前商业虚拟机垃圾收集都采用分代收集算法，根据对象的存活周期将内存划分为几块，一般把java堆分成新生代和老年代，这样就根据各个
年代特点采用最适当的收集算法。在新生代中，每次垃圾收集时都有大批的对象死去，少量存活，就采用复制算法，只需要复制出少量存活对象的复制成本就可以
完成收集。而老年代中对象存活率比较高，没有额外空间对它进行分配担保，就必须使用标记-清除算法或标记-整理算法

## HotSpot的算法实现
- 枚举根节点
> 某些应用方法区可能有数百兆数据，若逐一检查所有的引用，耗费时间比较吵；可达性分析对执行时间敏感性还体现在GC停顿上
> HotSpot实现中，使用一组称为OopMap的数据类型来达到这一个目的。
- 安全点
- 安全区域

## 垃圾收集器
- HotSpot中的7中不同分代的收集器：，参见HotSpot虚拟机的垃圾收集器
- Serial收集器
>Serial是最基本的垃圾收集器，是一个单线程的收集器，它在垃圾收集的时候，必须暂停其他所有的工作线程，知道它收集结束：Stop the World。
>参见Serial/Serial Old收集器运行示意图。它是虚拟机运行在Client模式下默认新生代收集器，优点是简单而高效，对于限定单个CPU的环境来说，
>Serial收集器由于没有线程交互的开销，可以获得最高的单线程收集效率。采用复制算法
- ParNew收集器
>ParNew收集器实际上是Serial收集器的多线程版本，除了使用多线程进行垃圾收集之外，其他行为包括Serial可用的所有控制参数、收集算法、Stop the World/
>对象分配规则、回收策略等都一样。参见ParNew-Serial Old收集器运行示意图。采用复制算法
>ParNew收集器是目前唯一一个能与CMS收集器配合工作
- 吞吐量=运行用户代码时间/（运行用户代码时间+垃圾收集时间）
- Parallel Scavenge收集器
>CMS等收集器的关注点是尽可能地缩短垃圾收集时用户线程的停顿时间，而Parallel Scavenge收集器的目标是达到一个可控制的吞吐量。
- Serial Old收集器：是Serial的老年代版本，参见Serial-Serial Old收集器运行示意图
- Parallel Old收集器：是Parallel 的老年代版本，参见Parallel Scavenge-Old收集器运行示意图
- CMS（Concurrent Mark Sweep）收集器，是一种以获取最短回收停顿时间为目标的收集器，是基于标记-清除算法实现，
整个过程分为4个步骤：初始标记、并发标记、重新标记、并发清除，参见Concurrent Mark Sweep收集器运行示意图
>初始标记、重新标记这两个步骤仍然需要Stop the World，初始标记只是标记一下GC ROOTS能够直接关联到的对象，速度很快，并发标记就是进行GC ROOTS
Tracing 的过程，而重新标记阶段则是为了修正并发标记期间因用户程序继续运作而导致标记产生变动的那一部分对象的标记记录，停顿时间一般比初始标记
阶段稍长一些，远比并发标记短。
>耗时最长的并发标记和和并发清除过程收集器线程可以和用户线程一起工作
>CMS的缺点：
>> 1.CMS收集器对CPU资源非常敏感，CMS默认启动的回收线程数是（CPU数量+3）/4,，也就是当CPU数量在4个以上时，并发回收时垃圾收集线程不少于20%的
>>cpu资源，并且随着CPU数量增加而下降。但是当CPU数量不足4个时，CMS对用户程序的影响可能会变大；
>>2.CMS无法处理浮动垃圾，可能出现Concurrent Mode Failure失败而导致另一次Full GC产生。由于CMS并发清理时用户线程还在正常运行，此过程中
>>生成的垃圾称为浮动垃圾，需要等到下一次GC时清理掉
>>3.CMS使用标记清除算法实现收集器，会产生空间碎片
- G1收集器
>G1收集器的特点：1.并发与并行；2.分代收集；3.空间整合；4.可预测的停顿
>G1将整个java堆划分为大小相等的独立区域（Region）
- G1进行垃圾回收的流程：1.初始标记；2.并发标记；3.最终标记；4.筛选回收，参见图G1收集器运行示意图
- 并行（Parallel）：指多条垃圾收集线程并行工作，但此时用户线程仍然处于等待状态
- 并发（Concurrent）：指用户线程与垃圾收集线程同时执行，用户程序在继续运行，而垃圾收集程序运行于另一个CPU上。

##内存分配与回收策略
- 对象在Eden区中分配，当Eden区没有足够空间进行分配时，虚拟机将发起一次Minor GC.
- -XX:+PrintGCDetails用于收集日志参数，告诉虚拟机在发生垃圾收集行为时打印内存回收日志，并且在程序退出时输出当前内存各区域分配情况。
- -Xms20M，-Xmx20M，-Xmn10M表示java堆最小20M，最大20M，10M分配给新生代，剩下的10M分配给老年代
- -XX:SurvivorRatio=0表示新生代中Eden区与一个Survivor区的空间比例是8：1
- 新生代GC（Minor GC）:指发生在新生代的垃圾收集动作，因为java对象大多具备朝生夕死的特性，所以Minor GC非常频繁，一般回收速度也比较快。
- 老年代GC（Major GC/Full GC）:指发生在老年代的GC，出现Major GC,经常会伴随至少一次Minor GC,Major GC的速度一般比Minor GC慢10倍以上。
```java
package capter03;

/**
 * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class Allocation {
    private static final int _1MB = 1024*1024;

    public static void main(String[] args){
        testAllocation();
    }

    /**
     [GC (Allocation Failure) [PSYoungGen: 6783K->879K(9216K)] 6783K->4983K(19456K), 0.0046817 secs] [Times: user=0.06 sys=0.00, real=0.00 secs]
     Heap
     PSYoungGen      total 9216K, used 7427K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
     eden space 8192K, 79% used [0x00000000ff600000,0x00000000ffc65010,0x00000000ffe00000)
     from space 1024K, 85% used [0x00000000ffe00000,0x00000000ffedbca0,0x00000000fff00000)
     to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
     ParOldGen       total 10240K, used 4104K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
     object space 10240K, 40% used [0x00000000fec00000,0x00000000ff002020,0x00000000ff600000)
     Metaspace       used 3327K, capacity 4556K, committed 4864K, reserved 1056768K
     class space    used 361K, capacity 392K, committed 512K, reserved 1048576K

     GC日志解释：
     GC日志以GC或Full GC说明这次垃圾收集的停顿类型，如果有Full，则说明此次GC发生了Stop the World
     PSYoungGen标识垃圾收集器和发生垃圾收集的区域，PS表示Parallel Scavenge收集器，YoungGen表示新生代
     方括号内部6783K->879K(9216K)标识GC前该内存区域已使用容量-->GC后该区域已使用容量
     方括号外部后面的6783K->4983K(19456K)表示GC前java堆已使用容量->GC后java堆已使用容量（java堆总容量）

     内存分析：
     本方法使用VM参数后的jvm日志eden space 8192K,from space 1024K,to   space 1024K,可看出eden区域是8M，两个Survivor区域分别是1M，
     新生代总可用空间为9216KB（Eden区+1个Survivor区的总容量）

     垃圾收集器运行解释：
     执行方法时，当执行allocation4对象的语句时会发生一次Minor GC，这次GC的结果是新生代内存由6783K缩小至879K【日志中6783K->879K(9216K)】，
     而总内存占用没有怎么减少【日志中6783K->4983K(19456K)】，因为allocation1，allocation2，allocation3三个对象都是存活的，虚拟机机务没有找到
     可回收对象。这次GC发生的原因是给allocation4分配内存的时候，发现Eden已经被占用6MB，剩余空间已经不足
     以分配allocation4所需的4M内存（新生代总可用空间为9216KB（Eden区+1个Survivor区的总容量）），因此发生Minor GC.GC期间虚拟机又发现
     已有的3个2MB大小的对象全部无法放入Survivor空间（Survivor空间之后1MB大小），所以只好通过分配担保机制提前转移到老年代去。
     这次GC结束后，4MB的allocation4对象顺利分配在Eden中，因此程序执行完的结果是Eden占用4MB（被allocation4占用），Survivor空间，老年代
     被占用6MB（被allocation1，allocation2，allocation3占用）

     */
    public static void testAllocation(){
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2*_1MB];
        allocation2 = new byte[2*_1MB];
        allocation3 = new byte[2*_1MB];
        allocation4 = new byte[4*_1MB];
    }
}

```

### 大对象直接进入老年代
- 大对象是指需要大量连续内存空间的java对象，最典型的是长字符串和数据，经常出现大对象容易导致内存还有不少空间时就得提前触发垃圾收集以获取
足够的连续空间来存放大对象。
- 虚拟机提供-XX:PretenureSizeThreshold参数，大于这个设置值的对象直接在老年代分配，避免Eden区以及两个Survivor区之间发生大量内存复制
（新生代采用复制算法收集内存）
- 虚拟机采用分代收集的思想管理内存，默认对象在Eden区域第一次出生并经过第一给Minor GC后仍然存活，并且能被Survivor容纳的话，会被移动到
Survivor空间，并且对象年龄设为1，之后每经过一次Minor GC则年龄增加1，一般15岁后会晋升到老年代，通过-XX:MaxTenuringThreshold设置
```java
package capter03;

/**
 * 通过设置对象晋升到老年代的年龄
 */
public class TestMaxTenuringThreshold {
    private static final int _1MB = 1024*1024;

    public static void main(String[] args){
        testTenuringThreshold();
    }

    /**
     * VM args:-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     *
     * VM args解释：-verbose:gc表示输出java虚拟机中GC的详细情况
     * -Xms20M:表示限制虚拟机堆内存最小是20M
     * -Xmx20M:表示限制虚拟机堆内存最大是20M
     * -Xmn10M:表示限制虚拟机的新生代区域为10M（包括一个Eden区域和两个Survivor区域）
     * -XX:PrintGCDetails:表示打印出GC详细信息
     * -XX:SurvivorRatio=8表示Eden区域与一个Survivor区域比例是8:1
     * -XX:MaxTenuringThreshold=1表示限制对象晋升到老年代的年龄是1岁
     *
     * 此时堆内存一共20M，新生代区域10M，其中Eden区域8M，两个Survivor区域分别是1M，老年代10M
     *
     * GC日志解析：VM args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
         [GC (Allocation Failure) --[PSYoungGen: 6783K->6783K(9216K)] 14975K->14983K(19456K), 0.0027136 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         [Full GC (Ergonomics) [PSYoungGen: 6783K->0K(9216K)] [ParOldGen: 8200K->8905K(10240K)] 14983K->8905K(19456K), [Metaspace: 3320K->3320K(1056768K)], 0.0041530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         Heap
         PSYoungGen      total 9216K, used 4342K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         eden space 8192K, 53% used [0x00000000ff600000,0x00000000ffa3d8a0,0x00000000ffe00000)
         from space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
         to   space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
         ParOldGen       total 10240K, used 8905K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         object space 10240K, 86% used [0x00000000fec00000,0x00000000ff4b2478,0x00000000ff600000)
         Metaspace       used 3327K, capacity 4556K, committed 4864K, reserved 1056768K
         class space    used 361K, capacity 392K, committed 512K, reserved 1048576K
     1.进行了一次Minor GC，进行GC后新生代GC前后占用分别是6783K->6783K，堆内存GC前后占用分别是14975K->14983K
     2.进行了第二次GC，此时由于Eden区域剩余的内存不足够分配4M的allocation3，所以进行了一次Full GC,也就是Stop the World，
     由于Eden中的对象年龄已经1岁，所以直接移到了老年代，并且成功在新生代分配了allocation3


     */
    public static void testTenuringThreshold(){
        byte[] allocation1,allocation2,allocation3;
        allocation1 = new byte[_1MB*4];
        allocation2 = new byte[_1MB*4];
        allocation3 = new byte[_1MB*4];
        allocation3 = null;
        allocation3 = new byte[_1MB*4];

    }
}

```

### 动态对象年龄判定
- 虚拟机并不是永远的要求对象的年龄必须达到了MaxTenuringThreshold才能晋升老年代，如果在Survivor空间中相同年龄所有对象大小的总和大于
Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无需等到MaxTenuringThreshold中要求的年龄

### 空间分配担保
> 在发生Minor GC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象的总空间，如果这个条件成立，那么Minor GC可以确保是安全的。
>如果不成立，则会查看HandlePromotionFailure设置值是否允许担保失败。如果允许，那么会继续检查老年代最大可用的连续空间是否大于历次晋升
>到老年代对象的平均大小，如果大于，将尝试一次Minor GC，如果小于或者HandlerPromotionFailure设置不允许毛线，那这是改为进行一次Full GC
>新生代使用复制收集算法，但是为了内存利用率，只使用一个Survivor空间作为轮换备份，因此当出现大量对象在Minor GC后仍然存活的情况，就需要
>老年代进行分配担保，把Survivor无法容纳的对象直接进入老年代，老年代要进行担保前提是老年代有容纳这些对象的声音空间，一共有多少对象会活下来
>在实际完成内存回收之前是无法确定的，只好取之前每一次回收晋升到老年代对象容量的平均大小值作为经验值，与老年代的剩余空间进行比较，决定是否
>进行Full GC来让老年代腾出更多空间。
>
# 第四章 虚拟机性能监控与故障处理工具
- 在jdk/bin下，有很多工具
>jps：JVM Process Status Tool,显示指定系统内所有的HotSpot虚拟机进程
>jstat：JVM Statistics Monitoring Tool，用于收集HotSpot虚拟机各方面的运行数据
>jinfo：Configuration Info For Java，显示虚拟机配置信息
>jmap：Memory Map For Java，生成虚拟机的内存存储快照（heapdump文件）
>jhat：JVM Heap Dump Browner，用于分析heapdump文件，他会建立一个Http/Html服务器，让用户可以在浏览器上查看分析结果
>jstack：Stack Trace for Java，显示虚拟机的线程快照

### jps：JVM Process Status Tool，虚拟机进程状况工具
- jps [ options ][ hostid ]
>jps可以列出正在运行的虚拟机进程，并显示虚拟机执行主类名称以及这些进程的本地虚拟机唯一ID（Local Virtual Machine Identifier,LVMID）,
>jdk的其他工具大多需要输入它查询到的LVMID来确定要监控的是哪一个虚拟机进程，与本地操作系统的进程ID一致
>jps -q：只输出LVMID，省略主类名称
>jps -m：输出虚拟机进程启动时传递给主类main()函数的参数
>jps -l：输出主类的全名，如果进程执行的是jar包，输出jar路径
>jps -v：输出虚拟机进程启动时JVM参数

###jstat：JVM Statistics Monitoring Tool，虚拟机统计信息监视工具
- jstat [ option vmid [interval][s|ms] [count] ]，interval和count表示查询间隔和次数，如果省略说明只查一次。
- jstat是用于监视虚拟机各种运行状态信息的命令行工具。它可以显示本地或者远程虚拟机进程中的类加载、内存、垃圾收集、JIT编译等运行数据。
> jstat -class 监视类装载、卸载数量、总空间以及类装载所耗费的时间
> jstat -gc 监视java堆状况，包括Eden区、两个Survivor区、老年代、永久代等容量、已用空间、GC时间合计等信息
> jstat -gccapacity 监视内容与-gc基本相同，但输出主要关注java堆各个区域使用到的最大、最小空间
> jstat -gcutil 监视内容与-gc基本相同，但输出主要关注已使用空间占总空间的百分比
> jstat -gccause 与-gcutil功能一样，但是会额外输出导致上一次GC产生的原因
> jstat -gcnew 监视新生代GC状况
> jstat -gcnewcapacity 监视内容与-gcnew基本相同，输出主要关注使用到的最大、最小空间
> jstat -gcold 监视老年代GC状况
> jstat -gcoldcapacity 监视内容与-gcold基本相同，输出主要关注使用到的最大、最小空间
> jstat -gcpermcapacity 输出永久代使用的最大、最小空间
> jstat -compiler 输出JFT编译器编译过的方法、耗时等信息
> jstat -printcompilation 输出已经被JFT编译的方法

### jinfo：Configuration Info For Java java配置信息工具
- jinfo的作用是实时的查看和调整虚拟机各项参数

### jmap：Memeory Map For Java java内存映射工具
- jmap [ option ] vmid
- jmap命令用于生成堆转储快照（一般称为heapdump或dump文件），还可以查询finalize执行队列，java堆和永久代的详细信息，如空间使用率、当前用
的是哪种收集器等。
> jmap -dump 生成java堆转储快照
> jmap -finalizerinfo 显示在F-Queue中等待Finalizer线程执行finalize方法的对象。只在linux/Solairs中有效
> jmap -heap 显示java堆详细信息，如使用哪种回收期、参数配置、分代状况等
> jmap -histo 显示堆中对象统计信息，包括类、实例数量、合计容量
> jmap -permstat 以ClassLoader为统计口径显示永久代内存状态
> jmap -F 当虚拟机进程对-dump选项没有响应时，可使用这个选项强制生成dump快照
>
### jhat 虚拟机堆转储快照分析工具

### jstack java堆栈跟踪工具
- jstack命令用于生成虚拟机当前时刻的线程快照，也就是当前虚拟机中每一条线程正在执行的方法堆栈集合，目的是定位线程长时间停顿原因，如线程间死锁、
死循环、请求外部资源导致的长时间等待等
- jstack [ option ] vmid
> jstack -F 当正常输出的请求不被响应时，强制输出线程队长
> jstack -l 除堆栈外，显示有关锁的附加信息
> jstack -m 如果调用native方法情况下，可以输出c/c++的堆栈
- java.lang.Thread类的getAllStackTraces()方法可以获取虚拟机中所有线程的StackTraceElement对象，能够完成jstack的功能

### HSDIS JIT生成代码反汇编

## JDK的可视化工具
- JConsole和VisualVM两个JDK的可视化虚拟机监控工具

### JConsole：java监视与管理控制台
- JConsole是一款基于JMX的可视化监视管理工具。是针对JMX Bean进行管理。
- 新生代：Eden Space和Survivor Space，老年代：Tenured Gen

### VisualVM 多合一的故障处理工具
>官方主推的虚拟机故障处理工具，比JProfiler，YouKit等专业收费的工具不逊色，最大的有点是不需要被监控的程序基于特殊的Agent运行，对应用
>程序实际影响很小：
>1.显示虚拟机进程以及进程配置、环境信息（jps，jinfo）；
>2.监视应用程序的CPU，GC，堆，方法区以及线程的信息（jstat，jstack）；
>3.dump以及分析堆转储快照（jmap，jhat）
>4.方法级的程序运行性能分析，找出被调用次数最多、运行时间最长的方法
>5.离线程序快照：收集线程的运行时配置、线程dump、内存dump等信息建立一个快照，可将快照发送给开发者进行bug反馈

# 调优案例分析与实战
### 高性能硬件上的程序部署策略
- PV访问量（Page View）：页面访问量；UV（Unique View）：独立访客访问量；IV：独立IP访问量
> Direct Memory 堆外内存
>JVM启动时分配的内存，称为堆内存，除此之外，在代码中常使用堆外内存，称为Direct Memory,如Nett广泛使用堆外内存，但是这部分内存不归JVM管理，
>GC算法并不会对他们进行回收
- 在高性能硬件上部署应用，主要有两种方式：1.通过64位JDK来使用超大内存；2.使用若干个32位虚拟机建立逻辑集群来利用硬件资源
>使用64位JDK管理大内存需要考虑以下几个问题：1.内存回收导致的长时间停顿（stop the world）；2.现阶段，64位jdk的性能测试结果普遍低于32位jdk
>3.需要保证程序足够稳定，因为这种应用要产生堆溢出就几乎无法产生堆转储快照（因为要产生几十GB甚至更大的dump文件），哪怕产生快照也无法分析
>4.相同程序在64位jdk的消耗内存一般比32位的jdk大，是由于指针膨胀以及数据类型对其补白等因素导致。
### 集群间同步导致的内存溢出
- 亲和式集群
### 堆外内存导致的溢出错误
- Direct Memory 堆外内存
>java中使用NIO时，可以使用Native函数库直接分配堆外内存，但是堆外内存（也就是直接内存 Direct Memory）并不是虚拟机运行时数据区的一部分，
>也不是java虚拟机规范中定义的内存区域，垃圾收集进行时，虚拟机虽然会对Direct Memory进行回收，但是Direct Memory却不像新生代、老年代
>那样，发现空间不足了就通知收集器进行垃圾回收，他只能等待老年代满了后Full GC，然后顺便帮它清理掉内存中的飞起对象。否则它只能一直等到
>抛出内存溢出异常。
>除了java堆和永久代外，以下区域也会占用较多的内存：
>1.Direct Memory：可通过-XX:MaxDirectMemorySize调整大小，内存不足时抛出OutOfMemoryError或者OutOfMemoryError：Direct buffer memory
>2.线程堆栈：可通过-Xss调整大小调整大小，内存不足时抛出StackOverflowError(纵向无法分配，即无法分配新的堆栈)或者OutOfMemoryError:unable
>to create new native thread（横向无法分配，即无法创建新的线程）
>3.Socket缓存区：每个Socket连接都有Receive和Send两个缓冲区，分别占大约37K和25K内存，连接多的话这块内存占用也比较客观。如果无法分配，则可能
>抛出IOException:Too many open file异常
>4.JNI代码：如果代码中使用JNI调用本地库，那本地库使用的内存也不在堆中
>5.虚拟机和GC：虚拟机、GC的代码执行也需要消耗一定的内存
### 外部命令导致系统缓慢
>执行外部shell脚本是通过java的Runtime.getRuntime.exec()方法来调用的，这种调用方法虽然可以达到目的，但是他在java虚拟机中是非常消耗资源
>的操作，即使外部命令本身能很快执行完毕，频繁调用时创建进程的开销也非常可观。java虚拟机执行这个命令的过程是：首先克隆一个和当前虚拟机拥有
>一样环境变量的进程，再用这个新的进程去执行外部命令，最后再退出这个进程。如果频繁执行这个操作，系统的消耗会很大，不仅仅是CPU，内存的负担也
>很大，改用java的API去获取这些信息后，会得到很大的改善。
###服务器JVM进程崩溃
>使用异步调用web服务，但是由于两边服务速度完全不对等，时间越长，积累了越来越多web服务没有调用完成，导致在等待的线程和Socket连接越来越多，
>最终超过虚拟机的承受能力后使得虚拟机崩溃，可以将异步调用改成生产者消费者模式的消息队列来优化这一现象。
###不恰当的数据结构导致内存占用过大
###由Windows虚拟内存导致的长时间停顿
> -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -Xloggc:gclog.log
>-XX:+PrintReferenceGC
>-Dsum.awt.keepWorkingSetOnMinimize=true
>
## 实战：Eclipse运行速度调优
>JMX(JAVA Management Extensions)是一个为应用程序植入管理功能的框架。JMX是一套标准的代理和服务。实际上，用户可以在任何java应用程序中
>使用这些代理和服务实现管理。主要用于对java应用程序和JVM进行监控和管理。JConsole和JVisualVM能够监控到java应用程序和JVM的相关信息都是
>通过JMX来实现的。
> -XX:MaxPermSize指定永久代最大容量
>jdk中永久代的最大内存是64M
> -Xverify:none 禁止掉字节码验证
>java虚拟机内置两个运行时编译器，如果一段java方法被调用次数达到一定程度，就会被判定为热点代码交给JIT编译器即时编译为本地代码，提高运行速度，
>这也是HotSpot名称的由来
>这也是HotSpot名称的由来

# 虚拟机执行子系统
## 类文件结构
- 语言无关性的基础是虚拟机和字节码存储格式。java虚拟机不和包括java在内的任何语言绑定，它只与class文件这种特定的二进制文件格式所关联，Class文件中包含了java虚拟机指令集和符号表以及
若干其他辅助信息。参见图Java虚拟机提供的语言无关性
## Class类文件的结构
>  Class文件是一组以8个字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑的排列在Class文件中，中间没有添加任何分隔符，当遇到需要占用8
>位字节以上空间的数据项时，则会按照高位在前的方式分割成若干个8位字节进行存储。根据java虚拟机规范的规定，Class文件格式采用一种类似于C语言
>结构体的伪结构来存储数据，这种伪结构中只有两种数据类型：无符号数和表。
>  无符号数属于基本的数据类型，以u1，u2，u4，u8来分别代表1个字节、2个字节、4个字节和8个字节的无符号位，无符号位可以用来描述数字、索引引用、
>数量值或者按照UTF-8编码构成字符串值。
>  表是由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表都习惯的以_info结尾，表用于描述有层次关系的复合结构的数据，整个Class文件
>本质上就是一张表 参见Class文件格式
### 魔数与Class文件的版本
>  每个Class文件的头4个字节称为魔数，它的唯一作用就是确定这个文件是否为一个能被虚拟机接受的Class文件。使用魔数而不是扩展名来进行识别主要
>是基于安全方面的考虑，因为文件扩展名可以随意改动。Class文件的魔数值为0xCAFEBABE(咖啡宝贝)，紧接着魔数的4个字节存储的是Class文件的
>版本号：第五个和第六个字节是次版本号（Minor Version），第七个和第八个字节是主版本号（Major Version）。java的版本号是从45开始，JDK1.1
>之后的每个JDK大版本发布主版本号向上加1，高版本的jdk能向下兼容以前版本的Class文件，但不能运行以后版本的Class文件，虚拟机拒绝执行超过
>其版本号的Class文件。jdk1.0-1.1 45;1.1-1.2 46;1.2-1.3 47;1.3-1.4 48; 1.4-1.5 49; 1.5-1.6 50;  1.6-1.7 51;...
### 常量池
>  十六进制表示的二进制范围是0000-1111，也就是十六进制1位是二进制4位，一个字节是8位，也就是一个字节是2位16进制
>  紧接着主次版本号之后是常量池入口，常量池可以理解成Class文件之中的资源仓库，它是Class文件结构中与其他项目关联最多的数据类型，也是占用Class
>文件空间最大的数据项目之一，同时也是Class文件中第一个出现的表类型数据项目。
>  由于常量池中的常量的数量不是固定的，所以在常量池的入口需要放置一项u2类型的数据(由前面的值u2代表两个字节)，代表常量池容量计数器(constant_pool_count)。
>这个容量计数是从1开始，而不是从0开始。由前面得知两个字节也就是16位，每4位是一个16进制数，那么也就是4位十六进制数，
>如图常量池结构所示，常量池容量为十六进制的0x0016，即二进制的22，这代表常量池中有21项常量，索引值范围是1-21.
>  常量池中主要存放两大类常量：字面量（Literal）和符号引用（Symbolic References）。字面量比较接近于java语言层面的常量概念，如文本字符串
>、声明为final的常量值等。而符号引用则属于编译原理方面的概念，包括了下面三类常量:
>>1.类和接口的全限定名
>>2.字段的名称和描述符
>>3.方法的名称和描述符
>java代码在进行javac编译的时候，不会有C和CC的连接步骤，而是在虚拟机加载Class文件的时候进行动态连接。也就是说，在Class文件中不会保存
>各个方法、字段的最终内存布局信息，因此这些字段、方法的符号引用不经过运行期间转换的话无法得到真正的内存入口地址，也就无法直接被虚拟机使用。
>当虚拟机运行时，需要从常量池获得对应的符号引用，再在类创建时或运行时解析、翻译到具体的内存地址。
- 按照规则分析Class文件字节码
> CA FE BA BE 00 00 00 32 00 16 07 00 02 01 00 1D
> 6F 72 67 2F 66 65 6E 69 78 73 6F 66 74 2F 63 6C
> 61 7A 7A 2F 54 65 73 74 43 6C 61 73 71 07 00 04
> 01 00 10 6A 61 76 61 2F 6C 61 6E 67 2F 4F 62 6A
>开始分析：前四个字节：CA FE BA BE 是Class文件的魔数；
>紧接着两个字节：00 00是两个字节的小版本号； 
>紧接着两个字节：00 32 是两个字节的主版本号，换算成10进制是50，也就是JDK1.5
>紧接着两个字节：00 16 是常量池入口，表示常量池容量计数器(constant_pool_count)，换算成十进制是22，常量池计数从1开始计算，也就表示常量池内共有21个常量
>紧接着一个字节：07是表类型的标志位，参考图常量池的项目类型，表示CONSTANT_Class_info(类或接口的符号引用)
>CONSTANT_Class_info的结构是如下：
> 类型  |  名称  |  数量
> u1   |  tag   |   1
> u2   |name_index| 1 
>也就是07是标志位
>接下来两个字节：00 02是标识name_index的值，也即是指向了常量池中的第二项常量，
>那么继续查找第二项常量，也就是下个字节01是标志位，参考图常量池的项目类型可知是一个CONSTANT_Utf8_info类型的常量，
>Constant_Utf8_info类型的结构如下
> 类型    |    名称    |数量
> u1      |   tag     |  1
> u2      |   length  |  1
>u1       |   bytes   |length
>length值说明这个UTF-8编码的字符串长度是多少字节，它后面紧跟着长度为length字节的连续数据是一个使用UTF-8缩略编码表示的字符串。
>由于Class文件中方法、字段等都需要引用CONSTANT_Utf8_info型常量来描述名称，所以CONSTANT_Utf8_info型常量的最大长度也就是
>java中方法、字段名最大长度。而这里的最大长度就是length中最大值，即u2类型能表达的最大值2个字节，2的16次方=65535.所以
>java程序中如果定义超过64KB英文字符的变量或方法名，将会无法编译。
>继续翻译，00 1D表示length，转成十进制是29字节，也就是往后29个字节是内容，内容是org/fenixsoft/clazz/TestClass

- 可以使用javap工具的-verbose参数输出Class文件字节码内容
### 访问标志
>常量池结束后，紧接着两个字节代表访问标志（access_flags），这个标志用于识别一些类或者接口层次的访问信息，包括：这个Class是个类还是接口；
>是否定义为public类型；是否定义为abstract类型；如果是类的话，是否被声明为final等，详情见访问标志图
### 类索引、父类索引与接口索引集合
>类索引和父类索引都是一个u2类型的数据，而接口索引集合是一组u2类型的数据的集合，Class文件中由这三项数据来确定这个类的继承关系
>类索引、父类索引和接口索引集合都是按照顺序排列在访问标志之后，类索引和父类索引用两个u2类型的索引值表示，他们各自指向一个类型为
>CONSTANT_Class_info的类描述符常量，通过CONSTANT_Class_info类型的常量中的索引值可以找到定义在CONSTANT_Utf8_info类型的常量中的全限
>定名字符串
>对于接口索引集合，入口的第一项--u2类型的数据为接口计数器，表示索引表的容量。如果该类没有实现任何接口，则该计数器的值为0，后面接口的索引
>表不在占用任何字节。参见图类索引、父类索引、接口索引集合
### 字段表集合
>字段表（field_info）用于描述接口或者类中声明的变量。字段（field）包括类级变量以及实例级变量，但不包括在方法内部声明的局部变量。java中
>描述一个字段包括的信息有：字段的作用域（public,private,protected修饰符）、是实例变量还是类变量（static修饰符）、可变性（final）、
>并发可见性（volatile修饰符，是否强制从主内存读写）、可否被序列化（transient修饰符）、字段数据类型（基本类型、对象、数组）、字段名称。
>上述信息中，各个修饰符都是布尔值，要么有某个修饰符，要么没有，很适合使用标志位来标识。而字段叫什么名字、字段被定义为什么数据类型，这些都是
>无法固定的，只能引用常量池中的常量来描述。参见图字段表结构
### 方法表集合
>Class文件存储格式中对方法的描述与对字段的描述几乎采用完全一致的方式，方法表的结构如同字段表一样，一次包括访问标志、名称索引、描述符索引、
>属性表集合几项。方法中由于volatile和transient关键字不能修饰方法，相对的synchronized、native、strictfp和abstract关键字可以修饰方法。
>方法里的方法体，经过编译器编译成字节码指令后，存放在方法属性表集合中一个名为code的属性里面。
>Class文件中可能会出现由编译器自动添加的方法，最典型的便是类构造器<clinit>方法和实例构造器<init>方法
### 属性表集合

## 字节码指令简介

# 第7章 虚拟机类加载机制
## 类加载的时机
- 类从被加载到虚拟机内存中开始，到卸载出内存为止，他的整个生命周期包括加载、验证、准备、解析、初始化、使用、卸载7个阶段，其中验证、准备、解析
3个部分统称为连接。如图类的生命周期
- 加载、验证、准备、初始化和卸载这5个阶段的顺序是确定的，类的加载过程必须按照这种顺序按部就班的开始，而解析阶段不一定：它在某些情况下可以在
初始化阶段之后再开始，这是为了支持java语言的运行时绑定。
>java虚拟机规范严格规定只有五种情况必须立即对类进行初始化（而加载、验证、准备自然需要在此之前开始）：
>1.遇到new、getstatic、putstatic或invokestatic这4条字节码指令时，如果类没有进行过初始化，则需要先触发其初始化。生成这4条指令的最常见
>的java代码场景是：使用new关键字实例化对象的时候、读取或者设置一个类的静态字段（被final修饰、已在编译期把结果放入常量池的静态字段除外）的时候，
>以及调用一个类的静态方法的时候
>2.使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没有进行过初始化，则需要先出发其初始化。
>3.当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发父类的初始化
>4.当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类），虚拟机会先初始化这个主类
>5.当使用jdk1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandler实例最后的解析结果REF_getStatic、REF_putStatic、REF——invokeStatic
>的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化
```java
package capter07;

/**
 *
 */
public class Test {
    public static void main(String[] args){
        /**
         * 非主动使用类字段演示
         * 只会输出SubClass init!。对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过子类来引用父类中定义的静态字段，
         * 只会触发父类的初始化而不会触发子类的初始化。通过-XX:TraceClassLoading参数可以观察到在运行过程中类的加载
         */
//        System.out.println(SubClass.value);
        /**
         * 被动使用类字段演示二：通过数组定义来引用类，不会触发此类的初始化
         */
//        SuperClass[] superClasses = new SuperClass[10];

        /**
         * 被动使用类字段演示三：常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会
         * 触发定义常量的类的初始化
         * 因为在java源码中引用了ConstClass类中的常量HELLOWORLD，但其实在编译阶段通过常量传播优化，已经将此常量的
         * 值“hello world”存储到了Test类的常量池中，以后Test对常量ConstClass.HELLOWORLD的引用实际上都被转化成
         * Test类对自身常量池的引用了。也就是说，实际上Test的Class文件之中并没有ConstClass类的符号引用入口，这两个
         * 类在编译成Class之后就不存在任何联系了。
         */
        System.out.println(ConstClass.HELLOWORLD);
    }
}

/**
 * 被动使用类字段演示一：通过子类引用父类的静态字段，不会导致子类初始化
 */
class SuperClass{
    static {
        System.out.println("SuperClass init");
    }
    public static int value = 123;
}

class SubClass extends SuperClass{
    static {
        System.out.println("SubClass init!");
    }
}

/**
 *
 */
class ConstClass{
    static {
        System.out.println("ConstClass init!");
    }
    public static final String HELLOWORLD = "hello world";
}

```
>接口也有初始化过程，接口与类的初始化区别是当一个类在初始化时，要求其父类全部都已经初始化过了，但是一个接口在初始化时，并不要求其父类接口全部
>完成了初始化，之后在真正使用到父接口的时候（如引用接口中定义的常量）才会初始化。
## 7.3 类加载的过程
- 类加载的过程，也就是加载、验证、准备、解析、初始化这五步。
- 加载
>加载是类加载过程的一个阶段，在加载阶段，虚拟机需要完成以下3件事情：
>1.通过一个类的全限定名来获取定义此类的二进制字节流。
>2.将这个字节流所代表的的静态存储结构转化为方法区的运行时数据类型
>3.在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据访问入口
>由于上面第一点中获取类二进制字节流的方式不同，形成了从ZIP包中读取（JAR/EAR/WAR）、从网络中读取（applet）、运算时计算生成（动态代理技术）、
>由其他文件生成（JSP）、从数据库中读取
>非数组类的加载阶段既可以使用系统提供的引导类加载器来完成，也可以由用户自定义的类加载去完成，开发人员可以通过定义自己的类加载器去控制字节流
>的获取方式（即重写一个类加载器的loadClass()方法）。
>数组类本身不通过类加载器创建，是由java虚拟机直接创建的数组类的元素类型（Element Type，指的是数组去掉所有纬度的类型）最终是要靠类加载器
>去创建，1.如果数组的组件类型是引用类型，那么递归加载这个组件类型，数组在加载该组件类型的类加载器的类名称空间上被标识；2.如果数组的组件类型
>不是引用类型，java虚拟机将会把数组标记为引导类加载器关联；3.数组类的可见性与他的组件类型的可见性一致，如果组件类型不是引用类型，那数组类
>的可见性将默认为public
>加载阶段完成后，虚拟机外部的二进制字节流就按照虚拟机所需的格式存储在方法区之中，方法区中的数据存储格式由虚拟机实现自行定义，然后在内存中
>实例化一个java.lang.Class类的对象，这个对象作为程序访问方法区中的这些类型数据的外部接口。
>加载与连接阶段的部分内容是交叉进行的，加载阶段尚未完成，连接阶段可能已经开始，但这些夹在加载阶段之中进行的动作，仍然属于连接阶段的内容，
>这两个阶段的开始时间仍然保持着固定的先后顺序。
- 验证
- 这一阶段的目的是为了确保Class文件的字节流中包含的信息符合当前虚拟机的要求
>1.文件格式验证
>>第一阶段要验证字节流是否符合Class文件格式的规范，并且能被当前虚拟机版本处理，包括：1.是否以魔数0xCAFEBABE开头；2.主、次版本号是否
>>在当前虚拟机处理范围之内；3.常量池的常量中是否有不被支持的常量类型；4.指向常量的各种索引值是否有指向不存在的常量或不符合类型的常量；
>>5.CONSTANT_Utf8_info型的常量是否有不符合UTF8编码的数据；6.Class文件中各个部分及文件本身是否有被删除的或附加的其他信息.......
>只有通过了文件格式验证阶段，字节流才会进入到内存的方法区中进行存储，后面的3个阶段全部是基于方法区的存储结构进行，不会再直接操作字节流。
>2.元数据验证
>对字节码描述的信息进行语义分析和校验，保证不存在不符合java语言规范的元数据信息，包括1.这个类是否有父类（除了Object之外，所有的类都应当
>有父类）；2.这个类的父类是否继承了不允许被继承的类（final）；3.如果这个类不是抽象类，是否实现了其父类或接口之中要求实现的所有方法；
>4.类中的字段、方法是否与父类产生矛盾，
>3.字节码验证
>通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。在第二阶段对元数据信息中的数据类型昨晚校验后，这个阶段对类的方法体进行校验分析，
>如1.保证任意时刻操作数栈的数据类型与指令代码序列都能配合工作；2.保证跳转指令不会跳转到方法体以外额字节码指令上；3.保证跳转指令不会跳转
>到方法体以外的字节码指令上；4.保证方法体中的类型转换是有效的。
>4.符号引用验证
>发生在虚拟机将符号引用转化为直接引用的时候，这个转化动作在连接的第三个阶段-解析阶段中发生，主要校验1.符号引用中通过字符串描述的全限定名
>是否能找到对应的类；2.在指定类中是否存在符合方法的字段描述符以及简单名称所描述的方法和字段；3.符号引用的类、字段、方法的访问性是否可被
>当前类访问。如果符号引用验证未通过，将抛出java.lang.IncompatibleClassChangeError异常的子类，如java.lang.IllegalAccessError,
>java.lang.NoSuchFieldError,java.lang.NoSuchMethodError。

















- 2019年9月6日 16:21:40 43/460
- 2019年9月9日 08:30:35 50/460
- 2019年9月9日 12:17:02 53/460
- 2019年9月9日 18:13:51 56/460
- 2019年9月10日 20:00:32 61/460
- 2019年9月11日 11:16:48 65/460
- 2019年9月16日 12:19:27 69/460
- 2019年9月16日 19:44:25 94/460
- 2019年9月17日 16:51:26 101/460
- 2019年9月17日 19:43:56 108/460
- 2019年9月18日 20:29:27 132/460
- 2019年9月19日 19:51:14 154/460
- 2019年9月20日 21:25:50 173/460
- 2019年9月26日 16:23:11 219/460

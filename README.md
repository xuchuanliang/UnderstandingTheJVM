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
>Serial收集器由于没有线程交互的开销，可以获得最高的单线程收集效率。
- ParNew收集器
>ParNew收集器实际上是Serial收集器的多线程版本，除了使用多线程进行垃圾收集之外，其他行为包括Serial可用的所有控制参数、收集算法、Stop the World/
>对象分配规则、回收策略等都一样。参见ParNew-Serial Old收集器运行示意图
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


-- 2019年9月6日 16:21:40 43/460
-- 2019年9月9日 08:30:35 50/460
-- 2019年9月9日 12:17:02 53/460
-- 2019年9月9日 18:13:51 56/460
-- 2019年9月10日 20:00:32 61/460
-- 2019年9月11日 11:16:48 65/460
-- 2019年9月16日 12:19:27 69/460
-- 2019年9月16日 19:44:25 94/460

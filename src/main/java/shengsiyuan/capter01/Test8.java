package shengsiyuan.capter01;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/*
    当前类加载器（Current Classloader）

    每个类都会使用自己的额类加载器（即加载自身的类加载器）来去加载其他类（指
    的是所依赖的类），如果ClassX引用了ClassY，那么ClassX的类加载器就会去加载ClassY（前提是ClassY尚未被加载）

    线程上下文类加载器（Context Classloader）

    线程上下文类加载器是从JDK1.2开始引入的，类Thread中的getContextClassloader()与setContextClassLoader(ClassLoader cl)分
    别用来获取和设置上下文类加载器

    如果没有通过setContextClassLoader(ClassLoader cl)进行设置的话，线程将继承其父线程的上下文类加载器。java应用运行时的初始线
    程的上下文类加载器是系统类加载器。在线程中运行的代码可以通过该类加载器来加载类与资源。

    线程上下文类加载器的重要性：

    SPI（Service Provider Interface）

    父ClassLoader可以使用当前线程Thread.currentThread().getContextClassLoader()所指定的ClassLoader加载的类。这就改变的父
    ClassLoader不能使用子ClassLoader或是其他没有直接父子关系的ClassLoader加载的类的情况，即改变了双亲委托模型。

    线程上下文类加载器就是当前线程的Current ClassLoader

    在双亲委托模型下，类加载是由下至上的，即下层的类加载器会委托上层进行加载。但是对于SPI来说，有些接口是java核心库所提供的，
    而java核心库是由启动类加载器来加载的，而这些接口的实现却来自于不同的jar包（厂商提供，如JDBC），java的启动类加载器是不会加载
    其他来源的jar包，且类加载器加载的类下层能访问上层，但是上层加载的类无法访问到下层类加载器加载的类，这样传统的双亲委派模型就
    无法满足SPI的要求。而通过给当前线程设置上下文类加载器，就可以由设置的上下文类加载器来实现对接口实现类的加载

 */
public class Test8 {
    public static void main(String[] args) {
//        Thread.currentThread().setContextClassLoader(Test8.class.getClassLoader().getParent());
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator = load.iterator();
        while (iterator.hasNext()){
            System.out.println();
            Driver driver = iterator.next();
            System.out.println("driver: "+driver.getClass()+", load: "+driver.getClass().getClassLoader());
        }
        System.out.println("当前线程上下文加载器："+Thread.currentThread().getContextClassLoader());
        System.out.println("serviceLoader的类加载器："+load.getClass().getClassLoader());
    }
}

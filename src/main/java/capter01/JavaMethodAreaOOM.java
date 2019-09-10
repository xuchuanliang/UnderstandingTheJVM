package capter01;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 方法区存放Class相关信息，如类名，访问修饰符、常量池、字段描述、方法描述等
 * 产生的大量的类填充方法区，直到溢出
 * VM Args:-XX:permSize=10M -XX:MaxPermSize=10M
 */
public class JavaMethodAreaOOM {

    public static void main(String[] args){
        while (true){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(JavaMethodAreaOOM.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invoke(o,objects);
                }
            });
            enhancer.create();
        }
    }
}

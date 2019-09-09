package capter01;

/**
 * java虚拟机栈实现内存溢出
 * VM Args:-Xss2M
 */
public class JavaVMStaticOOM {

    private void dontStop(){
        while (true){}
    }

    public void stackLeakByThread(){
        while (true){
            new Thread(()->{
                dontStop();
            }).start();
        }
    }

    public static void main(String[] args){
        JavaVMStaticOOM javaVMStaticOOM = new JavaVMStaticOOM();
        javaVMStaticOOM.stackLeakByThread();
    }


}

package V3;

import java.util.ArrayList;
import java.util.List;

public class FillHeap {
    private static class OOMObject{
        public byte[] placeholder = new byte[64 * 1024];
    }

    /**
     * JVM参数：-Xmx100M -Xms100M -XX:+UseSerialGC
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }
}

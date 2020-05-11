package temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test1 {
    public static void main(String[] args) {
//        char[] char1 = {'a','b'};
//        char[] char2 = new char[]{'a','b'};
//        char[] char3 = new char[2];
//        char3[0] = 'a';
//        char3[1] = 'b';
//        System.out.println(char1);
//        System.out.println(char2);
//        System.out.println(char3);
//        Test11 test11 = new Test11();
//        Test11 test12 = new Test11();
//        System.out.println(test11.getObject()==test12.getObject());
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(12);
        Map<String,String> map = new HashMap<>();
        map.put("张三","李四");
    }

}
class Test11{
    static Object object = new Object();

    public Object getObject(){
        return object;
    }
}

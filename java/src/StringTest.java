import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringTest {
     static final Integer finalInt1 = 100;
    public static void main(String[] args) {



        // 测试常量池存放在哪里
        // 怎么判断呢？ 就看 内存溢出的时候  报的错误
        // 如果是 PermGen space 溢出的话，那么就是 静态区
        // 如果是 heap space 的话，就是在堆空间
        List<String> list = new ArrayList<>();
        while (true) {
            list.add(String.valueOf(System.currentTimeMillis()).intern());

        }


//        Integer int1 = 99;
//        int1++;
//        Integer[] ints  = new Integer[]{100,2,3};
//        int[] ints2  = ints;
//        int[] ints2  = new int[]{1,2,3};



//        System.out.println(ints == ints2);
//        System.out.println(int1 == finalInt1);
//        int1++;
//        System.out.println(int1== finalInt1);
//        System.out.println(int1.intValue() == finalInt1.intValue());





////        char c = '中';
//        String a = "a";
//        String b = "a";
//        String c = new String("a");
//        System.out.println(" (a==b)  "+ (a==b));
//        System.out.println("(a==c)  "+ (a==c));
//        byte[] bytes = new byte[]{97,97,97,97};
//        String butesStr = new String(bytes);
//        String butesAStr = new String("aaaa");
//        String butesAStr2 = ("aaaa");
//        System.out.println("butesStr  "+ butesStr);
//        System.out.println("butesStr  "+ butesStr.equals(butesAStr));
//        System.out.println("butesStr  "+ (butesStr==(butesAStr)));
//        System.out.println("(butesStr==(butesAStr2))  "+ (butesStr==(butesAStr2)));
//
//
//        int random  = new Random().nextInt(2);
//        System.out.println("random  "+ random);
//
//        String v1 = "0";
//        String randomS = new String(random+"");
//        for(int i = 0 ; i<=random;i++){
//            String iS = new String(i+"");
//            System.out.println("i equals "+ i+" "+(v1.equals(iS)));
//            System.out.println("i == "+ i+" "+(v1==(iS)));
//        }
//
//        System.out.println(a.equals(b));
//        System.out.println(a.equals(c));
//
    }
}

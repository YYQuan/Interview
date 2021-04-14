import java.util.Arrays;
import java.util.stream.Stream;

public class StreamTest {
    
    public static void main(String[] args) throws InterruptedException {
        int[] ints1 = new int[10000];
        int[] ints2 = new int[10000];
        Arrays.fill(ints1,1);
        Arrays.fill(ints2,23);

        int integerI =  Arrays.stream(ints1)
                .reduce((integer, integer2) ->{
                    System.out.println(integer+"   " +integer2);
                    return 10;
                } ).getAsInt();
        int integerI2 =  Arrays.stream(ints2)
                .reduce((integer, integer2) ->{
                    System.out.println(integer+"   " +integer2);
                    return 10;
                } ).getAsInt();

        System.out.println(integerI);
    }
}

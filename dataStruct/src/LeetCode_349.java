import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.ToIntFunction;

public class LeetCode_349 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_349 code = new LeetCode_349();
        int[] ints  = new int[]{1,2,2,1};
//        ints  = new int[]{4,9,5};
        int[] ints2  = new int[]{2,2};
//        ints2  = new int[]{9,4,9,8,4};


        for(int i :code.solution2(ints,ints2)){
            System.out.print( i);
            System.out.print( " ");

        }

    }



    public  int[]  solution(int[] arrA ,int[] arrB){

        if(arrA == null || arrB == null || arrA.length<=0 || arrB.length<=0){
            return null;
        }

//        int[]  tmp = arrA.length< arrB.length?arrA:arrB;
//        int[]  tmpOther = arrA.length< arrA.length?arrA:arrB;

        HashMap<Integer,Integer> map = new HashMap<>();
        for(int i : arrA){
            map.put(i,1);
        }

        for(int i :arrB){

            if(map.keySet().contains(i)){
                map.put(i,2);
            }

        }

        ArrayList<Integer> result = new ArrayList();
        for(int i :map.keySet()){
            if(map.get(i)>1){
                result.add(i);
            }

        }

        return result.stream().mapToInt(value -> value).toArray();

    }

    public int[] solution2(int[] arrA ,int[] arrB){


        if(arrA == null|| arrB == null || arrA.length<=0 || arrB.length<=0){
            return null;
        }

        HashSet<Integer> tmpSet = new HashSet<>();
        HashSet<Integer> resultSet = new HashSet<>();

        for(int i  :arrA){
            tmpSet.add(i);
        }

        for(int i :arrB){
            if(tmpSet.contains(i)){
                resultSet.add(i);
            }
        }

        return resultSet.stream().mapToInt(value -> value).toArray();
    }


}

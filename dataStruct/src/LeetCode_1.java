package src;

import java.util.HashMap;

public class LeetCode_1 {

    public static void main(String[] args) {
        LeetCode_1 code = new LeetCode_1();
        int[] ints  = new int[]{2,7,11,15};
        int[] result = code.solution(ints,9);
        System.out.println(result[0]+"  "+result[1]);

    }


    public  int[] solution(int[] arr  ,int target){

        HashMap<Integer,Integer> map = new HashMap();
        for(int i = 0 ; i< arr.length ;i++){
            int value = target - arr[i];
            if(map.containsKey(value)){
                return new int[]{i,map.get(value)};
            }
            map.put(arr[i],i);
        }

        return null;
    }

}

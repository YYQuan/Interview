package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class LeetCode_454 {

    public static void main(String[] args) {
        LeetCode_454 code = new LeetCode_454();
        int[] intsA  = new int[]{1,2    };
        int[] intsB  = new int[]{-2,-1};
        int[] intsC  = new int[]{-1,2};
        int[] intsD  = new int[]{0,2};

        int result = code.fourSumCount(intsA,intsB,intsC,intsD);

//        System.out.println(Arrays.toString(ints));
        System.out.println(result);




    }


    // 击败百分之五
    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {

        int result = 0 ;
        HashMap<Integer,Integer> mapAB = new HashMap<>();
        for(int i = 0 ; i<A.length;i++){
            for(int j = 0;j<B.length;j++){
                updateInteger(mapAB,A[i]+B[j]);
            }
        }

        HashMap<Integer,Integer> mapCD = new HashMap<>();

        for(int i = 0; i<C.length;i++){
            for(int j = 0 ; j<D.length;j++){
                updateInteger(mapCD,C[i]+D[j]);
            }
        }


        for(Integer keyAB :mapAB.keySet()){

            if(mapCD.containsKey(-keyAB)){

                int countKeyAB =mapAB.get(keyAB);
                int countKeyCD = mapCD.get(-keyAB);

                result += (countKeyAB*countKeyCD);

            }

        }


        return result;


    }

    public void updateInteger(HashMap<Integer,Integer> map ,int key ){
        if(map.containsKey(key)){
            int value = map.get(key);
            map.put(key,value+1);

        }else{
            map.put(key,1);
        }
    }

}

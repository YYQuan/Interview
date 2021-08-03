package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LeetCode_350 {

    public static void main(String[] args) {
        LeetCode_350 code = new LeetCode_350();
//        int[] ints  = new int[]{1,2,2,1};
//        int[] ints2  = new int[]{2,2};
        int[] ints  = new int[]{4,9,5};
        int[] ints2  = new int[]{9,4,9,8,4};
        int[] result = code.intersect(ints,ints2);
        for(int i = 0;i<result.length;i++) {
            System.out.print(result[i] +" ");
        }

    }


    // 解法 1.0
    public int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map1 = new HashMap<>();
        HashMap<Integer, Integer> result = new HashMap<>();


        for(int i : nums1){
            updateMap(map1,i);
        }

        int len =0;
        for(int i :nums2){
            if(map1.containsKey(i)){
                if(result.containsKey(i)){

                    int resultMapCount =result.get(i);
                    int mapCount =map1.get(i);

                    if(resultMapCount<mapCount){
                        updateMap(result,i);
                        len++;
                    }

                }else{

                    updateMap(result,i);
                    len++;

                }
            }
        }

        int[]  resultArray = new int[len];

        for(Integer index : result.keySet()){
            for(int i = 0 ; i<result.get(index);i++){
                resultArray[--len] = index;
            }
        }
        return  resultArray;


    }

    public void updateMap(HashMap<Integer,Integer> map,int i){
        if(map.containsKey(i)){
            int value = map.get(i)+1;
            map.put(i,value);
        }else{
            map.put(i,1);
        }
    }


    public int[] intersect2(int[] nums1, int[] nums2) {

        if(nums1 == null ||nums2 ==null || nums1.length==0 ||nums2.length==0) return new int[]{};

        List<Integer> resultList = new ArrayList<>();
        HashMap<Integer,Integer> map = new HashMap<>();
        for(int i :nums1){
            map.put(i,map.getOrDefault(i,0)+1);
        }

        for(int i :nums2){
            if(map.keySet().contains(i)){
                int count  = map.getOrDefault(i,0);
                if(count>0) {
                    resultList.add(i);
                    map.put(i,count-1);
                }

            }
        }
        return resultList.stream().mapToInt(p -> p ).toArray();
    }
}

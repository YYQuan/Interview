package src;

import java.util.*;

public class LeetCode_349 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_349 code = new LeetCode_349();
//        int[] nums = new int[]{10,5,2,6};
//        int k = 100;
//        int[] nums = new int[]{10,9,10,4,3,8,3,3,6,2,10,10,9,3};
//        int k = 19;
        int[] nums = new int[]{1,2,3};
        int[] k = new int[]{1,2,3};
//        int[] nums = new int[]{100,100,100,100,3};
//        int k = 10;
        int[] result1 = code.intersection(nums,k);
        int[] result2 = code.intersection2(nums,k);

        for(int i = 0 ; i<result1.length;i++){
            System.out.print(result1[i]+" ");

        }
        for(int i = 0 ; i<result2.length;i++){
            System.out.print(result2[i]+" ");

        }
    }




    public int[] intersection(int[] nums1, int[] nums2) {

        HashSet<Integer> sets = new HashSet<>();
        HashSet<Integer> sets2 = new HashSet<>();

        ArrayList<Integer> list = new ArrayList<>();

        for(int i = 0 ; i<nums1.length;i++){
            sets.add(new Integer(nums1[i]).intValue());
        }

        // 把
        for(int i = 0; i<nums2.length;i++){
            if(sets.contains(nums2[i])){
                sets2.add(nums2[i]);
//                放在这里面判断 速度慢很多
//                if(!list.contains(nums2[i])) {
//                    list.add(nums2[i]);
//
//                }
            }
        }

//        int[ ] result = new int[sets2.size()];
//        int resultIndex = 0 ;
//        for(Integer i :sets2){
//            result[resultIndex++] = i.intValue();
//        }
        // 用循环的方式 速度比 这种方式快
        return sets2.stream().mapToInt(value ->value.intValue()).toArray();
//        return list.stream().mapToInt(value ->value.intValue()).toArray();
//        return result;

    }


    public int[] intersection2(int[] nums1, int[] nums2) {

        if(nums1 == null ||nums2 ==null || nums1.length==0 ||nums2.length==0) return new int[]{};

        Set<Integer> resultList = new HashSet<>();
        HashSet<Integer> set = new HashSet<>();
        for(int i :nums1){
            set.add(i);
        }

        for(int i :nums2){
            if(set.contains(i)){
                resultList.add(i);
            }
        }
        return resultList.stream().mapToInt(p -> p ).toArray();
    }
}

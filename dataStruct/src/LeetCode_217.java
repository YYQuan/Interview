package src;

import java.util.HashSet;

public class LeetCode_217 {

    public static void main(String[] args) {
        LeetCode_217 code = new LeetCode_217();

//        int[] nums1  = new int[]{1,2,3,1};
//        boolean result = code.containsNearbyDuplicate(nums1,3);

//        int[] nums1  = new int[]{1,2,3,1,2,3};
//        boolean result = code.containsNearbyDuplicate(nums1,2);
//        int[] nums1  = new int[]{1,2,1};
//        boolean result = code.containsNearbyDuplicate(nums1,0);
//          int[] nums1  = new int[]{-1,-1};
//        boolean result = code.containsNearbyDuplicate(nums1,1);
          int[] nums1  = new int[]{2147483647,-2147483648,2147483647,-2147483648};
        boolean result = code.containsDuplicate(nums1);
        boolean result2 = code.solution(nums1);



//        System.out.println(result+"  ");
        System.out.println(result+"  ");
        System.out.println(result2+"  ");


    }


    public boolean containsDuplicate(int[] nums) {

        HashSet<Integer> set = new HashSet<>();

        for(int i  = 0 ; i< nums.length;i++){
            if(set.contains(nums[i])){
                return true;
            }

            set.add(nums[i]);
        }

        return false;
    }



    public boolean  solution(int[] nums){
        if(nums==null||nums.length<=1) return false;

        HashSet<Integer> set = new HashSet<>();

        for(int i :nums){

            if(set.contains(i)){
                return true;
            }else{
                set.add(i);
            }

        }
        return false;

    }
}

package src;

import java.util.HashSet;
import java.util.TreeSet;

public class LeetCode_220 {

    public static void main(String[] args) {
        LeetCode_220 code = new LeetCode_220();

//        int[] nums1  = new int[]{1,2,3,1};
//        boolean result = code.containsNearbyAlmostDuplicate(nums1,3,0);

//
        int[] nums1  = new int[]{1,5,9,1,5,9};
        boolean result = code.containsNearbyAlmostDuplicate(nums1,2,3);

//       int[] nums1  = new int[]{-2147483648,2147483647};
//        boolean result = code.containsNearbyAlmostDuplicate(nums1,1,1);

//       int[] nums1  = new int[]{2147483646,2147483647};
//        boolean result = code.containsNearbyAlmostDuplicate(nums1,3,3);



//        int[] nums1  = new int[]{1,2,3,1,2,3};
//        boolean result = code.containsNearbyDuplicate(nums1,2);
//        int[] nums1  = new int[]{1,2,1};
//        boolean result = code.containsNearbyDuplicate(nums1,0);
//          int[] nums1  = new int[]{-1,-1};
//        boolean result = code.containsNearbyDuplicate(nums1,1);
//          int[] nums1  = new int[]{2147483647,-2147483648,2147483647,-2147483648};
//        boolean result = code.containsDuplicate(nums1);



//        System.out.println(result+"  ");
        System.out.println(result+"  ");


    }


    //  击败 33  核心 要用treeset 来二分查找  set中 最贴合的值，
    //  不要使用 hashset    treeset 查找的时间复杂度是 O（logn）  而 set是O（n）
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {

        TreeSet<Long>  valueLongSet = new TreeSet<>();

        for(int i =  0 ; i<nums.length;i++){
            long value = (long)nums[i];
            Long floorValue = valueLongSet.floor(value);
            Long ceilingValue = valueLongSet.ceiling(value);

            if(floorValue!=null&&value-floorValue<=t) return true;
            if(ceilingValue!=null&&ceilingValue-value<=t) return true;

            valueLongSet.add(value);

            if(valueLongSet.size()>k){
                valueLongSet.remove((long)nums[i-k]);
            }


        }


        return false;
    }
}

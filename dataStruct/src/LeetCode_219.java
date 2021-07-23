package src;

import java.util.*;

public class LeetCode_219 {

    public static void main(String[] args) {
        LeetCode_219 code = new LeetCode_219();

        int[] nums1  = new int[]{1,2,3,1};
//        boolean result = code.containsNearbyDuplicate(nums1,3);

//        int[] nums1  = new int[]{1,2,3,1,2,3};
//        boolean result = code.containsNearbyDuplicate(nums1,2);
//        int[] nums1  = new int[]{1,2,1};
//        boolean result = code.containsNearbyDuplicate(nums1,0);
//          int[] nums1  = new int[]{-1,-1};
//        boolean result = code.containsNearbyDuplicate(nums1,1);
//          int[] nums1  = new int[]{2147483647,-2147483648,2147483647,-2147483648};
        boolean result = code.containsNearbyDuplicate(nums1,2);
        boolean result2 = code.containsNearbyDuplicate2(nums1,2);



//        System.out.println(result+"  ");
        System.out.println(result+"  ");
        System.out.println(result2+"  ");



    }


    // 主要注意的点就是  用set set里可能会有重复的key
    // 如果有重复的 那么 也没关系， 移除的时候 重复的一起移除 没关系。
    // 核心 set里面只保留前K个元素 ，这样时间复杂度就是O（n）了
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        // if(nums.length<k) return false;
        HashSet<Integer> set  = new HashSet<>();

        for(int i = 0 ; i<nums.length;i++){
            boolean result =set.contains(nums[i]);
            if(result ){
                return true;
            }
            set.add(nums[i]);

            if(set.size()>k){
                set.remove(nums[i-k+1]);
            }




        }

        return false;

    }


    public boolean containsNearbyDuplicate2(int[] nums, int k) {
        if(nums == null ||nums.length==0)  return false;
        HashMap<Integer,Integer> map = new HashMap<>();


        for(int i =0 ;i<nums.length;i++){
            int tmp = nums[i];
            if(map.keySet().contains(tmp)){
                int tmpI = map.get(tmp);
                if(i-tmpI<=k) return true;
            }
            map.put(tmp, i);
        }
        return false;
    }

}

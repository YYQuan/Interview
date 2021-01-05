package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_136 {

    public static void main(String[] args) {
        LeetCode_136 code = new LeetCode_136();
        int[] ints  = new int[]{2,2,1};
        int result = code.singleNumber(ints);
        System.out.println(result);

    }

    public int singleNumber(int[] nums) {
        HashSet<Integer> sets = new HashSet<>();

        for(int i = 0; i<nums.length;i++){
            if(sets.contains(nums[i])){
                sets.remove(nums[i]);
            }else{
                sets.add(nums[i]);
            }
        }

//        sets.iterator()
        return sets.iterator().next();
    }


}

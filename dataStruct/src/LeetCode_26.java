package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_26 {

    public static void main(String[] args) {
        LeetCode_26 code = new LeetCode_26();
        int[] ints  = new int[]{0,0,1,1,1,2,2,3,3,4};
//        int result = code.removeDuplicates(ints);
        int result = code.bestRemoveDuplicates(ints);
        System.out.println(result+"  ");
        for(int i =0 ;i<ints.length;i++){
            System.out.print(ints[i]+"  ");

        }

    }


    // 我的方案   时间排名 百分之9
    public int removeDuplicates(int[] nums) {
        if(nums == null ||nums.length<0) return  0;

        HashSet<Integer> set = new HashSet<>();
        for(int i = 0;i<nums.length;i++){
            if(!set.contains(nums[i])){
                nums[set.size()] = nums[i];
                set.add(nums[i]);
            }
        }

        return set.size();
    }

    // 注意题目  排序的
    public int bestRemoveDuplicates(int[] nums) {
        if(nums == null ||nums.length<2) return  0;
        int k = 0 ;
        for(int i = 1;i<nums.length;i++){
            if(nums[k] != nums[i]){
                nums[++k] = nums[i];
            }
        }

        return k+1;
    }

    public int removeDuplicates2(int[] nums) {

        if(nums == null ||nums.length<1) return 0;

        int k = 0 ;
        for(  int i  =1 ; i<nums.length ;i++){
            if( nums[k] != nums[i]){
                nums[++k] = nums[i];
            }
        }

        return k+1;

    }


}

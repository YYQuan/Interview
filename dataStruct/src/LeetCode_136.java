package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_136 {

    public static void main(String[] args) {
        LeetCode_136 code = new LeetCode_136();
//        int[] ints  = new int[]{2,2,1};
        int[] ints  = new int[]{4,1,2,1,2};
        int result = code.singleNumber(ints);
        int result2 = code.singleNumber3(ints);
        System.out.println(result);
        System.out.println(result2);

    }

    // 题目限制了 元素都只出现一次 或者两次
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


    public int singleNumber2(int[] nums) {


        HashSet<Integer> ints = new HashSet<>();

        for(int i :nums){


            if(ints.contains(i)){
                ints.remove((Object)i);
            }else{
                ints.add(i);
            }

        }

        return ints.iterator().next();

    }

    public int singleNumber3(int[] nums) {

        if(nums == null ||nums.length==0) throw new IllegalArgumentException();
        if(nums.length==1) return nums[0];

        Arrays.sort(nums);


        for(int i  = 0; i<nums.length;){

            if(i<nums.length-1){
                if(nums[i]!=nums[i+1]){
                    return nums[i];
                }else{
                    int tmp = nums[i];
                    while(i<nums.length &&nums[i]==tmp){
                        i++;
                    }
                }

            }else{
                return nums[nums.length-1];
            }

        }

        throw new IllegalArgumentException();

    }
}

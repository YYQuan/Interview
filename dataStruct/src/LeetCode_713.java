package src;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_713 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_713 code = new LeetCode_713();
//        int[] nums = new int[]{10,5,2,6};
//        int k = 100;
//        int[] nums = new int[]{10,9,10,4,3,8,3,3,6,2,10,10,9,3};
//        int k = 19;
        int[] nums = new int[]{1,2,3};
        int k = 0;
//        int[] nums = new int[]{100,100,100,100,3};
//        int k = 10;
        int result1 = code.numSubarrayProductLessThanK(nums,k);
        System.out.println(result1);
    }


    // 题目说了 是正整数 数组
    public int numSubarrayProductLessThanK(int[] nums, int k) {

        if(nums.length<=0) return 0;

        //k == 0 和 1 的时候有点难处理 会影响乘积 由于题目说了 是正整数数组
        if(k<=1) return 0;

        int l = 0; // 左边界
        int r = 0; // 右边界
        int product = 1; // [l,r]当前的乘积

        int result =0;

        // 从左往右 遍历 nums数组
        for(; r<nums.length;r++){

            //得到 [l,r] 的累积
            product*=nums[r];

            // 如果当前 r不满足条件则 移动 l 使得[l,r] 的累积能够满足条件，
            // 如果 nums[r] >k的话 那么l是可能等于r+1的
            while(product>=k){
                product/=nums[l];
                l++;
            }

            // 只包含 r 这个index元素的 满足条件的个数
            // l 是 由可能 大于r的  也就是当前nums[r]这个元素就 >k了 就为有这种情况
            result += ((r-l)+1);

        }


        return result;

    }


}

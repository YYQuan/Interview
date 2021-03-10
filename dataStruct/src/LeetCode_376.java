package src;

import java.util.Arrays;
import java.util.HashMap;

public class LeetCode_376 {

    public static void main(String[] args) {
//        int[] ints =new int[] {10,9,2,5,3,7,101,18};
//        int[] ints =new int[] {10,11,2};
//        System.out.println(new LeetCode_300().solution2(
//                ints));
//        int nums1[] = {1,7,4,9,2,5};
        int nums1[] = {1,7,4,9,2,5,5,5,5,5,5,5,5,6,4,5};
//        int nums1[] = {1,7,4};
        System.out.println((new LeetCode_376().wiggleMaxLength(nums1)));
    }



    // 状态转移方程：
    //             up[i-1]      // nums[i]<= nums[i-1]
    // up[i] = {
    //             max(up[i-1],down[i-1]+1) // nums[i]>nums[i-1]
    //              down[i-1]      // nums[i]>= nums[i-1]
    // down[i] = {
    //               max(down[i-1],up[i-1]+1) // nums[i]<nums[i-1]

    // 疑问  既然 i-1 不一定在 结果当中  为啥 只判断 i 和 i-1 即可？ i-1 又不一定在结果当中
    // (i, i-1)  只有三种情况 ： nums[i] == nums[i-1]   大于 小于
    // 如果等于 那就没变化
    // 如果小于 那么就应该到 down 队列去
    // 如果大于 那么就应该到 up 队列去
    // 所以  只需要判断  nums[i] 和  nums[i-1]的关系  所以nums[i-1]相同的值 是一定在结果当中的，要不在up里 要么在down中
    public int wiggleMaxLength(int[] nums) {
        if(nums.length==0) return 0 ;
        int[]  up = new int[nums.length];  // 最后一步是升
        int[]  down = new int[nums.length];// 最后一步是降
        up[0] = 1;
        down[0] = 1;
        for(int i = 1 ; i< nums.length;i++){
            if(nums[i]>nums[i-1]){
                up[i] = Math.max(up[i-1],down[i-1]+1);
            }else{
                up[i] = up[i-1];
            }
            if(nums[i]<nums[i-1]){
                down[i] = Math.max(down[i-1],up[i-1]+1);
            }else{
                down[i] = down[i-1];
            }
        }
        return  Math.max(down[nums.length-1],up[nums.length-1]);
    }

}

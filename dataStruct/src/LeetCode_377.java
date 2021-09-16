package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_377 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_377 code = new LeetCode_377();
        int[] ints  = new int[]{1,2,3};
//        int[] ints  = new int[]{2};
//        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 5,  1,5};
//        int[] ints = {14,9,8,4,3,2};
//        int[] ints = {1};
//        int[] ints = {3};
//        System.out.println(code.coinChange(ints,11));
//        System.out.println(code.coinChange(ints,6249));
        System.out.println(code.combinationSum4(ints,4));
        System.out.println(code.combinationSum4D(ints,4));
        System.out.println(code.combinationSum4_2(ints,4));

    }


    // 怎么 处理 去重 和 顺序差异？
    // dp[j] =累加 dp[j-nums[i]]   nums[i] i：[0,nums.length)
    // 这个可以好好理解一下，   这个状态转移方程 并不是  i+1 就表 多一个硬币
    // 除了 dp[0]
    // 而是dp[i]就是  题目的解
    //
    // 为啥是累加 呢  是为了保留顺序差异
    public int combinationSum4D(int[] nums, int target) {

        if(nums.length==0) return 0;
        int[] dp = new int[target+1];

        // dp[0] =1  是为了而处理  i==nums[i] 的情况 就是等于1
        dp[0] = 1;

        for(int i =  0;i<target+1;i++){

            for(int v :nums) {
                if(i-v>=0) {
                    dp[i] += dp[i - v];
                }
            }

        }
        return dp[target];
    }


        // 常规递归 超时
    public int combinationSum4(int[] nums, int target) {
        if(target == 0) return 1;
        if(target<0 ) return 0;
        int result = 0 ;
        for(int v :nums) {
            result +=combinationSum4(nums, target-v);
        }

        return result;
    }


    public int combinationSum4_2(int[] nums, int target) {

        if(nums == null ||nums.length==0) return   0 ;
        int[] dp = new  int[target+1];
        dp[0] =0;//
        for(int i =  1 ; i <=target;i++){


            int sum  = 0  ;
            for(int j = 0 ; j<nums.length;j++){
                if(nums[j]<i){
                    sum +=dp[i-nums[j]];
                }else if(nums[j]==i){
                    sum +=1;
                }
            }
            dp[i] = sum;

        }
        return dp[target];

    }

}

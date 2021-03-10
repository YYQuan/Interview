package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LeetCode_494 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_494 code = new LeetCode_494();
//        int[] ints  = new int[]{1,2,5};
//        int[] ints  = new int[]{2};
//        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 5,  1,5};
//        int[] ints = {1, 1, 1, 1, 1};
        int[] ints = {0,0,0,0,0,0,0,0,1};
//        int[] ints = {0,0,1};
//        int[] ints = {1};



        System.out.println(code.findTargetSumWays(ints,1));
        System.out.println(code.findTargetSumWaysD(ints,1));

    }


    //击败45
    public int findTargetSumWaysD(int[] nums, int S) {
        if(nums.length == 0 ) return 0 ;
        int sum = 0;
        for(int value : nums){
            sum+=value;
        }

        if(S>sum||S<-sum) return 0;
        // [-sum,sum] 的范围
        int[] dp = new int[2*sum+1];

        if(nums[0]!=0) {
            dp[nums[0] + sum] = 1;
            dp[-nums[0] + sum] = 1;
        }else{
            dp[sum] = 2;
        }

        for(int i = 1 ; i<nums.length;i++){

            // 注意 本次的结果 不应该影响到本次后序的计算
            int value = nums[i];
            HashMap<Integer,Integer> map  = new HashMap<>();
            for(int j = value ; j<dp.length-value;j++){
                if(dp[j]!=0){
                    map.put(j-value,map.getOrDefault(j-value,0)+dp[j]);
                    map.put(j+value,map.getOrDefault(j+value,0)+dp[j]);
                    dp[j]=0;
                }
            }

            for(int v :map.keySet()){
                dp[v] = map.get(v);
            }

        }

        return dp[S+sum];
    }


        //常规递归  也能击败 23
    public int findTargetSumWays(int[] nums, int S) {
        if( nums.length == 0 ) return  0;
        return findTargetSumWays(nums,0,S);
    }
    public int findTargetSumWays(int[] nums,int index, int S) {

        if(index == nums.length){
            return S==0?1:0;
        }
        return findTargetSumWays(nums,index+1,S-nums[index])+findTargetSumWays(nums,index+1,S+nums[index]);

    }

}

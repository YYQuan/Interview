package src;

import java.util.Arrays;

public class LeetCode_198 {



    public static void main(String[] args) {
        LeetCode_198 code = new LeetCode_198();

//        System.out.println(code.uniquePaths(2,2));
//        int[] ints = new int[]{1,2,3,1};
        int[] ints = new int[]{2,7,9,3,1};
        System.out.println(code.rob(ints));
        System.out.println(code.robD(ints));
        System.out.println(code.rob2(ints));

    }


    // 击败 100
    public int robD(int[] nums) {

        if(nums.length==0) return 0;
        if(nums.length==1) return nums[0];
        if(nums.length==2) return Math.max(nums[0],nums[1]);
        int[] tmp = new int[nums.length];

        tmp[1] = Math.max(nums[0],nums[1]);
        tmp[0] = nums[0];

        for(int i = 2 ; i<nums.length;i++){

            tmp[i] = Math.max(tmp[i-1],tmp[i-2]+nums[i]);
        }
        return tmp[nums.length-1];
    }


        // f(n) = Math.max( nums[i]+f(n-2),f(i+1)))  i :{0,n-1}
    public int rob(int[] nums) {
        return rob(nums,nums.length-1);

    }
    public int rob(int[] nums,int index) {
        if(index <0) return  0 ;
        if(index == 1 ) return Math.max(nums[1],nums[0]);
        if(index == 0 ) return nums[0];

        return Math.max(nums[index] +rob(nums,index-2),rob(nums,index-1));
    }



    public int rob2(int[] nums) {

        if(nums == null || nums.length==0) return 0;
        if(nums.length==1) return nums[0];
        int[] dp = new int[nums.length];
        Arrays.fill(dp,0);



        dp[0] = nums[0];
        dp[1] = Math.max(nums[0],nums[1]);

        for(int i =2 ;i<nums.length;i++){

            dp[i] = Math.max(dp[i-1],dp[i-2]+nums[i]);

        }

        return dp[dp.length-1];


    }
}
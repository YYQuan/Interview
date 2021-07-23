package src;

public class LeetCode_213 {

    public static void main(String[] args) {
        LeetCode_213 code = new LeetCode_213();

//        System.out.println(code.uniquePaths(2,2));
//        int[] ints = new int[]{1,2,3,1};
//        int[] ints = new int[]{2,7,9,3,1};
//        int[] ints = new int[]{2,3,2};
//        int[] ints = new int[]{1,2,3,1};
        int[] ints = new int[]{2,3,3,1};
        System.out.println(code.robD(ints));
        System.out.println(code.rob(ints));

    }

    // 击败  100
    // 用边界来区分
    public int robD(int[] nums) {

        if(nums.length==0) return 0;

        if(nums.length==1) return nums[0];

        int[] tmp = new int[nums.length];
        int[] tmp2 = new int[nums.length];



        tmp[0] = nums[0];
        tmp[1] = Math.max(nums[0],nums[1]);


        tmp2[0] = 0;
        tmp2[1] = nums[1];

        for(int i = 2 ; i<nums.length-1;i++){
            tmp[i] = Math.max(tmp[i-1],tmp[i-2]+nums[i]);
        }
        tmp[nums.length-1] = tmp[nums.length-2];

        for(int i = 2 ; i<nums.length;i++){
            tmp2[i] = Math.max(tmp2[i-1],tmp2[i-2]+nums[i]);
        }


        return Math.max(tmp[nums.length-1],tmp2[nums.length-1]);
    }


    public int rob(int[] nums) {
        if(nums == null || nums.length ==0 ) return 0 ;
        if(nums.length==1)  return nums[0];

        int[] dp = new int [nums.length];
        int[] dp1 = new int [nums.length];

        dp[0] = nums[0];
        dp[1] = Math.max(nums[0],nums[1]);

        dp1[0] = 0;
        dp1[1] = Math.max(0,nums[1]);

        // 分两种情况 选 第一个房子（不能选 最后一个）
        // 不选 第一个房子（能选 最后一个）


        for(int i= 2 ;i<nums.length-1;i++){
            dp[i] = Math.max(dp[i-1],dp[i-2]+nums[i]);
        }

        for(int i= 2 ;i<nums.length;i++){
            dp1[i] = Math.max(dp1[i-1],dp1[i-2]+nums[i]);
        }

        return Math.max(dp[nums.length-2],dp1[nums.length-1]);
    }
}
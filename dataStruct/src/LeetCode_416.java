package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LeetCode_416 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_416 code = new LeetCode_416();
//        int[] ints  = new int[]{1,2,3};
        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 5,  1,5};
//        int[] ints = {14,9,8,4,3,2};
//        int[] ints = {1, 2, 3, 5};
        System.out.println(code.solution(ints));
        System.out.println(code.solutionDync(ints));
        System.out.println(code.canPartition(ints));
        System.out.println(code.canPartitionD(ints));

    }



    // 击败 20
    // 动态规划 要怎么弄呢？
    // 状态方程是怎么样的？
    // 构建新的状态表
    // dp[nums][sum/2]
    // dp[i][j] 表示  在nums[0,i]中是否包含 任意集合的元素和 等于 j
    // f(i,j) =  f(i-1,j)|f(i-1,j-nums[i])
    public boolean canPartitionD(int[] nums){
        if(nums.length<=1) return false;
        int sum = 0;
        for(int v:nums){
            sum +=v;
        }

        if(sum%2 ==1)  return false;

        int target = sum/2;
        boolean[][] dp  = new boolean[nums.length][target+1];


        for(int i = 0 ; i<target+1;i++){
            dp[0][i] =  (i ==nums[0]);
        }

        for(int i = 1 ; i<nums.length;i++){
            for(int j = 0 ; j<target+1;j++){
                dp[i][j] =  dp[i-1][j]|(j >= nums[i] && dp[i - 1][j - nums[i]]);
            }
        }

        return dp[nums.length-1][target];

    }


    // 常规递归  超时
    public boolean canPartition(int[] nums){
        int sum = 0 ;
        for(int value : nums){
            sum+=value;
        }

        if(sum%2 ==1) return false;

        int target = sum/2;

        return canPartition(nums,0,target);
    }

    public boolean canPartition(int[] nums,int index,int target){
        if(target<0) return false;
        if(index == nums.length) return false;
        if(nums[index] == target) return true;

        return canPartition(nums,index+1,target-nums[index])||canPartition(nums,index+1,target);

    }



////// 记忆化搜索

    int[][] tmpResult ;
    public boolean  solution(int[] arr){


        if(arr == null ||arr.length<=0)  return false;

        int sum =  0;

        for(int i = 0 ; i< arr.length ;i++){
            assert arr[i] >0 ;
            sum += arr[i];
        }

        if(sum%2 != 0 )  return false;

        tmpResult  = new  int[arr.length][sum/2+1];
        for(int i = 0 ; i<tmpResult.length; i++) {
            Arrays.fill(tmpResult[i], -1);
        }

        return solutionActual(arr,arr.length-1,sum/2);

    }

    //arr [0,index] 范围内 能不能 装满sum
    public  boolean  solutionActual(int[] arr,int  index ,int sum){
        if( index <=0 )  return false;
        if( sum == 0) return true;
        if(tmpResult[index][sum] > 0 ) return tmpResult[index][sum] != 0;

        tmpResult[index][sum] = solutionActual(arr,index-1,sum)||solutionActual(arr,index-1,sum-arr[index])? 1:0;

        return tmpResult[index][sum] != 0;
    }


    //////////////////////////////////
    // 动态规划

    public  boolean  solutionDync(int[] arr){

        if(arr == null ||arr.length<=0)  return false;

        int sum =  0;

        for(int i = 0 ; i< arr.length ;i++){
            assert arr[i] >0 ;
            sum += arr[i];
        }

        if(sum%2 != 0 )  return false;

        tmpResult  = new  int[arr.length][sum/2+1];
        for(int i = 0 ; i<tmpResult.length; i++) {
            Arrays.fill(tmpResult[i], -1);
        }

        boolean[] tmpResult = new boolean[sum/2+1];

        int totalSum = sum/2;
        if(arr[0]<=totalSum) {
            tmpResult[totalSum] = true;
        }


        for(int i = 0 ; i< arr.length ; i++){
            if(arr[i]<=totalSum) {
                tmpResult[i] = true;
            }
            for(int j = 0 ; j <totalSum+1; j++){
                if(tmpResult[j]){
                    int tmp = arr[i]+ j;
                    if(tmp <= totalSum) {
                        tmpResult[tmp] = true;
                    }
                }
            }
        }
        return  tmpResult[totalSum];
    }




}

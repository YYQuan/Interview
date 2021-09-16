package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_474 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_474 code = new LeetCode_474();
//        int[] ints  = new int[]{1,2,5};
//        int[] ints  = new int[]{2};
//        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 5,  1,5};
//        int[] ints = {14,9,8,4,3,2};
//        int[] ints = {1};
        int[] ints = {3};
        String[] strs = new String[]{"10", "0001", "111001", "1", "0"};
//        System.out.println(code.coinChange(ints,11));
//        System.out.println(code.coinChange(ints,6249));
        System.out.println(code.findMaxForm(strs,5,3));
        System.out.println(code.findMaxFormD(strs,5,3));
        System.out.println(code.findMaxForm2(strs,5,3));

    }


    // 击败 65
    // 状态方程的 核心就是 要不要 加上着一个 str
    // 状态方程  dp(i,m,n) = Math(dp(i-1,m,n),dp(i,m-i(m),n-i(n))),
    // 核心：有两点 1. 构建dp[m][n]  strs 的维度 用刷新次数来处理
    // 2： 每次刷新dp的时候， 从外往里刷， 避免前后的结构影响到后面的计算
    public int findMaxFormD(String[] strs, int m, int n) {
        if(strs.length==0)  return   0;
        int[][]  dp  = new int[m+1][n+1];
        for(String str:strs){
            int[] countInt = calculatorCount(str);
            for(int i = m;i>=countInt[0];i--){
                for(int j = n ;j>=countInt[1];j--){
                    // 使用 这个str 的情况 和不使用这个str 取最大值
                    dp[i][j] = Math.max(dp[i][j],dp[i-countInt[0]][j-countInt[1]]+1);
                }
            }
        }
        return  dp[m][n];
    }

    int[] calculatorCount(String str){
        int[] result  = new int[2];
        for(char c:str.toCharArray()){
            result[c-'0']++;
        }
        return result;
    }


    // 常规递归 超时
    // dp[m][n]  刷新 strs.length 次
    // 从外 往里刷新   m -> 0  n - >0  否者会影响计算
    // dp[m][n]  = Math.max(dp[m][n], dp[m -str[i]0][n-str[i][1]])
    // 最终的dp[m][n]
    public int findMaxForm(String[] strs, int m, int n) {
        if(strs.length==0)  return   0;
        return findMaxForm(strs,0,0,m,n);
    }

    public int findMaxForm(String[] strs, int index ,int count ,int m ,int n) {
        if(index >= strs.length)  return count ;
        int count0 = 0;
        int count1 = 0;
        for(char c:strs[index].toCharArray()){
            if(c == '0'){
                count0++;
            }else if(c == '1'){
                count1++;
            }
        }
        if( count0 > m||count1>n){
            return findMaxForm(strs,index+1,count,m,n);
        }
        return Math.max(findMaxForm(strs,index+1,count,m,n),findMaxForm(strs,index+1,count+1,m-count0,n-count1));

    }

    // 题目  m 和 n 约束的 集合的元素的全部的 0,1的数量
    public int findMaxForm2(String[] strs, int m, int n) {

        if(strs == null ||strs.length == 0 )  return 0 ;

        int[][]  dp = new int[m+1][n+1];

        for(String s : strs){


            int[] count = calculatorCount(s);
            for(int i = m ;i>=count[0];i--){
                for(int j = n ;j>=count[1];j--){

                    dp[i][j] = Math.max(dp[i][j],dp[i-count[0]][j-count[1]]+1);

                }
            }

        }
        return dp[m][n];
    }

}

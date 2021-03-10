package src;

import java.util.Arrays;

public class LeetCode_322 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_322 code = new LeetCode_322();
//        int[] ints  = new int[]{1,2,5};
//        int[] ints  = new int[]{2};
//        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 5,  1,5};
//        int[] ints = {14,9,8,4,3,2};
//        int[] ints = {1};
//        int[] ints = {1, 2, 3, 5};
        int[] ints = {186,419,83,408};
//        System.out.println(code.coinChange(ints,11));
//        System.out.println(code.coinChange(ints,6249));
        System.out.println(code.coinChangeD(ints,6249));

    }



    //  动态规划:击败 5
    //  状态表
    //  dp[coins.length][0-amount]
    //  f(i,j) = f(i-1,j) == -1 :  n +f(i-1,j%(j)/coins[i)+n*coins[i])      n [0,j/coins[i]]
    //  f(i,j) = f(i-1,j) != -1 :   (n +f(i-1,j%(j)/coins[i)+n*coins[i]) == -1?)      n [0,j/coins[i]]
    //  有个坑  尽量多拿大面值   最终结果也不一定小， 也就是贪心策略在这是不行的
    public int coinChangeD(int[] coins, int amount) {
        Arrays.sort(coins);
        if(coins.length<=0) return -1;
        int[][]  dp = new  int[coins.length][amount+1];

        for(int[] ints :dp){
            Arrays.fill(ints,-1);
        }
        for(int i = 0 ; i<amount+1;i++){
            dp[0][i] = (i%coins[0]==0)?(i/coins[0]):-1;
        }

        for(int i = 1 ;i<coins.length;i++){
            if(coins[i] == 0) continue;
            for(int j = 0 ;  j<amount+1;j++){
                if( j % coins[i]==0) {
                    dp[i][j] = j/coins[i];
                }else{
                    int min =  Integer.MAX_VALUE;
                    for(int q =0 ;q<= j/coins[i];q++){
                        int tmp = j -  (coins[i]*q);
                        if(dp[i-1][tmp] >-1){
                            min =  Math.min(min,q+dp[i-1][tmp]);
                            dp[i][j] = min;
                        }
                    }

                }
            }
        }


        return  dp[coins.length-1][amount];
    }


    //  常规递归  超时
    public int coinChange(int[] coins, int amount) {
        if(coins.length<=0) return -1;
        int result = coinChange(coins,amount,0);
        return  result<Integer.MAX_VALUE ?result :-1;
    }
    public int coinChange(int[] coins, int amount,int count) {

        if(amount == 0 ) return count;
        if(amount < 0 ) return Integer.MAX_VALUE;
        int min = Integer.MAX_VALUE ;
        for(int value :coins) {
            min  = Math.min(coinChange(coins, amount -value, count+1),min);
        }
        return min;

    }


}

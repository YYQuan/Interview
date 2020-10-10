import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knapsack {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Knapsack code = new Knapsack();
        int[] ints  = new int[]{1,2,3,5,4,3,1,32,5,3,5,54,1,2,3,32,1,12,3,3};
//        int[] ints  = new int[]{1};
        System.out.println(code.knapsack01(ints,ints,20));
        System.out.println(code.knapsack01_1(ints,ints,20));
        System.out.println(code.knapsack01Dync(ints,ints,20));

    }


    int[][]  tmpRes ; //   一个 [0,index]  + c 就代表这一种情况， tmpRes 用于保存，完成记忆化搜索。

    public int knapsack01_1(int[] w, int[] v, int c){
        tmpRes = new int[w.length][c+1] ;
        for(int i = 0 ; i< w.length ; i++) {
            Arrays.fill(tmpRes[i],-1);
        }
        return maxValue(w,v,v.length-1,c);
    }


    /**
     *
     *
     * @param w
     * @param v
     * @param index  [0,index] 的数据范围
     * @param c  当前容量
     * @return
     */
    public int maxValue(int[] w ,int[] v , int index , int c ){

        if(w == null || v == null || c<=0) return 0;

        if( index < 0  )  return 0 ;

        if(tmpRes[index][c] > 0 ) return tmpRes[index][c];

        int res = 0 ;

        // 不放 index 这个物品
        res = maxValue(w,v,index-1,c);

        if(c>= w[index]){
            res = Math.max(res,v[index]+maxValue(w,v,index-1,c-w[index]));
        }
        tmpRes[index][c] = res;
        return  res;

    }


    ////////////////////////////////////////////////
    // 大佬的记忆化搜索
    private int[][] memo;

    public int knapsack01(int[] w, int[] v, int C){

        if(w == null || v == null || w.length != v.length)
            throw new IllegalArgumentException("Invalid w or v");

        if(C < 0)
            throw new IllegalArgumentException("C must be greater or equal to zero.");

        int n = w.length;
        if(n == 0 || C == 0)
            return 0;

        memo = new int[n][C + 1];
        for(int i = 0; i < n; i ++)
            for(int j = 0; j <= C; j ++)
                memo[i][j] = -1;
        return bestValue(w, v, n - 1, C);
    }

    // 用 [0...index]的物品,填充容积为c的背包的最大价值
    private int bestValue(int[] w, int[] v, int index, int c){

        if(c <= 0 || index < 0)
            return 0;

        if(memo[index][c] != -1)
            return memo[index][c];

        int res = bestValue(w, v, index-1, c);
        if(c >= w[index])
            res = Math.max(res, v[index] + bestValue(w, v, index - 1, c - w[index]));

        return memo[index][c] = res;
    }

    ///////////////////////////////////////////////////////////////
    // 动态规划

    public int knapsack01Dync(int[] w, int[] v, int C){

        if(w == null || v == null || w.length != v.length)
            throw new IllegalArgumentException("Invalid w or v");

        if(C < 0)
            throw new IllegalArgumentException("C must be greater or equal to zero.");

        int n = w.length;
        if(n == 0 || C == 0)
            return 0;

        int[][] memo = new int[n][C + 1]; //   行坐标是 物品 ，  列坐标是 c 容量

        for(int j = 0 ; j <= C ; j ++)
            memo[0][j] = (j >= w[0] ? v[0] : 0 );

        for(int i = 1 ; i < n ; i ++)
            for(int j = 0 ; j <= C ; j ++){
                memo[i][j] = memo[i-1][j];
                // 放的下当前 这个 物品
                if(j >= w[i])
                    //memo[i - 1][j - w[i]]  放下 当前 这物品后， 剩余的容量的最大值
                    memo[i][j] = Math.max(memo[i][j], v[i] + memo[i - 1][j - w[i]]);
            }

        return memo[n - 1][C];
    }

}

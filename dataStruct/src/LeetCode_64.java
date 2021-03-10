package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_64 {

    public static void main(String[] args) {

        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>(){
            {

            }

        };
        list.add(Arrays.asList( new Integer[]{2}));
        list.add(Arrays.asList( new Integer[]{3,4}));
        list.add(Arrays.asList( new Integer[]{6,5,7}));
        list.add(Arrays.asList( new Integer[]{4,1,8,3}));

        int[][] ints = new int[2][3];
        ints[0] = new int[]{1,2,3};
        ints[1] = new int[]{4,5,6};
        System.out.println(new LeetCode_64().minPathSum(ints));
//        System.out.println(new LeetCode_120().minimumTotal2(list));

    }


    //动态规划 / 记忆化搜索  这里没感觉出 记忆化搜索和动态规划的区别
    // 击败 97
    public int minPathSum(int[][] grid) {
        int[][] tmp = new int[grid.length][grid[0].length];
        for(int i = 0 ;i<grid.length;i++) {
            Arrays.fill(tmp[i],-1);
        }


        for(int i = 0 ; i<grid.length;i++){
            for(int j = 0 ; j<grid[0].length;j++){
                int value = calculator(grid,tmp,i,j);
                tmp[i][j] = value;
            }
        }

        return tmp[grid.length-1][grid[0].length-1];
    }


    int calculator(int[][] grid,int[][] tmp,int i ,int j){


        if(i ==0){
            int sum = 0;
            for(int p = 0 ;p<=j;p++){
                sum+= grid[0][p];
            }
            return  sum;
        }


        if(j == 0 ){
            int sum = 0;
            return grid[i][0] +tmp[i-1][0];
        }

        return Math.min(tmp[i-1][j]+grid[i][j],tmp[i][j-1]+grid[i][j]);

    }

        // 常规递归  超时
    // f(i,j） = Math.min(f(i-1,j)+int[i][j],f(i,j-1)+int[i][j])
    public int minPathSum2(int[][] grid) {
        return minPathSum2(grid,grid.length-1,grid[0].length-1);

    }
    public int minPathSum2(int[][] grid,int i , int j) {
        if(i == 0 && j==0) return  grid[i][j];
        int v1 = Integer.MAX_VALUE;
        int v2 = Integer.MAX_VALUE;
        if(i>0){
            v1 = minPathSum2(grid,i-1,j)+grid[i][j];
        }
        if(j>0){
            v2 = minPathSum2(grid,i,j-1)+grid[i][j];
        }
        return Math.min(v1,v2);
    }
}

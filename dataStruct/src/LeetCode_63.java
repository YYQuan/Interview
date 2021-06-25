package src;

import java.util.Arrays;

public class LeetCode_63 {

    public static void main(String[] args) {
        LeetCode_63 code = new LeetCode_63();

//        System.out.println(code.uniquePaths(2,2));
        int[][] ints = new int[3][3];
        ints[0]=new int[]{0,0,0};
        ints[1]=new int[]{0,1,0};
        ints[2]=new int[]{0,0,0};

//        int[][] ints = new int[4][3];
//        ints[0]=new int[]{0,0,0};
//        ints[1]=new int[]{0,1,0};
//        ints[2]=new int[]{0,0,0};
//        ints[3]=new int[]{0,0,0};

//        ints = new int[4][5];
//        ints[0]=new int[]{0,0,0,0,0};
//        ints[1]=new int[]{0,0,0,0,1};
//        ints[2]=new int[]{0,0,0,1,0};
//        ints[3]=new int[]{0,0,0,0,0};
//
//        ints[0]=new int[]{0,1,0,0,0};
//        ints[1]=new int[]{1,0,0,0,0};
//        ints[2]=new int[]{0,0,0,0,0};
//        ints[3]=new int[]{0,0,0,0,0};
        System.out.println(code.uniquePathsWithObstacles(ints));
        System.out.println(code.uniquePathsWithObstacles2(ints));
        System.out.println(code.uniquePathsWithObstaclesD(ints));
//        System.out.println(code.uniquePathsWithObstacles(3,7));

    }

    // 动态规划
    // 击败 18
    // 重点  第0 行的初始值 ，只要左边出现了 阻塞 那么右边的是要被全部阻塞住的
    public int uniquePathsWithObstaclesD(int[][] obstacleGrid) {
        if(obstacleGrid[0][0]==1||obstacleGrid[obstacleGrid.length-1][obstacleGrid[0].length-1]==1) return 0;
        int[][] tmp  = new int[obstacleGrid.length][obstacleGrid[0].length];
        //只要左边出现了 阻塞 那么右边的是要被全部阻塞住的
        boolean isLegal = true;
        for(int i = 0 ;i<tmp[0].length;i++){
            if(isLegal&&obstacleGrid[0][i] ==1) isLegal = false;
            tmp[0][i]  = isLegal?1:0;
        }
        for(int i = 1 ; i<obstacleGrid.length;i++){
            for(int j = 0 ; j<obstacleGrid[0].length;j++){
                tmp[i][j] = (isLegalD(obstacleGrid,i-1,j)?tmp[i-1][j]:0)+(isLegalD(obstacleGrid,i,j-1)?tmp[i][j-1]:0);
            }
        }
        return tmp[obstacleGrid.length-1][obstacleGrid[0].length-1];
    }


    public  boolean isLegalD(int[][] obstacleGrid ,int m ,int n){
        if(m<0||m>=obstacleGrid.length) return false;
        if(n<0||n>=obstacleGrid[0].length) return false;

        if(obstacleGrid[m][n]==1) return false;
        return true;
    }

    //f(i,j) = f(i-1,j)+f(i,j-1)
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if(obstacleGrid[0][0]==1||obstacleGrid[obstacleGrid.length-1][obstacleGrid[0].length-1]==1) return 0;
        return uniquePathsWithObstacles(obstacleGrid.length-1,obstacleGrid[0].length-1,obstacleGrid);
    }

    public int uniquePathsWithObstacles(int m, int n,int[][] obstacleGrid) {
        if( m ==0 &&n ==0) return obstacleGrid[0][0]==1 ?0:1;
        return  (isLegal(obstacleGrid,m-1,n)?uniquePathsWithObstacles(m-1,n,obstacleGrid):0)+
                (isLegal(obstacleGrid,m,n-1)?uniquePathsWithObstacles(m,n-1,obstacleGrid):0);
    }

    public  boolean isLegal(int[][] obstacleGrid ,int m ,int n){
        if(m<0||m>=obstacleGrid.length) return false;
        if(n<0||n>=obstacleGrid[0].length) return false;

        if(obstacleGrid[m][n]==1) return false;
        return true;
    }


    public int uniquePathsWithObstacles2(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        if(m ==0 && n == 0) return 1;


        int[][] tmpIntss = new  int[m][n];

        for(int i = 0 ; i<n;i++){

            if(obstacleGrid[0][i]==1){
                while(i<n) {
                    tmpIntss[0][i++] =0;
                }
            }else{
                tmpIntss[0][i] =1;
            }

        }

        for(int i =1 ;i<m;i++){

            for(int j = 0;j<n;j++){

                if(obstacleGrid[i][j] ==1){
                    tmpIntss[i][j] =0;
                }else {
                    if(j==0) {
                        // 重点在这 下一列的初始值要受上一列的影响
                        tmpIntss[i][j] =tmpIntss[i-1][0];
                        continue;
                    }
                    tmpIntss[i][j] = tmpIntss[i][j - 1] +tmpIntss[i - 1][j];
                }
            }
        }

        return tmpIntss[m-1][n-1];

    }
}
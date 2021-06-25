package src;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;

public class LeetCode_62 {

    public static void main(String[] args) {
        LeetCode_62 code = new LeetCode_62();

//        System.out.println(code.uniquePaths(2,2));
        System.out.println(code.uniquePaths(3,7));
        System.out.println(code.uniquePaths3(3,7));
        System.out.println(code.uniquePathsD(3,7));

    }

    // 动态规划 击败 8
    public int uniquePathsD(int m, int n) {

        if(m ==1 ) return 1;

        int[][] tmp = new int[m+1][n+1];
        Arrays.fill(tmp[1],1);
        for(int i = 2 ;i<m+1;i++){
            for(int j  = 1 ;j<n+1;j++){
                tmp[i][j] =  (i>1?tmp[i-1][j]:0)+(j>1?tmp[i][j-1]:0);
            }
        }
        return tmp[m][n];
    }

    //f(i,j) = f(i-1,j)+f(i,j-1)
    public int uniquePaths(int m, int n) {
        if( m ==1 &&n ==1) return 1;

        return  (m>1?uniquePaths(m-1,n):0)  + (n>1?uniquePaths(m,n-1):0);
    }


    public int uniquePaths2(int m, int n) {

        return uniquePaths2(0,0,m-1,n-1);
    }

    public  int uniquePaths2(int x, int y ,int m ,int n){

        if(x ==m && y ==n) return 1;

        return (x==m?0: uniquePaths2(x+1,y,m,n)) + (y==n?0:uniquePaths2(x,y+1,m,n));

    }


    public int uniquePaths3(int m, int n) {

        if(m ==0 && n == 0) return 1;

        int[][] tmpIntss = new  int[m][n];
        Arrays.fill(tmpIntss[0],1);

        for(int i =1 ;i<m;i++){

            for(int j = 0;j<n;j++){
                if(j==0) {
                    tmpIntss[i][j] =1;
                    continue;
                }
                tmpIntss[i][j] = tmpIntss[i][j-1]+tmpIntss[i-1][j];
            }
        }

        return tmpIntss[m-1][n-1];

    }
}

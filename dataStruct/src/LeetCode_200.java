import java.util.ArrayList;
import java.util.List;

public class LeetCode_200 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_200 code = new LeetCode_200();
        char[][] ints  = {

                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };



        System.out.println(code.numIslands(ints));

    }


    boolean[][]  isVisited ;
    int m ; //行数
    int n ;// 列数
    public int numIslands(char[][] grid) {

        if(grid == null || grid.length == 0 ||grid[0].length == 0) return 0;

        m  = grid.length; //行数
        n  = grid[0].length;// 列数


        isVisited = new boolean[m][n];

        int result = 0;
        for(int i = 0 ;i<m ;i++)
            for(int j = 0 ; j<n;j++)
                if(isArea(i,j)&&grid[i][j]=='1'&&!isVisited[i][j]) {
                    System.out.println("-- i "+i +"  -- j "+ j );

                    handle(grid,i,j);
                    result++;
                }

        return  result;
    }


    int[][]  d = {{-1,0},{0,+1},{1,0},{0,-1}};


    private void handle(char[][] grid,int i, int j) {

        if(!isArea(i,j)) return;

        if(isVisited[i][j]) return;

        if(grid[i][j] == '0') return;

        isVisited[i][j] = true;

        for(int p = 0 ; p<4;p++) {
            int newi = i + d[p][0];
            int newj = j + d[p][1];
            handle(grid,newi,newj);
        }
    }

    private boolean isArea(int i ,int j){
        return  i>=0 &&j>=0 &&i<m&&j<n;
    }


}

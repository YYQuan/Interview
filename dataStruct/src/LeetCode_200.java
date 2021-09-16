package src;

import java.util.Arrays;

public class LeetCode_200 {

    public static void main(String[] args) {
        LeetCode_200 code = new LeetCode_200();

        char[][] chars = new char[4][];
//        chars[0]=new char[]{'A','B','C','E'};
//        chars[1]=new char[]{'S','F','C','S'};
//        chars[2]=new char[]{'A','D','E','E'};

        chars[0]=new char[]{'1','1','1','1','0'};
        chars[1]=new char[]{'1','1','0','1','0'};
        chars[2]=new char[]{'1','1','0','0','0'};
        chars[3]=new char[]{'0','0','0','0','0'};


//        System.out.println(code.exist(chars,"ABCCED"));
//        System.out.println(code.exist(chars,"SEE"));
//        System.out.println(code.exist(chars,"ABCB"));
        System.out.println(code.numIslands(chars));
        System.out.println(code.numIslands2(chars));

    }

    //击败91
    public int numIslands(char[][] grid) {
        boolean[][] isReads = new boolean[grid.length][grid[0].length];
        return numIslands(grid,isReads);
    }
    public int numIslands(char[][] grid,boolean[][] isReads) {

        int result = 0 ;
        for(int i = 0 ; i<grid.length;i++){
            for(int j = 0 ; j<grid[0].length;j++){

                if(grid[i][j] == '1'){
                    if(isReads[i][j]) continue;
                    expandIsland(grid,isReads,i,j);
                    result++;
                }
            }
        }
        return result;
    }


    void expandIsland(char[][] grid ,boolean[][] isReads,int i , int j){
        isReads[i][j] = true;

        //左上右下
        if(j>0){
            if(grid[i][j-1] =='1'&&!isReads[i][j-1]){
                expandIsland(grid,isReads,i,j-1);
            }
        }

        if(i>0){
            if(grid[i-1][j] =='1'&&!isReads[i-1][j]){
                expandIsland(grid,isReads,i-1,j);
            }
        }

        if(j<grid[0].length-1){
            if(grid[i][j+1] =='1'&&!isReads[i][j+1]){
                expandIsland(grid,isReads,i,j+1);
            }
        }

        if(i<grid.length-1){
            if(grid[i+1][j] =='1'&&!isReads[i+1][j]){
                expandIsland(grid,isReads,i+1,j);
            }
        }

    }


    public int numIslands2(char[][] grid) {
        if(grid ==null||grid.length == 0 ) return 0;
        if(grid[0].length == 0) return 0;
        // 存是否已经读过
        boolean[][] isRead = new boolean[grid.length][grid[0].length];

        int result =0;
        for(int i  =0 ;i<grid.length;i++){
            for(int j = 0 ;j<grid[0].length;j++){

                if(grid[i][j] == '1' &&!isRead[i][j]){
                    result++;
                    expandIsland2(grid,isRead,i,j);
                }
            }
        }

        return result;
    }

    public void expandIsland2(char[][] grid,boolean[][] isRead, int row,int colunm ){
        if(row>=grid.length) return ;
        if(colunm>=grid[0].length)return ;
        // 左上右下
        isRead[row][colunm] = true;
        if(colunm>0){
            if(grid[row][colunm-1]=='1'&&!isRead[row][colunm-1]) {
                expandIsland2(grid, isRead,  row, colunm - 1);
            }
        }

        if(row>0){
            if(grid[row-1][colunm]=='1'&&!isRead[row-1][colunm]) {
                expandIsland2(grid, isRead,  row - 1, colunm);
            }
        }

        if(colunm<(isRead[0].length-1)&&!isRead[row][colunm+1]){
            if(grid[row][colunm+1]=='1') {
                expandIsland2(grid, isRead,  row, colunm + 1);
            }
        }

        if(row <(isRead.length-1)&&!isRead[row+1][colunm]){
            if(grid[row+1][colunm]=='1'){
                expandIsland2(grid, isRead,  row+1, colunm );
            }
        }

    }
}

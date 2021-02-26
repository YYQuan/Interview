package src;

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
}

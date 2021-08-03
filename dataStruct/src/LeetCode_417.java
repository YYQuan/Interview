package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_417 {

    public static void main(String[] args) {
        LeetCode_417 code = new LeetCode_417();

//        int[][] chars = new int[5][5];
//        chars[0]=new int[]{1,2,2,3,5};
//        chars[1]=new int[]{3,2,3,4,4};
//        chars[2]=new int[]{2,4,5,3,1};
//        chars[3]=new int[]{6,7,1,4,5};
//        chars[4]=new int[]{5,1,1,2,4};

        int[][] chars = new int[3][6];

        chars[0]=new int[]{3,3,3,3,3,3};
        chars[1]=new int[]{3,0,3,3,0,3};
        chars[2]=new int[]{3,3,3,3,3,3};
//        System.out.println(code.exist(chars,"ABCCED"));
//        System.out.println(code.exist(chars,"SEE"));
//        System.out.println(code.exist(chars,"ABCB"));

        System.out.println( code.pacificAtlantic(chars));
        System.out.println( code.pacificAtlantic2(chars));


    }

    // 击败 5
    //  题意： 只能往 小于 的地方移动， 要同时满足能移动到 上/左边界 和下/右边界
    public List<List<Integer>> pacificAtlantic(int[][] matrix) {


        List<List<Integer>> result = new ArrayList<>();
        if(matrix.length<=0||matrix[0].length<=0) return result;
        boolean[][] board = new boolean[matrix.length][matrix[0].length];
        for(int i = 0 ; i<matrix.length;i++){
            for(int j = 0 ;j<matrix[0].length;j++){

                pacificAtlantic(matrix,board,i,j);
                if(isLegat(board)){
                    List<Integer> list = new ArrayList<>();
                    list.add(i);
                    list.add(j);
                    result.add(list);
                }

                for(int q = 0 ; q<matrix.length;q++) {
                    Arrays.fill(board[q], false);
                }
            }
        }
        return result;
    }

    public void pacificAtlantic(int[][] matrix,boolean[][] board,int i,int j) {

        board[i][j] = true;

        int currentValue = matrix[i][j];
        //左上右下
        if(j>0){
            if(matrix[i][j-1]<=currentValue&&!board[i][j-1]){
                pacificAtlantic(matrix,board,i,j-1);
            }
        }

        if(i>0){
            if(matrix[i-1][j]<=currentValue&&!board[i-1][j]){
                pacificAtlantic(matrix,board,i-1,j);
            }
        }

        if(j<board[0].length-1){
            if(matrix[i][j+1]<=currentValue&&!board[i][j+1]){
                pacificAtlantic(matrix,board,i,j+1);
            }
        }

        if(i<board.length-1){
            if(matrix[i+1][j]<=currentValue&&!board[i+1][j]){
                pacificAtlantic(matrix,board,i+1,j);
            }
        }
    }


    boolean isLegat(boolean[][] board){

        boolean accessLeftOrTop = false;
        boolean accessRightOrBottom = false;

        for(int i = 0 ; i<board.length;i++){
            if(board[i][0])  {
                accessLeftOrTop = true;
            }

            if(board[i][board[0].length-1]){
                accessRightOrBottom  = true;
            }
        }

        for(int i = 0 ; i<board[0].length;i++){
            if(board[0][i])  {
                accessLeftOrTop = true;
            }

            if(board[board.length-1][i]){
                accessRightOrBottom  = true;
            }
        }



        return accessLeftOrTop&&accessRightOrBottom;


    }



    public List<List<Integer>> pacificAtlantic2(int[][] heights) {
        List<List<Integer>> result  = new ArrayList<>();
        if(heights==null||heights.length==0) return result;
        if(heights[0]==null||heights[0].length==0) return result;


        boolean[][] broad = new boolean[heights.length][heights[0].length];

        for(int i = 0; i <heights.length;i++){
            for(int j = 0 ; j<heights[0].length;j++){
                    solution2(heights,broad,i,j);
                    // 注意这里只能说明 i,j 这里的水 能留到 board为true的地方，
                    // 不能代表 board true的地方都互通
                    if(isLegat(broad)) {
                        List<Integer> list = new ArrayList<>();
                        list.add(i);
                        list.add(j);
                        result.add(list);

                    }
                    for(boolean[] bArray :broad){
                        Arrays.fill(bArray,false);
                    }



            }
        }



        return result;

    }



    void solution2(int[][] heights ,boolean[][] broad,int row, int column){

        broad[row][column] = true;

        //左上右下
        if(column>0){
            if(!broad[row][column-1]&&heights[row][column]>=heights[row][column-1]) {
//                broad[row][column-1] = true;
//                isRead[row][column-1] = true;
                solution2(heights, broad, row, column - 1);
            }
        }

        if(row>0){
            if(!broad[row-1][column]&&heights[row][column]>=heights[row-1][column]) {
//                broad[row-1][column] = true;
//                isRead[row-1][column] = true;
                solution2(heights, broad, row - 1, column);
            }
        }

        if(column<(heights[0].length-1)){
            if(!broad[row][column+1]&&heights[row][column]>=heights[row][column+1]) {
//                broad[row][column+1] = true;
//                isRead[row][column+1] = true;
                solution2(heights, broad, row, column + 1);
            }
        }

        if(row<(heights.length-1)){
            if(!broad[row+1][column]&&heights[row][column]>=heights[row+1][column]) {
//                broad[row+1][column] = true;
//                isRead[row+1][column] = true;
                solution2(heights, broad, row + 1, column);
            }
        }



    }


}

package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_37 {
    static char[][] chars = new char[9][9];
    public static void main(String[] args) {
//        System.out.println('Hello World!');
        LeetCode_37 solution = new LeetCode_37();
//        System.out.println(solution.solution(4));
//        System.out.println(solution.solveNQueens(4));
//        System.out.println(solution.solveNQueens(5));
//        System.out.println(solution.solveNQueens(5));
        
        

        chars[0] = new char[]{'5','3','.','.','7','.','.','.','.'};
        chars[1] = new char[]{'6','.','.','1','9','5','.','.','.'};
        chars[2] = new char[]{'.','9','8','.','.','.','.','6','.'};
        chars[3] = new char[]{'8','.','.','.','6','.','.','.','3'};
        chars[4] = new char[]{'4','.','.','8','.','3','.','.','1'};
        chars[5] = new char[]{'7','.','.','.','2','.','.','.','6'};
        chars[6] = new char[]{'.','6','.','.','.','.','2','8','.'};
        chars[7] = new char[]{'.','.','.','4','1','9','.','.','5'};
        chars[8] = new char[]{'.','.','.','.','8','.','.','7','9'};


        char[][] result = new char[9][9];

        result[0] = new char[]{'5','3','4','6','7','8','9','1','2'};
        result[1] = new char[]{'6','7','2','1','9','5','3','4','8'};
        result[2] = new char[]{'1','9','8','3','4','2','5','6','7'};
        result[3] = new char[]{'8','5','9','7','6','1','4','2','3'};
        result[4] = new char[]{'4','2','6','8','5','3','7','9','1'};
        result[5] = new char[]{'7','1','3','9','2','4','8','5','6'};
        result[6] = new char[]{'9','6','1','5','3','7','2','8','4'};
        result[7] = new char[]{'2','8','7','4','1','9','6','3','5'};
        result[8] = new char[]{'3','4','5','2','8','6','1','7','9'};



        result[0] = new char[]{'5','3','4','6','7','8','9','1','2'};
        chars[0] = new char[]{'5','3','4','6','7','8','9','1','2'};
        chars[0] = new char[]{'5','3','.','.','7','.','.','.','.'};

        result[1] = new char[]{'6','7','2','1','9','5','3','4','8'};
        chars[1] = new char[]{'6','7','2','1','9','5','3','4','8'};
        chars[1] = new char[]{'6','.','.','1','9','5','.','.','.'};


        result[2] = new char[]{'1','9','8','3','4','2','5','6','7'};
        chars[2] = new char[]{'1','9','8','3','4','2','5','6','7'};
        chars[2] = new char[]{'.','9','8','.','.','.','.','6','.'};

        result[3] = new char[]{'8','5','9','7','6','1','4','2','3'};
        chars[3] = new char[]{'8','5','9','7','6','1','4','2','3'};
        chars[3] = new char[]{'8','.','.','.','6','.','.','.','3'};

        result[4] = new char[]{'4','2','6','8','5','3','7','9','1'};
        chars[4] = new char[]{'4','2','6','8','5','3','7','9','1'};
        chars[4] = new char[]{'4','.','.','8','.','3','.','.','1'};

        result[5] = new char[]{'7','1','3','9','2','4','8','5','6'};
        chars[5] = new char[]{'7','1','3','9','2','4','8','5','6'};
//        chars[5] = new char[]{'7','.','.','.','2','.','.','.','6'};

        result[6] = new char[]{'9','6','1','5','3','7','2','8','4'};
        chars[6] = new char[]{'9','6','1','5','3','7','2','8','4'};
//        chars[6] = new char[]{'.','6','.','.','.','.','2','8','.'};

        result[7] = new char[]{'2','8','7','4','1','9','6','3','5'};
        chars[7] = new char[]{'2','8','7','4','1','9','6','3','5'};
//        chars[7] = new char[]{'.','.','.','4','1','9','.','.','5'};

        result[8] = new char[]{'3','4','5','2','8','6','1','7','9'};
        chars[8] = new char[]{'3','4','5','2','8','6','1','7','9'};
//        chars[8] = new char[]{'.','.','.','.','8','.','.','7','9'};
        solution.solveSudoku2(chars);


        solution.printLnChars();

        System.out.println(solution.isSame(chars,result));
    }

    boolean isSame(char[][] cs1 ,char[][] cs2){


        if(cs1.length !=cs2.length ) return false;

        for(int i =0;i<cs1.length;i++){
            boolean r = Arrays.equals(cs1[i],cs2[i]);
            if(!r) return false;
        }
        return true;
    }



    public void printLnChars(){
        for(char[] cs :chars) {
            System.out.println(Arrays.toString(cs));
        }
    }

    public void printLnChars(char[][] chars){
        for(char[] cs :chars) {
            System.out.println(Arrays.toString(cs));
        }
    }

    // 击败 40
    // 重点 是否合法的判断  如果没有 有效值的时候 要及时回溯，不要接着往下跑
    public void solveSudoku(char[][] board) {
        solution(board,0);
    }
    public boolean solution(char[][] board,int row) {
        System.out.println(" row : "+ row );


        if(row==board.length-1){
            boolean isExit =true;
            for(int j =0 ;j<board.length;j++){
                if(needHandle(board[board.length-1][j]))
                    isExit = false;
            }
            if(isExit)
                return true;
        }

        //处理 哪一行
        for(int i = row ; i<board.length;i++){

            // 行内 处理
            for(int j = 0 ; j<board.length;j++){


                if(needHandle(board[i][j])){

                    for(int q = 1 ;q<10;q++){

                        if(isLegal(board,q,i,j)){
                            board[i][j] =(char) ('0'+q);

                            boolean r = solution(chars,i);
                            if(r) return true;
                            board[i][j] =(char) ('0');


                        }

                    }
                    // 如果1 到9 都不合要求的话 就要回溯
                    return false;
                }


            }

        }


        return false;
    }

    boolean needHandle(char c){
        if(c>='1'&&c<='9')  return false;

        return true;
    }


    public  boolean isLegal(char[][] board ,int c ,int p , int q){
//        printLnChars();
//        System.out.println('c  '+c+' i  '+p +'  q  '+q);
        char charc = (char)(c+'0');

        // 横
        for(int i = 0;i<board.length;i++){
            if(board[p][i]==charc)
                return false;
        }
        // 竖
        for(int i = 0 ;i<board.length;i++){
            if(board[i][q] == charc)
                return false;
        }
        // 区域
        int indexI = p/3 *3;
        int indexJ = q/3 *3;


        for(int i = indexI ; i<indexI+3;i++){
            for(int j = indexJ ; j<indexJ+3;j++){
                if(board[i][j] == charc)
                    return false;
            }
        }

        return true;
    }


    public void solveSudoku2(char[][] board) {


        System.out.println(  "solve2 " +solve2(board,0,0));

    }
    public boolean solve2(char[][] board,int x ,int y ) {

        if(board == null) return false;
        if(x>= board.length ||y>=board.length)  {
            return true;
        }

        for( int i = x ; i<board.length;i++){
            for(int j = y ; j<board[0].length;j++){

                if(board[i][j] =='.'){

                    for(int q = 1 ;q<=9;q++){

                        if(enableX(board, (char) ('0'+q),i)&&
                           enableY(board, (char) ('0'+q),j)&&
                           enableH(board, (char) ('0'+q),i,j)){
                           board[i][j] =(char) ('0'+q);

                           boolean resultTmp =false;
                           if(j >= board[0].length-1){
                               resultTmp = solve2(board,i+1,0);
                           }else {
                               // 注意得 从 0 开始  要不下一列的元素判断就变少了
                               resultTmp = solve2(board, i,0);
//                               resultTmp = solve2(board, i, 0);
                           }


                           if(resultTmp) {
                               return true;
                           }
                           board[i][j] ='.';

                        }


                    }
                    return false;
                }
            }
        }

        return true;

    }

    // 允许同行
    boolean enableX(char[][] chars , char  c,int xIndex){


        for(char cTmp : chars[xIndex]){
            if (c == cTmp){
                return false;
            }
        }
        return true;
    }



    // 允许同列
    boolean enableY(char[][] chars , char  c,int yIndex ){

        for(int i = 0 ; i<chars.length;i++){

            if(chars[i][yIndex] == c){
                return false;
            }

        }
        return true;
    }

    // 允许同区域
    boolean enableH(char[][] chars , char  c,int xIndex , int  yIndex){

        int startX = xIndex/3;
        int startY = yIndex/3;

        for(int i = 0;i<3;i++){
            for(int j  = 0 ; j<3;j++){

                if(chars[3*startX+i][3*startY+j] == c){
                    return false;
                }

            }
        }
        return true;
    }
}

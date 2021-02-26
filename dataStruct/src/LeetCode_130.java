package src;

import java.util.Arrays;

public class LeetCode_130 {

    public static void main(String[] args) {
        LeetCode_130 code = new LeetCode_130();

        char[][] chars = new char[9][9];
//        chars[0]=new char[]{'A','B','C','E'};
//        chars[1]=new char[]{'S','F','C','S'};
//        chars[2]=new char[]{'A','D','E','E'};

        chars[0]=new char[]{'X','X','X','X'};
        chars[1]=new char[]{'X','O','O','X'};
        chars[2]=new char[]{'X','X','O','X'};
        chars[3]=new char[]{'X','O','X','X'};

        chars[3]=new char[]{'O','X','O','O','O','O','O','O','O'};
        chars[3]=new char[]{'X','O','X','X'};
        chars[3]=new char[]{'X','O','X','X'};
        chars[3]=new char[]{'X','O','X','X'};
        chars[3]=new char[]{'X','O','X','X'};


        chars[0]=new char[]{'O','X','O','O','O','O','O','O','O'};
        chars[1]=new char[]{'O','O','O','X','O','O','O','O','X'};
        chars[2]=new char[]{'O','X','O','X','O','O','O','O','X'};
        chars[3]=new char[]{'O','O','O','O','X','O','O','O','O'};
        chars[4]=new char[]{'X','O','O','O','O','O','O','O','X'};
        chars[5]=new char[]{'X','X','O','O','X','O','X','O','X'};
        chars[6]=new char[]{'O','O','O','X','O','O','O','O','O'};
        chars[7]=new char[]{'O','O','O','X','O','O','O','O','O'};
        chars[8]=new char[]{'O','O','O','O','O','X','X','O','O'};
//        System.out.println(code.exist(chars,"ABCCED"));
//        System.out.println(code.exist(chars,"SEE"));
//        System.out.println(code.exist(chars,"ABCB"));
        code.solve(chars);
        System.out.println(chars);

    }
    // 击败20
    public void solve(char[][] board) {
        boolean[][] isReads = new boolean[board.length][board[0].length];
        boolean[][] tmpBoard = new boolean[board.length][board[0].length];
        solve(board,isReads,tmpBoard);
    }
    public void solve(char[][] board,boolean[][] isReads,boolean[][] tmpBoard) {

        for(int i = 0;i<board.length;i++){
            for(int j = 0 ;j<board[0].length;j++){

                if(board[i][j]=='O'&&!isReads[i][j]){
                    expand(board,isReads,tmpBoard,i,j);
                    change(board, tmpBoard);
                    for(int q = 0  ;q<board.length;q++) {
                        Arrays.fill(tmpBoard[q], false);
                    }
                }
            }
        }
    }


    //扩张
    void expand(char[][] board,boolean[][] isReads,boolean[][] tmpBoard,int i , int j){

        if(board[i][j] =='O') {
            isReads[i][j] = true;
            tmpBoard[i][j] = true;
            //左上右下

            if(j>0){
                if(board[i][j-1] == 'O'&&!isReads[i][j-1]){
                    expand(board,isReads,tmpBoard,i,j-1);
                }
            }

            if(i>0){
                if(board[i-1][j] == 'O'&&!isReads[i-1][j]){
                    expand(board,isReads,tmpBoard,i-1,j);
                }
            }

            if(j<board[0].length-1){
                if(board[i][j+1] == 'O'&&!isReads[i][j+1]){
                     expand(board,isReads,tmpBoard,i,j+1);
                }
            }

            if(i<board.length-1){
                if(board[i+1][j] == 'O'&&!isReads[i+1][j]){
                    expand(board,isReads,tmpBoard,i+1,j);
                }
            }
        }
        return ;
    }


    void change(char[][] board ,boolean[][] tmpBoard){

        for(int i = 0;i<tmpBoard[0].length;i++){
            if(tmpBoard[0][i]) return ;
            if(tmpBoard[tmpBoard.length-1][i]) return ;
        }

        for(int i = 0;i<tmpBoard.length;i++){
            if(tmpBoard[i][0]) return ;
            if(tmpBoard[i][tmpBoard[0].length-1]) return ;
        }


        for(int i = 0 ; i<tmpBoard.length;i++){

            for(int j = 0 ; j<tmpBoard[0].length;j++){

                if(tmpBoard[i][j]){
                    board[i][j] = 'X';
                }
            }

        }

    }


}

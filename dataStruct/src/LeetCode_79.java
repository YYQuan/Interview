package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_79 {

    public static void main(String[] args) {
        LeetCode_79 code = new LeetCode_79();

        char[][] chars = new char[3][4];
        chars[0]=new char[]{'A','B','C','E'};
        chars[1]=new char[]{'S','F','C','S'};
        chars[2]=new char[]{'A','D','E','E'};

        chars[0]=new char[]{'C','A','A'};
        chars[1]=new char[]{'A','A','A'};
        chars[2]=new char[]{'B','C','D'};


//        System.out.println(code.exist(chars,"ABCCED"));
//        System.out.println(code.exist(chars,"SEE"));
//        System.out.println(code.exist(chars,"ABCB"));
        System.out.println(code.exist(chars,"AAB"));

    }


    // 击败 97
    public boolean exist(char[][] board, String word) {
        boolean[][] isRead = new boolean[board.length][board[0].length];
        return exist(board,isRead,0,word);
    }

    public boolean exist(char[][] board,boolean[][] isRead, int index, String word) {

        if(index>=word.length()) return true;

        char c = word.charAt(index);
        // 左上右下
        int i = 0;
        int j = 0;
        for( i = 0;i<board.length;i++){
            for( j = 0; j<board[0].length;j++){
                if(board[i][j] == c) {
                    isRead[i][j] =true;
                    boolean result = existC(board,isRead,i,j,index+1,word);
                    if(result) return true;
                    isRead[i][j] =false;
                }
            }
        }

        return false;


    }

    boolean existC(char[][] board,boolean[][] isRead,int i ,int j ,int index ,String word ){
        if(index>=word.length()) return true;
        char c = word.charAt(index);
        if(i>0){
            if(board[i-1][j] == c&& !isRead[i-1][j] ) {
                isRead[i-1][j] = true;
                boolean result = existC(board, isRead,i-1,j,index+1,word);
                if(result) return true;
                isRead[i-1][j] = false;
            }
        }
        if(j>0){
            if(board[i][j-1] == c&& !isRead[i][j-1]) {
                isRead[i][j-1] = true;
                boolean result = existC(board, isRead,i,j-1,index+1,word);
                if(result) return true;
                isRead[i][j-1] = false;
            }
        }

        if(i<board.length-1){
            if(board[i+1][j] == c&&!isRead[i+1][j]) {
                isRead[i+1][j] = true;
                boolean result = existC(board, isRead,i+1,j,index+1,word);
                if(result) return true;
                isRead[i+1][j] = false;
            }
        }

        if(j<board[0].length-1){
            if(board[i][j+1] == c&&!isRead[i][j+1]) {
                isRead[i][j+1] = true;
                boolean result = existC(board, isRead,i,j+1,index+1,word);
                if(result) return true;
                isRead[i][j+1] = false;
            }
        }
        return false;
    }
}

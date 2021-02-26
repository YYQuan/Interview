package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_52 {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        LeetCode_52 solution = new LeetCode_52();
//        System.out.println(solution.solution(4));
//        System.out.println(solution.solveNQueens(4));
//        System.out.println(solution.solveNQueens(5));
//        System.out.println(solution.solveNQueens(5));
        List<String> tmp = new ArrayList<>();
        tmp.add("..Q..");
        tmp.add("....Q");
        tmp.add(".Q...");
        tmp.add("...Q.");
        tmp.add("Q....");
//        System.out.println(solution.solveNQueens(5).contains(tmp));
//        System.out.println(solution.solveNQueens(5));
        System.out.println(solution.solveNQueens(8).size());
//        System.out.println(solution.solveNQueens(5).size());
//        System.out.println(solution.solveNQueens(4).contains(tmp));
//        System.out.println(solution.solveNQueens(5).size());
//        System.out.println(solution.solution(5).size());

    }

    // 直接用51 的方式来处理 击败 百分之5
    public int totalNQueens(int n) {
        return solveNQueens(n).size();
    }

    // 击败 百分之7
    // 这题 对比其他 除了判断条件不一致之外， 就是 有两步 恢复动作 ，这个区别而已。
    // 抹去无效位置 其实和 岛屿 差不多的意思
    // 一行 只可能有一个 皇后 也可以用来截枝
    List<List<String>>  list = new ArrayList<>();
    public List<List<String>> solveNQueens(int n) {

        char[][]  chars = new char[n][n] ;
        for(int i = 0 ;i<n;i++) {
            //_表示未处理
            Arrays.fill(chars[i], '_');
        }

        solveNQueens(n,chars,0,0,-1);
        return list;
    }

    public void solveNQueens(int n,char[][] board,int index,int i , int j) {

        // 如果当前有 n个皇后了， 这说明有解了
        if(index == n ) {
            List<String> strs = boardTransfer(board);
            list.add(boardTransfer(board));
            return ;
        }

        // 保存当前的位置的状态
        char[][] tmp = new char[n][n];
        for(int z = 0 ;z<n;z++){
            tmp[z]  = Arrays.copyOf(board[z],n);
        }

        if(j>=0) {
            // 抹去无效位置后 开始找 index 列的 有效位置
            fillChar(board, i, j);
        }

        //
        for(int q =0 ;q<n;q++){
            if(board[index][q]=='_'){
                board[index][q] = 'Q';
                solveNQueens(n,board,index+1,index,q);
                board[index][q] = '_';
            }

        }

        // 恢复无效位置
        for(int z = 0 ;z<n;z++){
            board[z]  = Arrays.copyOf(tmp[z],n);
        }

    }

    void fillChar(char[][] board, int p , int q){
        // 不合法的位置都 填为.

        // 横 p
        // 竖 q
        // 左斜 p+q
        int left = p+q;
        // 右斜  p-q;
        int right = p-q;

        for(int i = 0 ;i<board.length;i++){
            for(int j = 0 ;j<board[0].length;j++){
                if(i==p&&j==q) continue;
                if(board[i][j] !='_') continue;

                if(i ==p){
                    board[i][j] = '.';
                }else if(j == q){
                    board[i][j] = '.';
                }else if(i+j == left){
                    board[i][j] = '.';
                }else if(i-j == right){
                    board[i][j] = '.';
                }

            }
        }


    }

    List<String>  boardTransfer(char[][] board){
        List<String> listStr = new ArrayList<>();
        for(int i = 0; i<board.length;i++){
            StringBuilder builder = new StringBuilder();
            for(int j = 0 ; j<board.length;j++){
                builder.append(board[i][j]);
            }
            listStr.add(builder.toString());
        }
        return listStr;
    }
}

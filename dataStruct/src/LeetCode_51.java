package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LeetCode_51 {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        LeetCode_51 solution = new LeetCode_51();
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


    public List<List<String>> solveNQueens2(int n) {
        char[][]  intss = new char[n][n];
        for(char[] ints :intss) {
            Arrays.fill(ints,'.');
        }


        char[][] tmpIntss = intss.clone();
//
        List<List<String>> result = new ArrayList<>();
//        for(char[] ints :intss) {
//            //List<Integer> intsList = Arrays.stream(ints).boxed().collect(Collectors.toList());
//            List<String> cList =  new String(ints).chars().mapToObj(i -> (String)""+i).collect(Collectors.toList());
//
//            result.add(cList);
//        }
        solution(intss,0,tmpIntss,result);
        return result;

    }

    public  void solution(char[][] charss,int row ,char[][] tmpChars ,List<List<String>> result){

        if(row>= charss.length) {
            List<String> sList = new ArrayList<>();
            for(char[] cs :tmpChars){
                sList.add(new String(cs));
            }
            result.add(sList);
            return ;
        }

        for(int i = 0 ;i<charss.length;i++){

            if(isLegal(tmpChars,row,i)){
                tmpChars[row][i] ='Q';
                solution(charss,row+1,tmpChars,result);
                tmpChars[row][i] ='.';
            }

        }
    }

    boolean isLegal(char[][] chars , int row,int coloum){

        return legalColoum(chars,coloum)&&
                legalRow(chars,row)&&
                legalRightSlant(chars,row,coloum)&&
                legalLeftSlant(chars,row,coloum);
    }


    boolean  legalRow(char[][] chars , int row){
        for(char c : chars[row]){
            if(c =='Q') return false;
        }
        return true;
    }
    boolean  legalColoum(char[][] chars , int coloum){
        for(int i = 0; i<chars.length;i++){
            if(chars[i][coloum] =='Q') return false;
        }
        return true;
    }

    boolean legalRightSlant(char[][] chars , int row,int coloum){
        int sum = row +coloum;
        for(int i = 0 ; i<chars.length;i++){
            for( int j = 0 ;j<chars.length;j++){
                if(i+j == sum&&chars[i][j]=='Q'){
                    return false;
                }
            }
        }
        return true;
    }

    boolean legalLeftSlant(char[][] chars , int row,int coloum){
        int d = row -coloum;
        for(int i = 0 ; i<chars.length;i++){
            for( int j = 0 ;j<chars.length;j++){
                if(i-j == d&&chars[i][j]=='Q'){
                    return false;
                }
            }
        }
        return true;
    }

}

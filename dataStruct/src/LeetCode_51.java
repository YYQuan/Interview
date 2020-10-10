import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_51 {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        LeetCode_51 solution = new LeetCode_51();
        System.out.println(solution.solution(4));

    }


    // N 皇后问题
    // 先求有多少个解 再求每个解是怎么样的。


    int result  = 0;

    boolean[]  colum  ;   //该列是否可用
    boolean[]  rule1 ;//右斜线  i + j =X   X :{0,2n-2}
    boolean[]  rule2 ;//左斜线  i - j = X  X :{-(n-1),n-1}

    List<List<String>>  results  = new ArrayList<>();
    public List<List<String>>  solution( int n ){
//        int result = 0 ;


        colum = new boolean[n];
        rule1 = new boolean[2 * n - 1];
        rule2 = new boolean[2 * n - 1];

        LinkedList<Integer> resultList = new LinkedList<>();
        putQueen(0,  n,resultList);



        return  results;
    }

    private  void  putQueen(int currentRow ,  int count ,LinkedList<Integer> resultList){

        if(currentRow == count)  {
//            result +=1;
            ;
            results.add(generateBoard(count,resultList));
//            results.add((LinkedList<Integer>)resultList.clone());
            return ;
        }

        // 把  当前 row  的queen 放在 第i列
        for(int i = 0 ; i<count ;i++){
            // 如果这行这列 合法 就继续到下一行
            if(isLegal(currentRow,i,count)){
                resultList.addFirst(i);
                colum[i] = true;
                rule1[currentRow+i] = true;
                rule2[currentRow - i +count -1] = true;
                putQueen(currentRow+1,count,resultList);
                colum[i] = false;
                rule1[currentRow+i] = false;
                rule2[currentRow - i +count -1] = false;
                resultList.removeLast();
            }
        }

        return ;
    }

    private List<String> generateBoard(int n, LinkedList<Integer> row){

        assert row.size() == n;

        ArrayList<String> board = new ArrayList<String>();
        for(int i = 0 ; i < n ; i ++){
            char[] charArray = new char[n];
            Arrays.fill(charArray, '.');
            charArray[row.get(i)] = 'Q';
            board.add(new String(charArray));
        }
        return board;
    }
//    private boolean isArea(int i , int j ){
//        return false;
//    }

    private boolean isLegal(int row ,int colum ,int count){
        assert row+colum <(2*count-1);
        assert row-colum+count-1 <(2*count-1);
//        System.out.println("row "+row);
//        System.out.println("colum "+colum);
//        System.out.println("count " +count);
//        System.out.println("row+colum " +(row+colum));
//        System.out.println("row-colum+count-1 " +(row-colum+count-1));
//        System.out.println("count " +count);


        return !this.colum[colum]&&!rule1[row+colum]&&!rule2[row-colum+count-1];
    }


}

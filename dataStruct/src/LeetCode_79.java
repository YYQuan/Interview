import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_79 {

    public static void main(String[] args) {
        char[][] b1 = {
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'}};
        System.out.println("Hello World!");
        LeetCode_79 code = new LeetCode_79();
        String words[] = {"ABCCED", "SEE", "ABCB" };
//        String words[] = {"ABCCED" };
//        String words[] = {"SEE" };
//        String words[] = {"SE" };
//        String words[] = {"SE"};
//        String words[] = {"SEC"};
        for(int i = 0 ; i < words.length ; i ++)
            if((new LeetCode_79()).solution(b1, words[i]))
                System.out.println("found " + words[i]);
            else
                System.out.println("can not found " + words[i]);
//        System.out.println(code.solution(b1,2));

    }


    int m ; // 行数
    int n ; // 列数
    boolean[][]  isEnableRead ;

    public boolean solution(char[][] arr,String word){

        if(arr == null || arr.length<=0) return false;

        if(word==null||word.length()<=0)  throw new  IllegalArgumentException(" illegal  params");

        m  = arr.length; //行数
        n  = arr[0].length;// 列数

        isEnableRead = new boolean[m][n];

        for(int i = 0 ; i< m ;i++){
            for(int j = 0 ; j<n ;j++){
                isEnableRead[i][j]  = true;
            }
        }


        for(int i = 0; i<m ; i++)
            for(int j = 0 ;j <n;j++) {

//                if(searchWord(arr, i, j, 0, word))
                if(searchWord2(arr, i, j, 0, word))
                    return true;
            }

        return false;
    }

    public boolean  searchWord(char[][] arr,final int i,final int j , int index,String word){




        if(index == word.length())  return true;

        if(!validData(i,j))  return false;

        System.out.println(" " + arr[i][j]);

        if(arr[i][j] == word.charAt(index)){
            isEnableRead[i][j] = false;
            System.out.println(" == " + arr[i][j]);
            // 上
            if(validData(i-1,j)){
//                isEnableRead[i-1][j] = false;
                boolean result = searchWord(arr,i-1,j,index+1,word);
                System.out.println("top == " + arr[i-1][j]);
//                isEnableRead[i-1][j] = true;
                if(result)  return  true;
            }



            // 右
            if(validData(i,j+1)){
//                isEnableRead[i][j+1] = false;
                boolean result = searchWord(arr,i,j+1,index+1,word);
//                isEnableRead[i][j+1] = true;
                System.out.println("right  == " + arr[i][j+1]);

                if(result)  return  true;
            }



            // 下
            if(validData(i+1,j)){
//                isEnableRead[i+1][j] = false;
                boolean result = searchWord(arr,i+1,j,index+1,word);
//                isEnableRead[i+1][j] = true;
                System.out.println("bottom == " + arr[i+1][j]);

                if(result)  return  true;
            }

            // 左
            if(validData(i,j-1)){
//                isEnableRead[i][j-1] = false;
                boolean result = searchWord(arr,i,j-1,index+1,word);
//                isEnableRead[i][j-1] = true;
                System.out.println("left == " + arr[i][j-1]);

                if(result)  return  true;
            }
            isEnableRead[i][j] = true;


        }

        return false;
    }


    int[][] d = {{-1,0},{0,+1},{+1,0},{0,-1}};


    public boolean  searchWord2(char[][] arr,final int i,final int j , int index,String word){
        if(index == word.length())  return true;

        if(!validData(i,j))  return false;

        System.out.println(" " + arr[i][j]);

        if(arr[i][j] == word.charAt(index)){
            isEnableRead[i][j] = false;
            System.out.println(" == " + arr[i][j]);

            for(int[] tmp : d){
                int newi = i+tmp[0];
                int newj = j+tmp[1];
                if(validData(newi,newj)){
                    boolean result = searchWord(arr,newi,newj,index+1,word);
                    System.out.println("top == " + arr[newi][newj]);
                    if(result)  return  true;
                }
            }
            isEnableRead[i][j] = true;


        }

        return false;
    }


    private boolean validData(int i, int j ) {

//        return i>=0 &&j>=0 && i <m && j<n;
        return i>=0 &&j>=0 && i <m && j<n&& isEnableRead[i][j]==true;
    }

}

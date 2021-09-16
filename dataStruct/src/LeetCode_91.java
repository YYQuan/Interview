package src;

import javafx.util.Pair;

import java.util.LinkedList;

public class LeetCode_91 {

    public static void main(String[] args) {
        LeetCode_91 code = new LeetCode_91();
        ;
        System.out.println(code.numDecodings("2611055971756562"));
//        2 6 1 10 5 5 9 7 1 7 5 6 5 6 2
//        2 6 1 10 5 5 9 7 17 5 6 5 6 2
//        26 1 10 5 5 9 7 17 5 6 5 6 2
//        26 1 10 5 5971756562
//        System.out.println(code.numSquares2(13));

    }
    //概念题难懂 先略过
//    public int numDecodings(String s) {
//
//    }

        public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    public int numDecodings(String s) {
        if(s.length()<=0) return 0;
        if(s.charAt(0) =='0') return 0;
        return solution(s,0);
    }

    int  solution(String s , int index){

        if(index == s.length()){
            return 1;
        }

        if(index>s.length()) return 0;


        int sum = 0 ;
        if(s.charAt(index)=='0')  return 0;
        else if(index<(s.length()-1)&&s.charAt(index)<'3'&&s.charAt(index+1)=='0'){

            sum+= solution(s,index+2);
        }else if(index<(s.length()-1)&&s.charAt(index)=='1'&&s.charAt(index+1)<='9'){
            sum+= solution(s,index+2);
            sum+= solution(s,index+1);
        }else if(index<(s.length()-1)&&s.charAt(index)=='2'&&s.charAt(index+1)<='6'){
            sum+= solution(s,index+2);
            sum+= solution(s,index+1);
        }else{
            sum+= solution(s,index+1);
        }
        return sum;
    }
}

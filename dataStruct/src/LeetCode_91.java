package src;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;

public class LeetCode_91 {

    public static void main(String[] args) {
        LeetCode_91 code = new LeetCode_91();

//        int[] ints1 = new int[]{1,2};
//        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
        Integer[] ints1 = new Integer[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        ListNode head = new ListNode();
        ListNode node = head;
        for(int i :ints1){
            node.next = new ListNode(i);
            node = node.next;
        }

        node = reverseBetween(head.next,2,4);

        while(node!=null){
            System.out.println(node.val);
            node= node.next;
        }

//        List<Integer> result =code.rightSideView(node1);


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

    public static ListNode reverseBetween(ListNode head, int left, int right) {
        if(head ==null) return null;
        ListNode virtualHead = new ListNode();
        virtualHead.next=head;
        ListNode start = null;
        ListNode end = null;

        ListNode pNode = virtualHead;
        ListNode cNode  =virtualHead.next;
        ListNode nNode = head.next;
        for(int i = 1;i<=right;i++){

            if(i==left){
                start = pNode;
            }

            if(i>left){
                if(cNode == null)   return null;
                cNode.next = pNode;
                pNode = cNode;
                cNode = nNode ;
                if(nNode!=null) {
                    nNode = nNode.next;
                }


            }else{
                if(cNode == null)   return null;
                pNode = pNode.next;
                cNode = cNode.next;
                if(nNode!=null) {
                    nNode = nNode.next;
                }
            }
        }
        if(start!=null) {
            ListNode tmpNode = start.next;
            if (tmpNode != null) {
                tmpNode.next = cNode;
            }
            start.next = pNode;

        }




        return virtualHead.next;
    }

    public static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
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

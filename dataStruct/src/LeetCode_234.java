package src;

import java.util.LinkedList;

public class LeetCode_234 {

    public static void main(String[] args) {
        LeetCode_234 code = new LeetCode_234();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
//        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};

        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        ListNode result =head;
        boolean isPalindrome = code.isPalindrome(head);
        boolean isPalindrome2 = code.isPalindrome2(head);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
        System.out.println(isPalindrome);
        System.out.println(isPalindrome2);

    }

    //击败 99
    public boolean isPalindrome(ListNode head) {

        if(head == null ||head.next == null) return true;

        ListNode node = head;

        int count = 1;
        while(node.next!=null){

            node = node.next;
            count++;

        }


        ListNode[] nodes = new ListNode[ count];

        node = head;
        for(int i = 0 ; i<count ;i++){
            nodes[i] = node;
            if(i!=count-1) {
                node = node.next;
            }
        }

        for(int i = 0;i<count/2;i++){

            if(nodes[i].val!=nodes[count-i-1].val) return false;

        }

        return true;




    }
    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

    }

    public boolean isPalindrome2(ListNode head) {
        if(head == null)  return false;

        LinkedList<ListNode> nodes = new LinkedList<>();

        ListNode node = head;
        while(node!=null){
            nodes.add(node);
            node = node.next;
        }

        while(!nodes.isEmpty()){

            if(nodes.size() ==1) return true;
            ListNode firstNode= nodes.poll();
            ListNode lastNode = nodes.removeLast();
            if(firstNode.val!=lastNode.val) return false;
        }

        return true;

    }



}

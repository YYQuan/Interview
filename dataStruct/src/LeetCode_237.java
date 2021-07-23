package src;

import java.util.Arrays;

public class LeetCode_237 {

    public static void main(String[] args) {
        LeetCode_237 code = new LeetCode_237();

//        int[] ints1 = new int[]{1,2};
//        int[] ints1 = new int[]{1,2,3};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        int[] ints1 = new int[]{4,2,1,3};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        code.deleteNode(head);
        while(head !=null) {
            System.out.println(head.val);
            head = head.next;
        }
    }

    //  击败 100
    //  既然 不知道怎么干掉自己 ，那么就把自己变成别人 ，然后把别人干掉 。你就是别人了。
    public void deleteNode(ListNode node) {
        if(node == null || node.next == null) return ;

        node.val = node.next.val;
        node.next = node.next.next;



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

    public void deleteNode2(ListNode node) {
        if(node  == null) return ;
        if(node.next == null) {
            node = null;
            return;
        }

        node.val =node.next.val;
        node.next =node.next.next;



    }


}

package src;

import java.util.HashMap;

public class LeetCode_206 {

    public static void main(String[] args) {
        LeetCode_206 code = new LeetCode_206();
        String pattern = "foo";
        String s = "bar";

        ListNode head = new ListNode(0);
        ListNode node = head;
        for( int i = 0 ; i<2 ;i++){
            node.next = new ListNode(i+1);
            node = node.next;
        }

        ListNode result =  code.reverseList(head);

        while(result.next !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    public ListNode reverseList(ListNode head) {

        if(head ==null||head.next == null)  return head;
        ListNode current = head;
        ListNode nextNode =  head.next;
        ListNode preNode  = null;
        while(current !=null){
            current.next= preNode;

            preNode = current;
            current = nextNode;
            if(nextNode!=null) {
                nextNode = nextNode.next;
            }


        };
        return  preNode;
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

}

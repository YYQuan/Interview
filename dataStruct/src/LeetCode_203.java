package src;

import java.util.HashMap;
import java.util.LinkedList;

public class LeetCode_203 {

    public static void main(String[] args) {
        LeetCode_203 code = new LeetCode_203();

        int[] ints1 = new int[]{6,1,2,3,4,5,6};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }



//        ListNode result =  code.removeElements(head,6);
        ListNode result =  code.removeElements2(head,6);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    // 击败 99
    public ListNode removeElements(ListNode head, int val) {

        ListNode virtualHead  = new ListNode();
        virtualHead.next = head;

        ListNode preNode = virtualHead;
        ListNode node = head;

        while(node!=null){

            if(node.val==val){
                preNode.next = node.next;
                node = node.next;
            }else{
                node = node.next;
                preNode = preNode.next;

            }




        }

        return virtualHead.next;


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


    public ListNode removeElements2(ListNode head, int val) {

        if(head == null)  return null;
        ListNode virtualHead = new ListNode();
        virtualHead.next= head;

        ListNode pNode = virtualHead;
        ListNode node = pNode.next;
        ListNode nNode = node.next;

        while(node!=null){

            if(node.val == val){

                pNode.next = nNode;
                node = nNode;
                if(node!=null){
                    nNode = node.next;
                }
            }else{
                pNode = pNode.next;
                node = node.next;
                if(node!=null){
                    nNode = node.next;
                }
            }

        }
        return virtualHead.next;
    }

}

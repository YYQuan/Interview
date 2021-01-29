package src;

import java.util.HashMap;

public class LeetCode_21 {

    public static void main(String[] args) {
        LeetCode_21 code = new LeetCode_21();

//        int[] ints1 = new int[]{1,1,1,2,3,4,5,6};
//        int[] ints1 = new int[]{1,1,2,2,3,3,4,5,6};
//        int[] ints1 = new int[]{1,2,3,3,4,4,5};
//        int[] ints2 = new int[]{1,2,3,3,4,4,5};
//        int[] ints1 = new int[]{1,2,4};
//        int[] ints2 = new int[]{1,3,4};
//        int[] ints1 = new int[]{0};
//        int[] ints2 = new int[]{0};
        int[] ints1 = new int[]{2};
        int[] ints2 = new int[]{1};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }

        ListNode head2 = new ListNode(ints2[0]);
        ListNode node2 = head2;
        for( int i = 1; i<ints2.length ;i++){
            node2.next = new ListNode(ints2[i]);
            node2 = node2.next;
        }


        ListNode result =  code.mergeTwoLists(head,head2);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }


    //击败 25
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        ListNode node  = l1;
        ListNode node2  = l2;

        ListNode resultHead = new ListNode(Integer.MIN_VALUE);
        ListNode resuleNode = resultHead;
        while(node!=null||node2!=null){

            while(node!=null){

                if(node2!=null){
                    if(node.val>node2.val){
                        break;
                    }
                }
                resuleNode.next = new ListNode(node.val);
                resuleNode = resuleNode.next;
                node = node.next;

            }

            while(node2!=null){
                if(node!=null){
                    if(node2.val>node.val){
                        break;
                    }
                }
                resuleNode.next = new ListNode(node2.val);
                resuleNode = resuleNode.next;

                node2 = node2.next;
            }


        }
        return resultHead.next;
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

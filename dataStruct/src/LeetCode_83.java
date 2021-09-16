package src;

import java.util.HashSet;
import java.util.Objects;

public class LeetCode_83 {

    public static void main(String[] args) {
        LeetCode_83 code = new LeetCode_83();
        String pattern = "foo";
        String s = "bar";

//        int[] ints = new int[]{3,5};
//        int[] ints = new int[]{3,5};
        int[] ints = new int[]{1,1,1,3};
//        int[] ints = new int[]{};
        ListNode head = new ListNode(ints[0]);
        ListNode node = head;
        for( int i = 1; i<ints.length ;i++){
            node.next = new ListNode(ints[i]);
            node = node.next;
        }

//        ListNode result =  code.reverseBetween(head,2,4);
//        ListNode result =  code.reverseBetween(head,1,1);
//        ListNode result =  code.reverseBetween(head,2,2);
//        ListNode result =  code.reverseBetween(head,1,2);
//        ListNode result =  code.reverseBetween(head,3,3);
        ListNode result =  code.deleteDuplicates2(head);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }


    // 击败百分之6
    public ListNode deleteDuplicates(ListNode head) {

        if(head==null) return head;

        HashSet<Integer> integerHashSet = new HashSet<>();
        ListNode node = head;
        integerHashSet.add(node.val);

        while(node!=null){


            if(node.next!=null){
                if(integerHashSet.contains(node.next.val)) {
                    node.next = node.next.next;
                    continue;
                }
            }
            node = node.next;
            if(node !=null) {
                integerHashSet.add(node.val);
            }


        }
        return head;
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


    public ListNode deleteDuplicates2(ListNode head) {
        if(head ==null) return null;
        ListNode  virtualHead = new ListNode();
        virtualHead.val = Integer.MIN_VALUE;
        virtualHead.next=head;

        ListNode pNode = virtualHead;
        ListNode cNode = head;
        ListNode nNode = head.next;

        while(cNode!=null){
            while(pNode.val == cNode.val){
                pNode.next = cNode.next;
                cNode = cNode.next;
                if(cNode!=null) {
                    nNode = cNode.next;
                }
            }

            pNode = pNode.next;
            if(cNode!=null)
                cNode = cNode.next;
            if(nNode!=null)
                nNode = nNode.next;
        }
        return  virtualHead.next;
    }
}

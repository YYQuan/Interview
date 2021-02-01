package src;

public class LeetCode_148 {

    public static void main(String[] args) {
        LeetCode_148 code = new LeetCode_148();

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
        ListNode result =  code.sortList(head);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }

    // 和 147题 一模一样 的解法就行了 。但是只击败了百分之 8
    public ListNode sortList(ListNode head) {
        if(head==null||head.next == null)  return head;

        ListNode virtualHead = new ListNode(Integer.MIN_VALUE);
        virtualHead.next = head;


        ListNode preNode = virtualHead;
        ListNode node = virtualHead.next;
        ListNode nextNode = node.next;
        q:while(node!=null){


            while(node.val>=preNode.val){

                if(nextNode==null) break q ;
                nextNode  = nextNode.next;
                node = node.next;
                preNode = preNode.next;

            }

            preNode.next = nextNode;


            ListNode tmpPre = virtualHead;
            ListNode tmpNode = tmpPre.next;

            while(tmpNode.val<= node.val){

                tmpNode = tmpNode.next;
                tmpPre = tmpPre.next;
            }

            tmpPre.next = node;
            node.next = tmpNode;

            node = nextNode;
            if(node!=null) {
                nextNode = node.next;
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

}

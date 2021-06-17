package src;

public class LeetCode_24 {

    public static void main(String[] args) {
        LeetCode_24 code = new LeetCode_24();

        int[] ints1 = new int[]{1,1,1,2,3,4,5,6};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }




//        ListNode result =  code.swapPairs(head);
        ListNode result =  code.swapPairs2(head);






        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    // 击败100
    public ListNode swapPairs(ListNode head) {
        if(head == null || head.next ==null) return head;
        ListNode virtualHead  = new ListNode();
        virtualHead.next = head;

        ListNode preNode = virtualHead;
        ListNode node  = head;
        ListNode nextNode = head.next;

        while(nextNode!=null&&node!=null){

            preNode.next = nextNode;
            node.next =nextNode.next;
            nextNode.next = node;


            preNode = node;
            node = preNode.next;
            if(node!=null){
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

    public ListNode swapPairs2(ListNode head) {

        if(head == null) return null;
        if(head.next ==null) return head;

        ListNode visualHead  = new ListNode();
        visualHead.next = head;

        ListNode pre = visualHead;
        ListNode node = visualHead.next;
        ListNode next = node.next;

        while(next!=null){
            node.next = next.next;
            pre.next = next ;
            next.next = node;

            pre  = node ;
            if(pre.next==null){
                break;
            }
            node = pre.next;
            if(node.next==null){
                break;
            }
            next = node.next;
        }

        return visualHead.next;
    }

}

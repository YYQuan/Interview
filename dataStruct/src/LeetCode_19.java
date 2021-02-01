package src;

public class LeetCode_19 {

    public static void main(String[] args) {
        LeetCode_19 code = new LeetCode_19();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};

        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        ListNode result = code.removeNthFromEnd(head,2);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }


    //  击败100
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode virtualHead =  new ListNode();
        virtualHead.next = head;

        ListNode  node = head;

        for(int i = 1 ; i<n;i++){
            if(node ==null)  return  virtualHead.next;
            node  =  node.next;
        }

        ListNode preNNode = virtualHead;
        ListNode nNode = head;

        while(node.next!=null){
             node = node.next;
             preNNode = preNNode.next;
             nNode = nNode.next;
        }

        preNNode.next = nNode.next;

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

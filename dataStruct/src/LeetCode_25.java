package src;

public class LeetCode_25 {

    public static void main(String[] args) {
        LeetCode_25 code = new LeetCode_25();

//        int[] ints1 = new int[]{1,2};
//        int[] ints1 = new int[]{1,2,3};
        int[] ints1 = new int[]{1,2,3,4,5,6,7,8};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        ListNode result =  code.reverseKGroup(head,3);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }

    // 击败百分之30
    public ListNode reverseKGroup(ListNode head, int k) {

        if(head.next==null||k==1)  return head;

        ListNode virtualHead = new ListNode();
        virtualHead.next = head;

        ListNode preKNode = virtualHead;//K开始的节点
        ListNode preNode = virtualHead;
        ListNode node = head;
        ListNode nextNode = head.next;

        w:while(true) {
            ListNode tmp = node;
            int i = 1;
            for (i = 1; i < k; i++) {
                if (tmp == null||tmp.next==null) {
                    break w;
                }

                tmp = tmp.next;
            }
            while(i-->0&&node!=null){

                node.next = preNode;

                preNode = node;
                node = nextNode;
                if(nextNode!=null) {
                    nextNode = nextNode.next;
                }

            }

            ListNode tmp1 = preKNode.next;

            preKNode.next = preNode;
            tmp1.next = node;
            preKNode = tmp1;
            preNode = tmp1;


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

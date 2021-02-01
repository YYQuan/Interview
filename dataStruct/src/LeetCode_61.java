package src;

public class LeetCode_61 {

    public static void main(String[] args) {
        LeetCode_61 code = new LeetCode_61();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};

        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        ListNode result = code.rotateRight(head,7);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }


    // 击败 53
    // 核心是 头尾相连之后  要偏移几次
    public ListNode rotateRight(ListNode head, int k) {
        if(head==null||head.next==null)  return head;
        ListNode virtualHead = new ListNode();
         virtualHead.next = head;

         ListNode preNode =virtualHead;
         ListNode node = head;

         int i  =1 ;
         while(node.next!=null){
             i++;
             preNode = preNode.next;
             node = node.next;


         }

         node.next = head;
         preNode = node;
         node = head;
         int offset = i-k%i;

         for( i  = 0 ;i<offset ;i++ ){
             preNode = preNode.next;
             node  = node.next;
         }

         preNode.next = null;

        return node;

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

package src;

import java.util.Objects;

public class LeetCode_92 {

    public static void main(String[] args) {
        LeetCode_92 code = new LeetCode_92();
        String pattern = "foo";
        String s = "bar";

//        int[] ints = new int[]{3,5};
//        int[] ints = new int[]{3,5};
        int[] ints = new int[]{1,2,3};
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
        ListNode result =  code.reverseBetween(head,1,3);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }


    // 击败 100  但是看起来这么简单的题 好多要注意的地方
    public ListNode reverseBetween(ListNode head, int m, int n) {


        if(head.next == null) return head;

        int i = 1;
        ListNode reverseHead  = null;
        ListNode preCurrentNode = null;
        ListNode currentNode = head;
        ListNode nextNode = head.next;




        while(i<n){


            preCurrentNode = currentNode;
            currentNode = nextNode;
            nextNode = nextNode!=null?nextNode.next:null;

            if(i==m-1) {
                reverseHead = preCurrentNode;
            }

            if(i>=m){
                currentNode.next = preCurrentNode;
            }

            i++;

        }

        if(m==1){
            reverseHead = currentNode;
            head.next = nextNode;
            return reverseHead;
        }
        if(n>m) {
            ListNode tmpNode = reverseHead != null ? reverseHead.next : reverseHead;

            if (reverseHead != null && !reverseHead.equals(currentNode)) {
                reverseHead.next = currentNode;
            }

            if (tmpNode != null) {
                tmpNode.next = nextNode;
            }
        }


        return m==1 ?reverseHead:head;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListNode listNode = (ListNode) o;
            return val == listNode.val &&
                    Objects.equals(next, listNode.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(val, next);
        }
    }

}

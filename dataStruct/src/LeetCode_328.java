package src;

public class LeetCode_328 {

    public static void main(String[] args) {
        LeetCode_328 code = new LeetCode_328();
        String pattern = "foo";
        String s = "bar";

//        int[] ints = new int[]{3,1};
//        int[] ints = new int[]{3,5};
//        int[] ints = new int[]{1,4,3,2,5,2};
//        int[] ints = new int[]{1,2,3,4,5};
//        int[] ints = new int[]{2,1,3,5,6,4,7};
        int[] ints = new int[]{1};
//        int[] ints = new int[]{1,2,1};
//        int[] ints = new int[]{};
        ListNode head = new ListNode(ints[0]);
        ListNode node = head;
        for( int i = 1; i<ints.length ;i++){
            node.next = new ListNode(ints[i]);
            node = node.next;
        }


        ListNode result =  code.oddEvenList(head);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    // 击败 百分100
    public ListNode oddEvenList(ListNode head) {

        if(head == null) return head;


        ListNode startJi = null;
        ListNode endJi = null;

        ListNode endOu = null;


        ListNode node = head;
        int i = 0;
        while(node !=null){


            if(i%2 ==0){
                if(endOu == null){
                    endOu = node;
                }else{
                    endOu.next = node;

                    endOu  = endOu.next;

                }
            }else{
                if(endJi == null){
                    startJi = node;
                    endJi = node;
                }else{
                   endJi.next = node;
                   endJi = endJi.next;

                }
            }

            node = node.next;
            i++;
        }
        if(endJi!=null) {
            endJi.next = null;
        }
        endOu.next = startJi;
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

}

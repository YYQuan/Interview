package src;

import java.util.HashMap;

public class LeetCode_2 {

    public static void main(String[] args) {
        LeetCode_2 code = new LeetCode_2();
        String pattern = "foo";
        String s = "bar";

//        int[] ints = new int[]{3,1};
//        int[] ints = new int[]{3,5};
//        int[] ints = new int[]{1,4,3,2,5,2};
//        int[] ints = new int[]{1,2,3,4,5};
//        int[] ints = new int[]{2,1,3,5,6,4,7};
        int[] ints1 = new int[]{2,4,3};

        int[] ints2 = new int[]{5,6,4};

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


        ListNode result =  code.addTwoNumbers(head,head2);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    // 击败百分之22
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        HashMap<Integer,Integer> map = new HashMap<>();

        ListNode node1 = l1;
        int i = 0;
        while(node1!=null){
            map.put(i,node1.val);
            node1 = node1.next;
            i++;
        }

        ListNode node2 = l2;
        i=0;
        while(node2!=null){
            map.put(i,map.getOrDefault(i,0)+node2.val);

            node2 = node2.next;
            i++;
        }
        ListNode virtualHead = new ListNode();
        ListNode node =virtualHead;
        for(i = 0;;i++){
            if(map.keySet().contains(i)){

                int valueI = map.get(i);

                if(valueI>=10){
                    map.put(i+1,map.getOrDefault(i+1,0)+1);
                }

                node.next =new ListNode(valueI%10);
                node = node.next;


            }else{
                break;
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

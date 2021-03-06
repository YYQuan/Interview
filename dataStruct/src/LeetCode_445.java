package src;

import java.util.HashMap;
import java.util.LinkedList;

public class LeetCode_445 {

    public static void main(String[] args) {
        LeetCode_445 code = new LeetCode_445();
        String pattern = "foo";
        String s = "bar";

//        int[] ints = new int[]{3,1};
//        int[] ints = new int[]{3,5};
//        int[] ints = new int[]{1,4,3,2,5,2};
//        int[] ints = new int[]{1,2,3,4,5};
//        int[] ints = new int[]{2,1,3,5,6,4,7};

//        int[] ints1 = new int[]{7,2,4,3};
//        int[] ints2 = new int[]{5,6,4};

        int[] ints1 = new int[]{9,9};
        int[] ints2 = new int[]{9};
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

    // 击败 百分之10
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        LinkedList<Integer> list1 = new LinkedList<>();
        LinkedList<Integer> list2 = new LinkedList<>();

        ListNode node=  l1;
        while(node!=null){
            list1.addFirst(node.val);
            node = node.next;
        }


        ListNode node2 = l2;

        while(node2 !=null){
            list2.addFirst((node2.val));
            node2 = node2.next;

        }

        HashMap<Integer,Integer>  map = new HashMap<>();

        for(int i = 0 ;i<list1.size();i++){
            map.put(i,map.getOrDefault(i,0)+list1.get(i));
        }

        for(int i = 0 ;i<list2.size()||i<list1.size();i++){

            int value = map.getOrDefault(i,0)+((i<list2.size())?list2.get(i):0);
            if(value >=10){
                map.put(i+1,map.getOrDefault(i+1,0)+1);
            }
            map.put(i,value%10);
        }


        ListNode preNode =null;
        ListNode nodeResult =null;
        for(int i = 0 ;;i++){
            int value = map.getOrDefault(i,-1);
            if(value==-1){
                break;
            }

            if(nodeResult ==null) {
                nodeResult = new ListNode(value);
                continue;
            }

            ListNode tmpNode = new ListNode(value);
            preNode = nodeResult;
            nodeResult = tmpNode;
            nodeResult.next = preNode;
        }

        return  nodeResult;
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

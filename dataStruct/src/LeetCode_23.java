package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class LeetCode_23 {

    public static void main(String[] args) {
        LeetCode_23 code = new LeetCode_23();
//
//        int[] ints = new  int[]{1,1};
//        int[] ints1 = new  int[]{2,1};
//        int[] ints2 = new  int[]{3,1};

        int[] ints = new  int[]{1,4,5};
        int[] ints1 = new  int[]{1,3,4};
        int[] ints2 = new  int[]{2,6};
        ListNode head = new ListNode(ints[0]);
        ListNode node = head;
        for(int i = 1 ;i<ints.length;i++){
            node.next=new ListNode(ints[i]);
            node = node.next;
        }

        ListNode head1 = new ListNode(ints1[0]);
        ListNode node1 = head1;

        for(int i = 1 ;i<ints1.length;i++){
            node1.next=new ListNode(ints1[i]);
            node1 = node1.next;
        }
        ListNode head2 = new ListNode(ints2[0]);
        ListNode node2 = head2;

        for(int i = 1 ;i<ints2.length;i++){
            node2.next=new ListNode(ints2[i]);
            node2 = node2.next;
        }

        ListNode[] nodes = new ListNode[]{head,head1,head2};
//        ListNode result = code.mergeKLists2(nodes);
        ListNode result = code.mergeKLists3(nodes);

        while(result!=null) {
            System.out.println(result.val+"-");
            result=result.next;
        }
    }


    // 击败 34
    public ListNode mergeKLists(ListNode[] lists) {

        PriorityQueue<ComparableListNode> queue = new PriorityQueue<>();
        for(ListNode node :lists){

            while(node!=null){
                queue.add(new ComparableListNode(node));
                node = node.next;
            }

        }
        ListNode result = new ListNode();
        if(!queue.isEmpty()) {
            result.next = queue.peek().node;
        }
        while(!queue.isEmpty()){
            ListNode node = queue.poll().node;

            if(!queue.isEmpty()) {
                node.next = queue.peek().node;
            }else{
                node.next= null;
            }
        }

        return result.next;
    }




    public static class ComparableListNode  implements Comparable<ComparableListNode>{
        ListNode  node ;

        public ComparableListNode(ListNode node) {
            this.node = node;
        }

        @Override
        public int compareTo(ComparableListNode o) {
            return node.val-o.node.val;
        }

    }
    public static class ListNode  {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }


    }

    public ListNode mergeKLists2(ListNode[] lists) {

        if(lists == null ||lists.length==0 ) return  null;
        if(lists.length ==1) return  lists[0];

        ListNode node = lists[0];
        for(int i = 1 ; i<lists.length;i++){
            ListNode node1 = lists[i];
            node = mergeTwo(node,node1);
        }

        return node;

    }

    public ListNode  mergeTwo(ListNode l1 ,ListNode l2 ){
        ListNode  visualHead = new ListNode();
        ListNode  node = visualHead;
        ListNode node1 = l1;
        ListNode node2 = l2;

        while(node1!=null||node2!=null){

            if(node1==null){
                node.next = node2;
                node = node2;
                node2 = node2.next;
                continue;
            }

            if(node2 ==null){
                node.next = node1;
                node = node1;
                node1 = node1.next;
                continue;
            }

            if(node1.val<node2.val){
                node.next = node1;
                node = node1;
                node1 = node1.next;
            }else{
                node.next = node2;
                node = node2;
                node2 = node2.next;
            }


        }

        return visualHead.next;

    }




    //
    public ListNode mergeKLists3(ListNode[] lists) {


        PriorityQueue<PriorityListNode> queue  = new PriorityQueue();

        for(ListNode node : lists){

            while(node !=null){
                queue.add(new PriorityListNode(node));
                node =node.next;

            }
        }

        ListNode visualHead = new ListNode();
        ListNode node = visualHead;

        while(!queue.isEmpty()){


            node.next = queue.poll().node;

            node = node.next;

        }
        node.next =null;

        return visualHead.next;
    }

    class PriorityListNode implements  Comparable<PriorityListNode> {

        ListNode node;

        public PriorityListNode(ListNode node) {
            this.node = node;
        }

        @Override
        public int compareTo(PriorityListNode o) {
            return node.val-o.node.val;
        }
    }






}


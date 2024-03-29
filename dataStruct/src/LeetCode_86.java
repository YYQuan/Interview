package src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class LeetCode_86 {

    public static void main(String[] args) {
        LeetCode_86 code = new LeetCode_86();
        String pattern = "foo";
        String s = "bar";

//        int[] ints = new int[]{3,1};
//        int[] ints = new int[]{3,5};
        int[] ints = new int[]{1,4,3,2,5,2};
//        int[] ints = new int[]{};
        ListNode head = new ListNode(ints[0]);
        ListNode node = head;
        for( int i = 1; i<ints.length ;i++){
            node.next = new ListNode(ints[i]);
            node = node.next;
        }


        ListNode result =  code.partition2(head,3);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    // 击败 百分之12
    public ListNode partition(ListNode head, int x) {

        // 根据head 找到  第一个 大于等于x 的节点 a
        // 从a开始找把 小于x的节点都断开  并且保存起来


        ListNode virtualHead = new ListNode(x-1);
        virtualHead.next = head;


        ListNode  newHead = null;


        ListNode preNode = virtualHead;
        ListNode node = virtualHead.next;
        while(node!=null){


            if(newHead==null&&node.val>=x){
                //找到 拼接点
                newHead = preNode;
            }

            if(node.val<x&&newHead!=null){
                preNode.next  = node.next;

                ListNode tmpNode =newHead.next;
                newHead.next = node;
                node.next  = tmpNode;

                newHead = node;

            }
            preNode = node;
            node = node.next;
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


    public ListNode partition2(ListNode head, int x) {
        if(head ==null) return null;
        ListNode virtualHead = new ListNode();
        virtualHead.val = Integer.MIN_VALUE;
        virtualHead.next=head;
        Queue<ListNode> minQueue = new LinkedList<>();
        Queue<ListNode> maxQueue = new LinkedList<>();

        ListNode node = virtualHead.next;
        while(node !=null){

            if(node.val >= x){
                maxQueue.add(node);
            }else{
                ((LinkedList<ListNode>) minQueue).add(node);
            }
            node  =node.next;
        }
        node = virtualHead;
        while(!minQueue.isEmpty()){
            node.next = ((LinkedList<ListNode>) minQueue).removeFirst();
            node = node.next;
        }

        while(!maxQueue.isEmpty()){
            node.next = ((LinkedList<ListNode>) maxQueue).removeFirst();
            node = node.next;
        }
        node.next =null;

        return virtualHead.next;

    }

}

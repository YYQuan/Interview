package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_82 {

    public static void main(String[] args) {
        LeetCode_82 code = new LeetCode_82();

//        int[] ints1 = new int[]{1,1,1,2,3,4,5,6};
//        int[] ints1 = new int[]{1,1,2,2,3,3,4,5,6};
        int[] ints1 = new int[]{1,2,3,3,4,4,5};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }



        ListNode result =  code.deleteDuplicates(head);





        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }

    }

    // 击败 百分之8
    public ListNode deleteDuplicates(ListNode head) {

        HashMap<Integer,NodeElement> map = new HashMap<>();
        ListNode virtual = new ListNode();
        virtual.next = head;

        ListNode preNode = virtual;
        ListNode node   = head;
        while(node!=null){

            if(map.keySet().contains(node.val)){
                NodeElement element = map.get(node.val);

                //移除掉当前node
                preNode.next = node.next;
                node = node.next;

                //移除掉首次添加 node
                if(element!=null&&!element.isRemoveFirst()) {
                    element.setRemoveFirst(true);
                    // 移除掉 先前的node  分 两种情况
                    // 1. 不影响当前 node
                    // 2. 影响当前node
                    if(element.node ==  preNode){
                        element.preNode.next = node;
                        element.node = node;

                        preNode = element.preNode;
                        node = element.node;
                    }else{
                        element.preNode.next = node;
                        element.node = node;

                    }
                }



                continue;
            }else{
                // 不需要移除
                map.put(node.val,new NodeElement(preNode,node));

                preNode = node;
                node  = node.next;
            }



        }

        return virtual.next;

    }

    static class NodeElement {
        boolean  isRemoveFirst = false;
        ListNode preNode;
        ListNode node;

        public NodeElement( ListNode preNode, ListNode node) {
            this.preNode = preNode;
            this.node = node;
        }

        public boolean isRemoveFirst() {
            return isRemoveFirst;
        }

        public void setRemoveFirst(boolean removeFirst) {
            isRemoveFirst = removeFirst;
        }
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

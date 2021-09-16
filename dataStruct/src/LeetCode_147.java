package src;

public class LeetCode_147 {

    public static void main(String[] args) {
        LeetCode_147 code = new LeetCode_147();

//        int[] ints1 = new int[]{1,2};
//        int[] ints1 = new int[]{1,2,3};
        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        ListNode result =  code.insertionSortList2(head);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }

    //击败百分之98
    public ListNode insertionSortList(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode virtualHead = new ListNode(Integer.MIN_VALUE);
        virtualHead.next = head;


        ListNode preNode = virtualHead;
        ListNode node = head;
        ListNode nextNode = head.next;

        // 第一步找到  node < pre 的node   》》》》重点
        // 断开 node
        // 从头开始找  第一个大于node的点  插入到前面
        // 移动 preNode  、node 、next 对于要移动的点 有个隐含逻辑就是 pre 大于 itself
        q:while(node!=null){



            while(node.val>=preNode.val){
                if(nextNode==null) break q;
                nextNode = nextNode.next;
                node = node.next;
                preNode = preNode.next;
            }


            preNode.next = nextNode;

            ListNode tmpPre = virtualHead;
            ListNode tmp = virtualHead.next;
            while(tmp.val<=node.val){

                tmpPre = tmpPre.next;
                tmp = tmp.next;
            }

            tmpPre.next = node;
            node.next = tmp;

            preNode = preNode;
            node = preNode.next;

            if(node==null) break q;
            nextNode = node.next;

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


    public ListNode insertionSortList2(ListNode head) {
        if(head ==null) return head;

        ListNode virtualHead = new ListNode();


        ListNode virtualCurrentHead = new ListNode();
        virtualHead.next= virtualCurrentHead;
        virtualHead.val= Integer.MIN_VALUE;

        virtualCurrentHead.next= head;
        virtualCurrentHead.val= Integer.MIN_VALUE;

        // 两串数据   要不能相互影响的话，那么一个尾部是head， 一个头部是head 所以加了两个头
        ListNode pNode = virtualHead;
        ListNode cNode = virtualCurrentHead;
        ListNode nNode = head;



        ListNode pN = head;
        ListNode n = head.next;
        ListNode nN = head.next==null?null:head.next.next;

        while(n!=null){

            while(n!=null&&pN.val<=n.val){
                pN = pN.next;
                n = n.next;
                nN =nN==null?null:nN.next;
            }

            if(n == null ) break;

            while(cNode!=null&&n.val>cNode.val){
                pNode = pNode.next;
                cNode = cNode.next;
                nNode = nNode==null?null:nNode.next;
            }
            pNode.next = n;
            n.next = cNode;
            pN.next= nN;
            n =nN;
            nN = (nN==null?null:nN.next);
            pNode = virtualHead;
            cNode = virtualCurrentHead;
            nNode = head;
        }
        return virtualCurrentHead.next;

    }
}

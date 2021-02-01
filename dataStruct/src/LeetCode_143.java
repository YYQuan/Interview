package src;

public class LeetCode_143 {

    public static void main(String[] args) {
        LeetCode_143 code = new LeetCode_143();

//        int[] ints1 = new int[]{1,2};
//        int[] ints1 = new int[]{1,2,3,4};
        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};

        ListNode head = new ListNode(ints1[0]);
        ListNode node = head;
        for( int i = 1; i<ints1.length ;i++){
            node.next = new ListNode(ints1[i]);
            node = node.next;
        }
        ListNode result =head;
                code.reorderList(head);
        while(result !=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }


    // 击败 80
    // 核心先 保存起来   方便操作每一个 节点
    public void reorderList(ListNode head) {

        if(head == null || head.next ==null) return ;
        ListNode node =head;
        int i = 0 ;
        for(i = 0 ;node.next!=null;i++){
            node=node.next;
        }
        node =head;
        ListNode[]  nodes = new ListNode[i+1];
        for(int j = 0 ; j<=i;j++){
            nodes[j] = node;
            node = node.next;
        }

        for(int j = 0 ; j<=(i+1)/2;j++)    {

            if(nodes[j].next == nodes[i-j]) {
                nodes[j+1].next = null;
                break;
            }

            nodes[j].next = nodes[i-j];
            nodes[i-j].next = nodes[j+1];

            if(j == (i+1)/2&& (i+1)%2==1){
                nodes[j+1].next.next = null;
            }

        }
        return ;

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

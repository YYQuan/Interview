package src;

import src.TreeNodeUtil.TreeNode;

import java.util.LinkedList;
import java.util.TreeSet;

public class LeetCode_230 {

    public static void main(String[] args) {
        LeetCode_230 code = new LeetCode_230();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
//        Integer[] ints = new Integer[]{5,3,6,2,4,null,7};
//        int[] ints = new int[]{1,2,3,4,5,6,7,8,9};
        Integer[] ints = new Integer[]{3,1,4,null,2};
//        Integer[] ints = new Integer[]{1,null,2};


        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        int result = code.kthSmallest(head,1);
        int result2 = code.kthSmallest2(head,1);
        System.out.println(result);
        System.out.println(result2);
//        TreeNodeUtil.printOrderTree(result);
    }

    /**
     *
     * @param root
     * @param k
     * @return
     */
    public int kthSmallest(TreeNode root, int k) {

        int count = getNodeMinCount(root.left);

        if(count+1 == k )  return root.val;

        // 这里很关键  count 是大于 等于 k 不是大于
        // 等于k 并不是 第K小， 只有 root.left 才能 表示小
        else if(count>=k) return kthSmallest(root.left,k);
        else return kthSmallest(root.right,k-count-1);


    }

    public  int getNodeMinCount(TreeNode node){

        if(node == null) return 0;

        return getNodeMinCount(node.left)+getNodeMinCount(node.right)+1;

    }


    public int kthSmallest2(TreeNode root, int k) {

        if(root == null)  return 0 ;
        TreeSet<TreeNodeContain> nodeTreeSet = new TreeSet<>();


        LinkedList<TreeNode> nodes = new LinkedList<>();

        nodes.add(root);
        while(!nodes.isEmpty()){
            int size = nodes.size();

            while(size>0){
                size--;
                TreeNode  node = nodes.removeFirst();
                nodeTreeSet.add(new TreeNodeContain(node));
                if (node.left!=null) nodes.addLast(node.left);
                if(node.right!=null) nodes.addLast(node.right);
            }


        }


        int count = k;
        TreeNodeContain contain  = null;
        while(count>0){
            count--;
            contain =nodeTreeSet.pollFirst();
        }

        if(contain!=null){
            return contain.node.val;
        }

        return 0;

    }

    class TreeNodeContain implements Comparable{
        TreeNode node;

        public TreeNodeContain(TreeNode node) {
            this.node = node;
        }

        @Override
        public int compareTo(Object o) {
            TreeNodeContain  contain = (TreeNodeContain)o ;
            return this.node.val - contain.node.val;
        }
    }


}

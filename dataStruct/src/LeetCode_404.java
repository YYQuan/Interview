package src;

import java.util.LinkedList;

public class LeetCode_404 {

    public static void main(String[] args) {
        LeetCode_404 code = new LeetCode_404();
//        TreeNode node3 =new TreeNode(3);
//        TreeNode node20 =new TreeNode(20);
//        node3.left = new TreeNode(9);
//        node3.right = node20;
//        node20.left = new TreeNode(15);
//        node20.right = new TreeNode(7);

        TreeNode node6 = new TreeNode(6);
        TreeNode node5 = new TreeNode(5);
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        TreeNode node21 = new TreeNode(21);
        TreeNode node22 = new TreeNode(22);


        node2.right = node3;
        node3.right =node4;
        node4.right =node5;
        node5.right =node6;
        node4.left = node21;
        node6.left = node22;


        System.out.print(code.sumOfLeftLeaves(node2));
    }

    // 击败 百分之 14
    // 核心 判断自己的左节点是不是叶子节点 分情况处理
    public int sumOfLeftLeaves(TreeNode root) {

        if(root == null) return 0;
        if(root.left ==null &&root.right==null) return root.val;

        LinkedList<TreeNode>  queue = new LinkedList<>();
        queue.add(root);

        int sum = 0;
        while(!queue.isEmpty()){

            TreeNode node =queue.poll();

            if(isLeftSonNodeBottom(node)){
                sum+=node.left.val;
                if(node.right!=null) {
                    queue.add(node.right);
                }
            }else{
                if(node.left!=null){
                    queue.add(node.left);
                }

                if(node.right!=null){
                    queue.add(node.right);
                }
            }

        }
        return sum;

    }

    //  该节点的左子节点 是不是叶子节点
    boolean isLeftSonNodeBottom(TreeNode node){

        if(node == null ) return false;

        if(node.left == null) return false;

        return node.left.left ==null &&node.left.right==null;

    }

    public static  class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
    }



}

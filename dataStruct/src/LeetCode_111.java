package src;

public class LeetCode_111 {

    public static void main(String[] args) {
        LeetCode_111 code = new LeetCode_111();
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

        node2.right = node3;
        node3.right =node4;
        node4.right =node5;
        node5.right =node6;


        System.out.print(code.minDepth(node2));
    }

    // 击败 45
    // 要点 对于 null 节点的深度是不能算的
    public int minDepth(TreeNode root) {

        if(root == null) return 0 ;
        if(root.left == null&& root.right ==null) return 1;

        return minDepth(root,0);


    }
    public int minDepth(TreeNode root,int depth) {

        if(root == null) return Integer.MAX_VALUE;
        if(root.left==null&&root.right==null) return depth+1;

        int depthLeft = depth+1;
        int depthRight = depth+1;
        depthLeft= minDepth(root.left,depth+1);
        depthRight = minDepth(root.right,depth+1);



        return  Math.min(depthLeft,depthRight);
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

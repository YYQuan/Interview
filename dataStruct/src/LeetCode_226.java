package src;

public class LeetCode_226 {

    public static void main(String[] args) {
        LeetCode_226 code = new LeetCode_226();
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

        code.invertTree(node2);
        System.out.print(code.invertTree(node2));
        System.out.print(code.invertTree(node2));
    }

    // 击败100
    // 可以准备去google啦
    public TreeNode invertTree(TreeNode root) {

        if(root==null) return root;

        invertTree(root.left);
        invertTree(root.right);



        TreeNode node = root.left;
        root.left = root.right;
        root.right = node;


        return root;



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

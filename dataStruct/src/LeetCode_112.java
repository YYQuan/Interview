package src;

import java.util.LinkedList;

public class LeetCode_112 {

    public static void main(String[] args) {
        LeetCode_112 code = new LeetCode_112();

        TreeNode node6 = new TreeNode(6);
        TreeNode node5 = new TreeNode(5);
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);

        node2.right = node3;
        node3.right =node4;
        node4.right =node5;
        node5.right =node6;
    }

    //击败100
    public boolean hasPathSum(TreeNode root, int targetSum) {

        return hasPathSum(root,0,targetSum);
    }
    public boolean hasPathSum(TreeNode root, int currentSum ,int targetSum) {

        if(root == null )  return false;
        if(root.left==null&&root.right==null){
            return (currentSum +root.val)== targetSum;
        }

        boolean resultLeft = hasPathSum(root.left,currentSum+root.val,targetSum);
        boolean resultRight = hasPathSum(root.right,currentSum+root.val,targetSum);

        return resultLeft|resultRight;
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


    public boolean hasPathSum2(TreeNode root, int targetSum) {

        return solution(root,targetSum);
    }
    public boolean solution(TreeNode root, int targetSum) {
        if(root == null){
            return false;
        }

        if(root.left==null &&root.right ==null){
            return (targetSum-root.val)==0;
        }

        if(solution(root.left,targetSum -root.val)){
            return true;
        }
        if(solution(root.right,targetSum -root.val)){
            return true;
        }

        return false;
    }


}

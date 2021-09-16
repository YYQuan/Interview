package src;

import java.util.PriorityQueue;
import  src.TreeNodeUtil.TreeNode;

public class LeetCode_104 {

    public static void main(String[] args) {
        LeetCode_104 code = new LeetCode_104();
        TreeNode node3 =new TreeNode(3);
        TreeNode node20 =new TreeNode(20);
        node3.left = new TreeNode(9);
        node3.right = node20;
        node20.left = new TreeNode(15);
        node20.right = new TreeNode(7);
        System.out.println(code.maxDepth(node3));
        System.out.println(code.maxDepth2(node3));
    }

    // 击败 100
    public int maxDepth(TreeNode root) {
        return maxDepth(root,0);
    }

    public int maxDepth(TreeNode root,int currentDepth) {

        if(root==null) return currentDepth;
        if(root.left==null&&root.right==null){
            return currentDepth+1;
        }
        int depthLeft = currentDepth;
        int depthRight = currentDepth;
        if(root.left!=null) {
            depthLeft = maxDepth(root.left, currentDepth + 1);
        }
        if(root.right!=null) {
            depthRight = maxDepth(root.right, currentDepth + 1);
        }

        return Math.max(depthLeft,depthRight);
    }





    public int maxDepth2(TreeNode root) {
        if(root ==null) return 0;

        if(root.left == null &&root.right == null) return 1;

        return Math.max(maxDepth2(root.right)+1,maxDepth2(root.left)+1);

    }

}

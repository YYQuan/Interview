package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import src.TreeNodeUtil.TreeNode;
public class LeetCode_110 {

    public static void main(String[] args) {
        LeetCode_110 code = new LeetCode_110();
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

//        code.invertTree(node2);
//        System.out.print(code.invertTree(node2));
//        System.out.print(code.invertTree(node2));
    }


    // 击败 7
    // 时间复杂度 是  O（n log n）
    public boolean isBalanced(TreeNode root) {


        if(root == null) return true;
        LinkedList<TreeNode> queue = new LinkedList<>();

        queue.add(root);

        while(!queue.isEmpty()){
            TreeNode tmpNode =queue.poll();

            int lDepth  = depth(tmpNode.left,0);
            int rDepth  = depth(tmpNode.right,0);

            if(Math.abs(lDepth-rDepth)>1){
                return false;
            }
            if(tmpNode.left!=null) {
                queue.add(tmpNode.left);
            }
            if(tmpNode.right!=null) {
                queue.add(tmpNode.right);
            }
        }

        return true;
    }

    public int depth(TreeNode node,int depth){

        if(node == null) return depth;

        if(node.left == null&&node.right ==null) return depth+1;

        int depthLeft =depth(node.left,depth+1);
        int depthRight =depth(node.right,depth+1);


        return Math.max(depthLeft,depthRight);

    }

    public boolean isBalanced2(TreeNode root) {

        if(root == null)  return true;

        if((Math.abs(getDepth(root.left) - getDepth(root.right))<=1)
                &&isBalanced2(root.left)
                &&isBalanced2(root.right)){
            return true;
        }else{
            return false;
        }




    }
    public int getDepth(TreeNode root) {

        if(root == null) return 0;

        if(root.left ==null && root.right==null) return 1;


        return Math.max(getDepth(root.left),getDepth(root.right))+1;



    }


}

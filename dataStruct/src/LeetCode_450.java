package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_450 {

    public static void main(String[] args) {
        LeetCode_450 code = new LeetCode_450();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
        Integer[] ints = new Integer[]{5,3,6,2,4,null,7};


        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        TreeNode result = code.deleteNode(head,5);
//        int result = code.minDiffInBST(head);
        System.out.println(result);
        TreeNodeUtil.printOrderTree(result);
    }

    public TreeNode deleteNode(TreeNode root, int key) {

        if(root == null) return root;

        LinkedList<StructNode>  nodes = new LinkedList<>();
        nodes.add(new StructNode(null,root));
        //先找到 key 节点
        // 用层序遍历 来找到 key节点
        while(!nodes.isEmpty()){

            StructNode structNode = nodes.poll();
            TreeNode  node = structNode.currentNode;
            if((node) == null) break;
            if(node.val == key){
                boolean isLeft = false;
                if(structNode.parentNode==null) {

                    findMinNode(node.right).left = node.left;
                    return node.right;
                }else {
                    if (structNode.parentNode.left != null && structNode.parentNode.left.val == key) {
                        isLeft = true;
                    }
                }



                if (node.right!=null) {
                    TreeNode minNode = findMinNode(node.right);
                    minNode.left = node.left;
                    if(isLeft) {
                        structNode.parentNode.left =node.right;
                    }else{
                        structNode.parentNode.right =node.right;
                    }
                }else{
                    if(isLeft) {
                        structNode.parentNode.left =node.left;
                    }else{
                        structNode.parentNode.right =node.left;
                    }
                }


            }else{
                nodes.add(new StructNode(node,node.left));
                nodes.add(new StructNode(node,node.right));
            }




        }
        return root;



    }

    TreeNode findMinNode (TreeNode node ){
        TreeNode result = node;
        while(result!=null&&result.left!=null){
            result = result.left;
        }
        return result;
    }
    static class StructNode {
        TreeNode  parentNode;
        TreeNode currentNode;

        public StructNode(TreeNode parentNode, TreeNode currentNode) {
            this.parentNode = parentNode;
            this.currentNode = currentNode;
        }
    }



}

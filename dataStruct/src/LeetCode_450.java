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
        TreeNode head2 = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

        TreeNode result2 = code.deleteNode2(head2,5);
        TreeNode result = code.deleteNode(head,3);
//        int result = code.minDiffInBST(head);
//        System.out.println(result);
        TreeNodeUtil.printOrderTree(result);
        TreeNodeUtil.printOrderTree(result2);
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


    public TreeNode deleteNode2(TreeNode root, int key) {

        if(root == null ) return null;

        LinkedList<TreeNode> nodes = new LinkedList<>();
        nodes.add(root);

        A:while(!nodes.isEmpty()){
            int size = nodes.size();

            B:for(int i = 0 ; i<size  ;i++){
                TreeNode node = nodes.removeFirst();

                if(node.left!=null) {
                    if(node.left.val==key){
                        TreeNode replaceNode = findReplaceNode(node);
                        node.left=replaceNode;

                        if(node == root ) return replaceNode;
                        break A;
                    }else {
                        nodes.addLast(node.left);
                    }
                }
                if(node.right!=null){
                    if(node.right.val==key){

                        TreeNode replaceNode = findReplaceNode(node);
                        node.right = replaceNode;
                        if(node == root ) return replaceNode;
                        break A;
                    }else {
                        nodes.addLast(node.right);
                    }
                }
            }
        }

        return root;

    }


    TreeNode findReplaceNode(TreeNode node){

        if(node == null) return null;
        if(node.left == null&&node.right==null)  return null;
        if(node.right == null){
            TreeNode result = node.left;
            node.left = null;
            return result;
        }else{
            TreeNode p = node;
            TreeNode resultNode = node.right;

            while(resultNode.left!=null){
                p = resultNode;
                resultNode = resultNode.left;
            }

            p.left = null;
            return resultNode;
        }
    }

}

package src;

import src.TreeNodeUtil.TreeNode;

import java.util.Arrays;
import java.util.LinkedList;

public class LeetCode_450 {

    public static void main(String[] args) {
        LeetCode_450 code = new LeetCode_450();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
//        Integer[] ints = new Integer[]{5,3,6,2,4,null,7};
//        Integer[] ints = new Integer[]{2,0,33,null,1,25,40,null,null,11,31,34,45,10,18,29,32,null,36,43,46,4,null,12,24,26,30,null,null,35,39,42,44,null,48,3,9,null,14,22,null,null,27,null,null,null,null,38,null,41,null,null,null,47,49,null,null,5,null,13,15,21,23,null,28,37,null,null,null,null,null,null,null,null,8,null,null,null,17,19,null,null,null,null,null,null,null,7,null,16,null,null,20,6};
//        Integer[] ints = new Integer[]{2,0,33,null,1,25,40,null,null,11,31,34,45,10,null};
        Integer[] ints = new Integer[] {2,0,33,null,1,25,40,null,null,11,31,34,45,10,18,29,32,null,36,43,46,4,null,12,24,26,30,null,null,35,39,42,44,null,48,3,9,null,14,22,null,null,27,null,null,null,null,38,null,41,null,null,null,47,49,null,null,5,null,13,15,21,23,null,28,37,null,null,null,null,null,null,null,null,8,null,null,null,17,19,null,null,null,null,null,null,null,7,null,16,null,null,20,6};
        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
        TreeNode head2 = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        TreeNode result = code.deleteNode(head,33);
        TreeNode result2 = code.deleteNode2(head2,33);
//        int result = code.minDiffInBST(head);
//        System.out.println(result);
        TreeNodeUtil.printOrderTree(result);
        System.out.println("----");
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


    // 注意题目  题目只是 要 保持二叉搜索树，  并不是完全二叉搜索树  要保持成 完成二叉搜索树
    // 所以只需要把 被移除节点 的 左子树  移到 右子树的最小节点的左边 就能保持 为二叉搜索树了。
//    public TreeNode deleteNode2(TreeNode root, int key) {
//        if(root == null) return null;
//        LinkedList<TreeNode> queue = new LinkedList<>();
//
//        TreeNode virtualNode = new TreeNode(key-1);
//        virtualNode.left = root;
//
//
//        queue.add(virtualNode);
//
//        while(!queue.isEmpty()){
//
//            int size = queue.size();
//
//            for(int i = 0 ; i<size;i++){
//
//                TreeNode node = queue.removeFirst();
//
//                if(node.left !=null) {
//                    if (node.left.val == key) {
//                        moveMin(new TreeNode[]{node,node.left},true);
//                        return virtualNode.left;
//
//                    }
//                    queue.addLast(node.left);
//                }
//
//                if(node.right !=null) {
//                    if (node.right.val == key) {
//                        moveMin(new TreeNode[]{node,node.right},false);
//                        return virtualNode.left;
//
//                    }
//                    queue.addLast(node.right);
//
//                }
//            }
//        }
//
//        return virtualNode.left;
//
//    }


   void moveMin(TreeNode[] roots ,boolean isleft){

        TreeNode[] result = new TreeNode[2];

        if(roots == null||roots[0]==null||roots[1]==null) return ;



        if(roots[1].right == null ){
            // 最小节点就是 被移除节点的左节点
            if(isleft){
                roots[0].left = roots[1].left;
            }else{
                roots[0].right = roots[1].left;
            }
            return ;
        }


       TreeNode parent = roots[1];
       TreeNode node   = roots[1].right;// 找被移除节点的最小节点



       while (node.left != null) {
           parent = node;
           node = node.left;
       }

       result[0] = parent;
       result[1] = node;

       node.left = roots[1].left;


       if(isleft) {
           roots[0].left = roots[1].right;
       }else {
           roots[0].right = roots[1].right;
       }


       return ;


    }

}

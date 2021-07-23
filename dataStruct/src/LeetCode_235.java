package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_235 {

    public static void main(String[] args) {
        LeetCode_235 code = new LeetCode_235();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};


        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(4));
        TreeNode  result2 = code.lowestCommonAncestor2(head,new TreeNode(2),new TreeNode(4));
//        int result = code.minDiffInBST(head);
        System.out.println(result.val);
        System.out.print(result2.val);
    }

    // 击败 11
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        TreeNode  result = null;
        TreeNode  node = root;

        LinkedList<TreeNode >  list = new LinkedList<>();
        list.add(node);
        while(!list.isEmpty()) {
            TreeNode tmpNode = list.poll();
            if (findNode(tmpNode, p) && findNode(tmpNode, q)) {
               result = tmpNode;
               list.add(tmpNode.left);
               list.add(tmpNode.right);
            }
        }

        return result;

    }


    boolean  findNode(TreeNode node , TreeNode target){

        TreeNode tmp = node;
        LinkedList<TreeNode> list = new LinkedList<>();
        list.add(tmp);
        while(!list.isEmpty()){

            TreeNode nodeTMP =  list.poll();

            if(nodeTMP ==null)  continue;
            if(nodeTMP.val == target.val) return true;

            list.add(nodeTMP.left);
            list.add(nodeTMP.right);

        }

        return false;
    }


    // 核心思路  根据 二叉搜索树的特性， 第一个 在p.val 和q.val之间的 node.val 就是 他们的最近公共祖先
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {

        if(root == null) return null;
        if(p== null) return q;
        if(q== null) return p;
        int intP = p.val;
        int intq = q.val;

        TreeNode node = root;

        while(true){

            if(node == null) return null;

            if(node.val == intP) return p;
            if(node.val == intq) return q;

            if(node.val>intP &&node.val >intq)  {
                node = node.left;
            }else if(node.val<intP&&node.val<intq){
                node = node.right;
            }else{
                return node;
            }
        }


    }



}

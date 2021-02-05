package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_98 {

    public static void main(String[] args) {
        LeetCode_98 code = new LeetCode_98();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
        Integer[] ints = new Integer[]{-2147483648,null,2147483647};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        boolean result = code.isValidBST(head);
//        int result = code.minDiffInBST(head);
        System.out.print(result);
    }

    // 击败 百分之28
    // 中序遍历 后 是有序的那么就是 二分搜索树
    public boolean isValidBST(TreeNode root) {


        List<Long> ints  = orderTreeNode(root);

        if(ints.size()<=1) return true;

        for(int i = 1 ;i<ints.size();i++){
            if(ints.get(i)-ints.get(i-1)<=0) return false;
        }

        return true;
    }

    private List<Long>  orderTreeNode(TreeNode node){
        List<Long>  result = new ArrayList<>();

        if(node == null)  return result;

        orderTreeNode(node,result);

        return  result;

    }

    void  orderTreeNode(TreeNode node, List<Long> list){

        if(node == null ) return ;

        orderTreeNode(node.left,list);
        list.add(new Long(node.val));
        orderTreeNode(node.right,list);

    }



}

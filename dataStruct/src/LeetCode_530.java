package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class LeetCode_530 {

    public static void main(String[] args) {
        LeetCode_530 code = new LeetCode_530();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
//        Integer[] ints = new Integer[]{5,3,6,2,4,null,7};
//        int[] ints = new int[]{1,2,3,4,5,6,7,8,9};
//        Integer[] ints = new Integer[]{3,10,77,null,5};
        Integer[] ints = new Integer[]{236,104,701,null,227,null,911};
//        Integer[] ints = new Integer[]{1,null,2};


        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

        int result = code.getMinimumDifference(head);

        System.out.println(result);
//        TreeNodeUtil.printOrderTree(result);
    }


//    核心  二叉搜索树的中序遍历 会得到有序数组
    // 击败 29
    // 思路 中序遍历之后 得到有序的 数组 再对数组进行处理
    public int getMinimumDifference(TreeNode root) {

        ArrayList<Integer> values = middleOrder(root);

        int result = Integer.MAX_VALUE;
        for( int i =1;i<values.size();i++){
            result = Math.min(result , values.get(i)-values.get(i-1));
        }

        return result;
    }




    public ArrayList<Integer>  middleOrder(TreeNode root){
        ArrayList<Integer> result = new ArrayList<>();

        middleOrder(root,result);

        return result;

    }
    public void  middleOrder(TreeNode root,ArrayList<Integer> arrays){

        if(root == null ) return ;
        middleOrder(root.left,arrays);
        arrays.add(root.val);
        middleOrder(root.right,arrays);


    }



}

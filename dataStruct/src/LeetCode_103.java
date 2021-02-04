package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_103 {

    public static void main(String[] args) {
        LeetCode_103 code = new LeetCode_103();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
//        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node2.left = node3;
        node2.right = node4;
        TreeNode node1 = new TreeNode(1);
        node1.right= node2;
        List<List<Integer>> result =code.zigzagLevelOrder(node1);
        System.out.println(Arrays.toString(result.toArray()));

    }

    // 击败 98
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        LinkedList<List<Integer>>  result = new LinkedList<>();
        if(root ==null) return result;

        LinkedList<TreeNode>  list = new LinkedList<>();
        list.add(root);

        boolean isLeft2Right = true;
        while(list.size()>0){

            // 一次读一层
            int count = list.size();
            LinkedList<Integer> tmpList = new LinkedList<>();
            for(int i = 0 ; i<count ;i++){

                TreeNode node =list.removeFirst();
                if(node.left!=null) {
                    list.add(node.left);
                }
                if(node.right!=null) {
                    list.add(node.right);
                }
                if(isLeft2Right){
                    tmpList.addLast(node.val);
                }else{
                    tmpList.addFirst(node.val);
                }
            }
            isLeft2Right =!isLeft2Right;
            result.add(tmpList);
        }

        return result;
    }


    public static class TreeNode {
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

package src;

import javafx.util.Pair;

import java.util.LinkedList;

public class LeetCode_91 {

    public static void main(String[] args) {
        LeetCode_91 code = new LeetCode_91();

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
//        List<Integer> result =code.rightSideView(node1);

//        System.out.println(code.numSquares(13));
//        System.out.println(code.numSquares2(13));

    }
    //概念题难懂 先略过
//    public int numDecodings(String s) {
//
//    }

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

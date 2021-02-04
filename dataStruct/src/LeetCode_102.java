package src;

import java.util.*;

public class LeetCode_102 {

    public static void main(String[] args) {
        LeetCode_102 code = new LeetCode_102();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
//        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node2.left = node3;
        TreeNode node1 = new TreeNode(1);
        node1.right= node2;
        List<List<Integer>> result =code.levelOrder(node1);
        System.out.println(Arrays.toString(result.toArray()));

    }

    // 击败 94
    // 核心 用两个list来保存 来分行。
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result  = new ArrayList<>();
        LinkedList<TreeNode>  queueJi = new LinkedList<>();
        LinkedList<TreeNode>  queueOu = new LinkedList<>();


        if(root == null) return  result;

        queueOu.addFirst(root);



        while(!queueOu.isEmpty()||!queueJi.isEmpty()){
            List<Integer> listOu = new ArrayList<>();
            while(!queueOu.isEmpty()){
                TreeNode node = queueOu.removeFirst();
                listOu.add(node.val);
                if(node.left!=null) {
                    queueJi.add(node.left);
                }
                if(node.right!=null) {
                    queueJi.add(node.right);
                }

            }

            if(listOu.size()>0) {
                result.add(listOu);
            }
            List<Integer> listJi = new ArrayList<>();

            while(!queueJi.isEmpty()){
                TreeNode node = queueJi.removeFirst();
                listJi.add(node.val);
                if(node.left!=null) {
                    queueOu.add(node.left);
                }
                if(node.right!=null) {
                    queueOu.add(node.right);
                }

            }
            if(listJi.size()>0) {
                result.add(listJi);
            }
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

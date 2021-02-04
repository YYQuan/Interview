package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_107 {

    public static void main(String[] args) {
        LeetCode_107 code = new LeetCode_107();

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
        List<List<Integer>> result =code.levelOrderBottom(node1);
        System.out.println(Arrays.toString(result.toArray()));

    }

    // 击败 99
    public List<List<Integer>> levelOrderBottom(TreeNode root) {

        LinkedList<List<Integer>> result = new LinkedList<>();
        if(root==null) return result;
        LinkedList<TreeNode> list1 = new LinkedList<>();
        LinkedList<TreeNode> list2 = new LinkedList<>();

        list1.add(root);

        while(list1.size()>0||list2.size()>0){

            List<Integer> tmpList1 = new LinkedList<>();
            while(!list1.isEmpty()){

                TreeNode node = list1.removeFirst();
                tmpList1.add(node.val);
                if(node.left!=null){
                    list2.add(node.left);
                }
                if(node.right!=null){
                    list2.add(node.right);
                }

            }

            if(tmpList1.size()>0) {
                result.addFirst(tmpList1);
            }

            List<Integer> tmpList2 = new LinkedList<>();
            while(!list2.isEmpty()){

                TreeNode node = list2.removeFirst();
                tmpList2.add(node.val);
                if(node.left!=null){
                    list1.add(node.left);
                }
                if(node.right!=null){
                    list1.add(node.right);
                }

            }
            if(tmpList2.size()>0) {
                result.addFirst(tmpList2);
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

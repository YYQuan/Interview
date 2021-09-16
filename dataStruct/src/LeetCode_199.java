package src;

import java.util.*;

public class LeetCode_199 {

    public static void main(String[] args) {
        LeetCode_199 code = new LeetCode_199();

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
        List<Integer> result =code.rightSideView(node1);
        List<Integer> result2 =code.rightSideView2(node1);
        System.out.println(Arrays.toString(result.toArray()));
        System.out.println(Arrays.toString(result2.toArray()));

    }

    // 击败97
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result  = new LinkedList<>();
        if(root==null) return result;
        LinkedList<TreeNode>  queue = new LinkedList<>();

        queue.add(root);

        while(!queue.isEmpty()){

            int count = queue.size();
            int number = 0;
            for(int i  =0 ;i<count ;i++){
                TreeNode node = queue.removeFirst();
                number = node.val;
                if(node.left!=null) {
                    queue.add(node.left);
                }
                if(node.right!=null) {
                    queue.add(node.right);
                }
            }
            result.add(number);
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


    public List<Integer> rightSideView2(TreeNode root) {
        List<Integer>  result = new ArrayList<>();
        if(root == null ) return result;

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while(!queue.isEmpty()){
            int size = queue.size();
            while(size>0){
                TreeNode node = queue.removeFirst();
                if(node.left!=null) {
                    queue.addLast(node.left);
                }
                if(node.right!=null) {
                    queue.addLast(node.right);
                }

                size--;
                if(size ==0){
                    result.add(node.val);
                }
            }
        }

        return result;
    }
}

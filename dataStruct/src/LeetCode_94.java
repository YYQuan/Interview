package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_94 {

    public static void main(String[] args) {
        LeetCode_94 code = new LeetCode_94();
//        String s = "()(";
//        String s = "()[]{}";
//        String s = "/home/";
//        String s = "/../";
//        String s = "/home//foo/";
//        String s = "/a/./b/../../c/";
//        String s = "/.";
        String s = "/./";
//        String[] s = new String[]{"2","1","+","3","*"};
//        String[] s = new String[]{"4","13","5","/","+"};
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node2.left = node3;
        TreeNode node1 = new TreeNode(1);
        node1.right= node2;
        List<Integer> result =code.inorderTraversal(node1);
        System.out.println(Arrays.toString(result.toArray()));

    }

    // 击败100
    //  先用递归来解决
    public List<Integer> inorderTraversal(TreeNode root) {

        LinkedList<TreeNode> queue = new LinkedList<>();
        List<Integer> list = new ArrayList<>();
        if(root == null) return list;


        inorderTraversal(root,list);
       return list;
    }

    public void inorderTraversal(TreeNode root,List<Integer> list) {

        if(root==null) return ;


        inorderTraversal(root.left,list);
        list.add(root.val);
        inorderTraversal(root.right,list);



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

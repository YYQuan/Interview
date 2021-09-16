package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_145 {

    public static void main(String[] args) {
        LeetCode_145 code = new LeetCode_145();
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
        List<Integer> result =code.postorderTraversal(node1);
        System.out.println(Arrays.toString(result.toArray()));

    }

    public List<Integer> postorderTraversal(TreeNode root) {

        List<Integer> list = new ArrayList<>();

        if(root==null) return list;

        postorderTraversal(root,list);

        return list;
    }

    public void postorderTraversal(TreeNode root,List<Integer> list) {
        if(root == null) return;

        postorderTraversal(root.left,list);
        postorderTraversal(root.right,list);
        list.add(root.val);
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


    public List<Integer> postorderTraversal2(TreeNode root) {
        List<Integer> ints = new ArrayList<>();
        postorderTraversal2(root,ints);
        return ints;
    }
    public void  postorderTraversal2(TreeNode root,List<Integer> result) {

        if(root ==null){
            return ;
        }


        postorderTraversal(root.left,result);
        postorderTraversal(root.right,result);
        result.add(root.val);

    }
}

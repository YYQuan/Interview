package src;

import java.util.*;

public class LeetCode_144 {

    public static void main(String[] args) {
        LeetCode_144 code = new LeetCode_144();
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
        List<Integer> result =code.preorderTraversal(node1);
        System.out.println(Arrays.toString(result.toArray()));

    }

    // 击败 100
    public List<Integer> preorderTraversal(TreeNode root) {

        LinkedList<TreeNode> queue = new LinkedList<>();
        List<Integer> list = new ArrayList<>();
        if(root == null) return list;

        queue.add(root);

        while(!queue.isEmpty()){

            TreeNode node = queue.removeFirst();
            list.add(node.val);

            if(node.right!=null) {
                queue.addFirst(node.right);
            }
            if(node.left!=null) {
                queue.addFirst(node.left);
            }

        }

        return  list ;

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


    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> ints = new ArrayList<>();
        preorderTraversal(root,ints);
        return ints;
    }
    public void  preorderTraversal(TreeNode root,List<Integer> result) {

        if(root ==null){
            return ;
        }

        result.add(root.val);
        preorderTraversal(root.left,result);
        preorderTraversal(root.right,result);

    }
}

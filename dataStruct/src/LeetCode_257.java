package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_257 {

    public static void main(String[] args) {
        LeetCode_257 code = new LeetCode_257();
//        TreeNode node3 =new TreeNode(3);
//        TreeNode node20 =new TreeNode(20);
//        node3.left = new TreeNode(9);
//        node3.right = node20;
//        node20.left = new TreeNode(15);
//        node20.right = new TreeNode(7);

        TreeNode node6 = new TreeNode(6);
        TreeNode node5 = new TreeNode(5);
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        TreeNode node21 = new TreeNode(21);
        TreeNode node22 = new TreeNode(22);


        node2.right = node3;
        node3.right =node4;
        node4.right =node5;
        node5.right =node6;
        node4.left = node21;
        node6.left = node22;


        System.out.print(code.binaryTreePaths(node2));
    }

    // 击败 百分之8
    public List<String> binaryTreePaths(TreeNode root) {

        List<String> result = new ArrayList<>();

        if(root == null ) return result;
        binaryTreePaths(root, "",result);



        return result;


    }

    public void  binaryTreePaths(TreeNode root,String path,List<String> list) {
        if(root == null) return ;
        String s = path.length()>0?"->":"";
        if(root.left == null&& root.right == null) {


            list.add(path+s+root.val);
            return;
        }


        binaryTreePaths(root.left,path+s+root.val,list);
        binaryTreePaths(root.right,path+s+root.val,list);

    }

    public static  class TreeNode {
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

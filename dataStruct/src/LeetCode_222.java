package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_222 {

    public static void main(String[] args) {
        LeetCode_222 code = new LeetCode_222();
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

        node2.right = node3;
        node3.right =node4;
        node4.right =node5;
        node5.right =node6;

//        code.invertTree(node2);
        System.out.print(code.countNodes(node2));
        System.out.print(code.countNodes2(node2));
    }

    public int countNodes(TreeNode root) {


        List<Integer> ints = new ArrayList<>();
        order(root,ints);

        return ints.size();
    }

    // 直接遍历即可 击败 20
    // 也许有其他方便的解法 第一遍的时候 先略过
    public void order(TreeNode root ,List<Integer> list){

        if(root==null){
            return ;
        }

        list.add(root.val);
        order(root.left,list);
        order(root.right,list);

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


    public int countNodes2(TreeNode root) {

        if(root == null)  return 0;

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int result = 0;
        while(!queue.isEmpty()){

            int size = queue.size();
            result +=size;
            while(size>0){
                size--;
                TreeNode node = queue.remove();

                if(node.left!=null){
                    queue.add(node.left);
                }
                if(node.right!=null){
                    queue.add(node.right);
                }
            }


        }
        return result;
    }


}

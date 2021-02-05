package src;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_113 {

    public static void main(String[] args) {
        LeetCode_113 code = new LeetCode_113();
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


//        System.out.print(code.pathSum(node2));
    }

    // 击败 40
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();

        if(root == null)  return result;
        List<Integer> list = new ArrayList<>();
//        list.add(root.val);
        pathSum(root,result,list,targetSum);

        return result;

    }

    public void pathSum(TreeNode root, List<List<Integer>> result ,
                                       List<Integer> list ,int targetSum) {
        if(root == null )  return ;
        List<Integer> tmpList = new ArrayList<>(list);
        tmpList.add(root.val);
        if(root.left == null &&root.right == null) {
            if(root.val == targetSum){
//                list.add(root.val);
                result.add(tmpList);
            }
            return ;
        }

        pathSum(root.left,result,tmpList,targetSum-root.val);
        pathSum(root.right,result,tmpList,targetSum-root.val);



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

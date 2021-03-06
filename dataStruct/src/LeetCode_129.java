package src;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_129 {

    public static void main(String[] args) {
        LeetCode_129 code = new LeetCode_129();
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

    // 击败 28
    public int sumNumbers(TreeNode root) {

        if(root ==null ) return 0 ;

        List<Integer> list = new ArrayList<>();
         sumNumbers(root,0,list);

         int result = 0;
         for(int i : list){
             result+=i;
         }

         return result;
    }

    public void  sumNumbers(TreeNode root,int currentSum,List<Integer> list) {

        if(root ==null)  return ;
        if(root.left ==null&&root.right == null){
            int result = currentSum*10+root.val;
            list.add(result);
            return ;
        }

        sumNumbers(root.left,currentSum*10+root.val,list);
        sumNumbers(root.right,currentSum*10+root.val,list);
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

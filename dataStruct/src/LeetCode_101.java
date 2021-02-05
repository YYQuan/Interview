package src;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_101 {

    public static void main(String[] args) {
        LeetCode_101 code = new LeetCode_101();

    }

    // 击败 6
    public boolean isSymmetric(TreeNode root) {

        if(root == null)  return true;

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        r2lOrder(root,list1);
        l2rOrder(root,list2);

        for(int i = 0 ;i<list1.size();i++){

            if(list1.get(i)==null&&list2.get(i)==null){
                continue;
            }else if(list1.get(i)==null||list2.get(i)==null){
                return false;
            }else{
                if(list1.get(i).intValue()!=list2.get(i).intValue()){
                    return false;
                }

            }

        }

        return true;


    }


    public  void r2lOrder(TreeNode root ,List<Integer> list ){

        if(root==null){
            list.add(null);
            return ;
        }

        list.add(root.val);
        r2lOrder(root.right,list);
        r2lOrder(root.left,list);
    }
    public  void l2rOrder(TreeNode root ,List<Integer> list ){

        if(root==null){
            list.add(null);
            return ;
        }

        list.add(root.val);
        l2rOrder(root.left,list);
        l2rOrder(root.right,list);

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

package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_783 {

    public static void main(String[] args) {
        LeetCode_783 code = new LeetCode_783();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
        int result = code.minDiffInBST(head);
        System.out.print(result);
    }


    // 击败 30
    // 因为是 二分搜索树 所以 肯定是父子节点之间的差值最小  结果的最小值是1
    // 中序遍历 出来的 数组就是有序的了
    public int minDiffInBST(TreeNode root) {



        List<Integer> orderResult = order(root);
        if(orderResult.size()<2){
            throw new IllegalArgumentException();
        }

        int minVaule = Integer.MAX_VALUE;
        for(int i = 1 ;i<orderResult.size();i++){
            minVaule  = Math.min(Math.abs(orderResult.get(i)-orderResult.get(i-1)),minVaule);
        }

        return  minVaule;
    }

    public List<Integer> order(TreeNode node ){

        List<Integer> result = new ArrayList<>();


        order(node,result);
        return result;

    }

    public void order(TreeNode node ,List<Integer> list){
        if(node ==null)  return ;

        order(node.left,list);
        list.add(node.val);
        order(node.right,list);


    }


}

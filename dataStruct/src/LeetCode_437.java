package src;

import java.util.ArrayList;
import java.util.List;
import src.TreeNodeUtil.TreeNode;

public class LeetCode_437 {

    public static void main(String[] args) {
        LeetCode_437 code = new LeetCode_437();
        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};

        TreeNodeUtil.TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
        int result = code.pathSum(head,8);
        System.out.print(result);
    }

    // 击败11
    // 核心：怎么区分 包含自己这个节点的 和不包含自己节点的
    public int pathSum(TreeNode root, int sum) {

        if(root == null )  return  0;
        //  包含  root的 路径 的数量 加上 不包含 root的数量 等于总的
        return findPath(root, sum)+pathSum(root.left,sum)+pathSum(root.right,sum);

    }

    //包含 root的路径
    public int findPath(TreeNode root, int sum) {

        if(root == null) return 0 ;
        int result = 0;
        if(root.val == sum ){
           result = 1;
        }

        result += findPath(root.left,sum-root.val);
        result += findPath(root.right,sum-root.val);
        return result;

    }



}

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
        int result2 = code.pathSum2(head,8);
        System.out.println(result);
        System.out.println(result2);
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


    public int pathSum2(TreeNode root, int targetSum) {

        if(root == null) {
            return 0;
        }

        int result = 0 ;
        // 有隐藏逻辑， 不能跳着选中  所以包含本节点的 要单独处理
        result+= pathSum2(root.left,targetSum);
        result+= pathSum2(root.right,targetSum);
        result+= findPath2(root,targetSum);




        return result;
    }


    int  findPath2(TreeNode node ,int target){
        if(node ==null)  return 0 ;
        // 等于 0了不代表 可以跳出了  ，还是可以继续往下走的

        return (node.val == target?1:0)+
                findPath2(node.left,target-node.val)+
                findPath2(node.right,target-node.val) ;
    }


}

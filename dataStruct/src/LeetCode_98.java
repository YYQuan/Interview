package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_98 {

    public static void main(String[] args) {
        LeetCode_98 code = new LeetCode_98();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
//        Integer[] ints = new Integer[]{-2147483648,null,2147483647};
        Integer[] ints = new Integer[]{5, 4, 6, null, null, 3, 7};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        boolean result = code.isValidBST(head);
        boolean result2 = code.isValidBST2(head);
//        int result = code.minDiffInBST(head);
        System.out.println(result);
        System.out.println(result2);
    }

    // 击败 百分之28
    // 中序遍历 后 是有序的那么就是 二分搜索树
    public boolean isValidBST(TreeNode root) {


        List<Long> ints = orderTreeNode(root);

        if (ints.size() <= 1) return true;

        for (int i = 1; i < ints.size(); i++) {
            if (ints.get(i) - ints.get(i - 1) <= 0) return false;
        }

        return true;
    }

    private List<Long> orderTreeNode(TreeNode node) {
        List<Long> result = new ArrayList<>();

        if (node == null) return result;

        orderTreeNode(node, result);

        return result;

    }

    void orderTreeNode(TreeNode node, List<Long> list) {

        if (node == null) return;

        orderTreeNode(node.left, list);
        list.add(new Long(node.val));
        orderTreeNode(node.right, list);

    }

    // 用 普通递归的话， 得维护和上层节点的关系  所以还是用 中序遍历后， 看是否有序最简单
    public boolean isValidBST2(TreeNode root) {
        List<Integer> list = getList(root);

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) >= list.get(i)) return false;
        }

        return true;
    }

    public List<Integer> getList(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;
        getList(list, root);
        return list;

    }

    public void getList(List<Integer> result, TreeNode root) {

        if (root == null) return;
        getList(result, root.left);
        result.add(root.val);
        getList(result, root.right);

    }
}

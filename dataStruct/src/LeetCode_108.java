package src;

import src.TreeNodeUtil.TreeNode;

import java.util.LinkedList;

public class LeetCode_108 {

    public static void main(String[] args) {
        LeetCode_108 code = new LeetCode_108();
//        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
//        Integer[] ints = new Integer[]{5,3,6,2,4,null,7};
//        int[] ints = new int[]{1,2,3,4,5,6,7,8,9};
        int[] ints = new int[]{-10,-3,0,5,9};


//        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));
        TreeNode result = code.sortedArrayToBST(ints);
        TreeNode result2 = code.sortedArrayToBST2(ints);

        TreeNodeUtil.printOrderTree(result);
        TreeNodeUtil.printOrderTree(result2);
    }
    public static  TreeNode  sortedArrayToBST(int[] nums){
        if (nums == null||nums.length <=0) {
            throw new IllegalArgumentException();
        }

        return  getMiddleNode(nums,0,nums.length-1);

    }

    public static TreeNode  getMiddleNode(int[] ints,int l , int r ){
        if(l == r ){
            return new TreeNode(ints[l]);
        }


        int middle =l+ (r-l)/2;
        System.out.println("l "+l+" r "+r+" m "+middle);
        TreeNode result =  new TreeNode(ints[middle]);
        if(middle-1>=l) {
            result.left = getMiddleNode(ints, l, middle - 1);
        }
        if(middle+1<=r) {
            result.right = getMiddleNode(ints, middle + 1, r);
        }

        return result;



    }


    // 思路  ： 一直取中值， 从上到下的来构建链表
    public TreeNode sortedArrayToBST2(int[] nums) {
        if(nums == null ) return null;

        return solution(nums,0,nums.length-1);
    }

    public TreeNode solution(int[] nums,int l ,int r) {
        if(r == l) return new TreeNode(nums[l]);
        else if(r< l ) return  null;

        int middleIndex = l + (r-l)/2;
        TreeNode node = new TreeNode(nums[middleIndex]);

        node.left = solution(nums,l,middleIndex-1);
        node.right = solution(nums,middleIndex+1,r);

        return node;
    }

}

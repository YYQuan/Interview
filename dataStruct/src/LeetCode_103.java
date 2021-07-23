package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import src.TreeNodeUtil.TreeNode;

public class LeetCode_103 {

    public static void main(String[] args) {
        LeetCode_103 code = new LeetCode_103();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
//        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node2.left = node3;
        node2.right = node4;
        TreeNode node1 = new TreeNode(1);
        node1.right= node2;
        TreeNodeUtil.TreeNode node  =TreeNodeUtil.transferArrays2Tree(new Integer[]{1,2,3,4,null,null,5});

        List<List<Integer>> result =code.zigzagLevelOrder(node);
        List<List<Integer>> result2 =code.zigzagLevelOrder2(node);
        System.out.println(Arrays.toString(result.toArray()));
        System.out.println(Arrays.toString(result2.toArray()));

    }

    // 击败 98
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        LinkedList<List<Integer>>  result = new LinkedList<>();
        if(root ==null) return result;

        LinkedList<TreeNode>  list = new LinkedList<>();
        list.add(root);

        boolean isLeft2Right = true;
        while(list.size()>0){

            // 一次读一层
            int count = list.size();
            LinkedList<Integer> tmpList = new LinkedList<>();
            for(int i = 0 ; i<count ;i++){

                TreeNode node =list.removeFirst();
                if(node.left!=null) {
                    list.add(node.left);
                }
                if(node.right!=null) {
                    list.add(node.right);
                }
                if(isLeft2Right){
                    tmpList.addLast(node.val);
                }else{
                    tmpList.addFirst(node.val);
                }
            }
            isLeft2Right =!isLeft2Right;
            result.add(tmpList);
        }

        return result;
    }



    public List<List<Integer>> zigzagLevelOrder2(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();
        if(root == null) return result;

        List<Integer> list = new ArrayList<>();
        LinkedList<TreeNode> nodes  = new LinkedList<>();
        boolean isL2R = true;
        nodes.add(root);
        while(!nodes.isEmpty()){
            list.clear();

            int size = nodes.size();

            for( int i = 0 ; i<size ;i++){
                TreeNode node ;
                if(isL2R) {

                    node =nodes.removeFirst();
                    // 存都存成 从左往右 ， 取的时候 做区别
                    if (node.left != null) {
                        nodes.addLast(node.left);
                    }
                    if (node.right != null) {
                        nodes.addLast(node.right);
                    }
                }else{
                    //
                    node =nodes.removeLast();
                    if (node.right != null) {
                        nodes.addFirst(node.right);
                    }
                    if (node.left != null) {
                        nodes.addFirst(node.left);
                    }
                }
                list.add(node.val);
            }
            isL2R =!isL2R;
            result.add(new ArrayList<>(list));
        }
        return result;
    }

}

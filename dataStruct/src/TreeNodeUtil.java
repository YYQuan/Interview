package src;

import java.util.Arrays;
import java.util.LinkedList;

public class TreeNodeUtil {


    public static void main(String[] args) {
        Integer[] ints = new Integer[]{10,5,-3,3,2,null,11,3,-2,null,1};
        TreeNode result = transferArrays2Tree(ints);
        System.out.println(result);
        printOrderTree(result);
    }


    // 层序遍历
    public static void printOrderTree(TreeNode node ){

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(node);

        LinkedList<Integer>  list = new LinkedList<>();


        while(!queue.isEmpty()){

            TreeNode nodeTmp = queue.poll();
            if(nodeTmp!=null) {
                list.add(nodeTmp.val);
            }else{
                list.add(null);
            }

            if(nodeTmp!=null){
                queue.add(nodeTmp.left);
                queue.add(nodeTmp.right);
            }else{
//                queue.add(null);
//                queue.add(null);
            }


        }

        System.out.println(Arrays.toString(list.toArray()));



    }

    public static  TreeNode  transferArrays2Tree(Integer[] ints){

        LinkedList<Integer> intList = new LinkedList<>();
        for(Integer i : ints){
            intList.add(i);
        }

        LinkedList<TreeNode>  treeNodes = new LinkedList<>();
        TreeNode result = new TreeNode(intList.poll());
        treeNodes.add(result);

        while(!treeNodes.isEmpty()){


            TreeNode node = treeNodes.poll();



            if(intList.isEmpty()){
                break;
            }
            Integer intLeft =  intList.poll();
            if(intLeft==null){
                if(node!=null) {
                    node.left = null;
                }
                treeNodes.add(null);
            }else {
                TreeNode left = new TreeNode(intLeft);
                if(node!=null) {

                    node.left = left;
                }
                treeNodes.add(left);
            }


            if(intList.isEmpty()){
                break;
            }
            Integer intRight =  intList.poll();
            if(intRight==null){
                if(node!=null) {

                    node.right = null;
                }
                treeNodes.add(null);
            }else {
                TreeNode right = new TreeNode(intRight);
                if(node!=null) {
                    node.right = right;
                }
                treeNodes.add(right);
            }




        }

        return result;

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

package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import src.TreeNodeUtil.TreeNode;

public class LeetCode_337 {

    public static void main(String[] args) {
        LeetCode_337 code = new LeetCode_337();

//        System.out.println(code.uniquePaths(2,2));
//        int[] ints = new int[]{1,2,3,1};
//        int[] ints = new int[]{2,7,9,3,1};
//        int[] ints = new int[]{2,3,2};
        Integer[] ints = new Integer[]{3,2,3,null,3,null,1};
//        Integer[] ints = new Integer[]{3,2,4};
//        Integer[] ints = new Integer[]{1,2};
//        Integer[] ints = new Integer[]{2,3,null,1};

        TreeNodeUtil.TreeNode root = TreeNodeUtil.transferArrays2Tree(ints);
//        System.out.println(code.transfer(root));
        System.out.println(code.robD(root));
        System.out.println(code.robDCount);

        System.out.println(code.rob(root));
        System.out.println(code.robCount);

    }


    int robDCount = 0 ;
    // 动态规划
    // 超时？？？？？？？？？  常规递归 可以过  动态规划 超时了？  虽然我这个动态规则很啰嗦
    // 这个问题 常规递归 是否有发生 大量重复计算？  常规递归有重复计算呀 ， 为啥动态规划还要慢一些呢？
    // 慢可能是 在 root 转 list的时候 慢。
    public int robD(TreeNode root) {
        return robD(transfer(root)) ;
    }

    public int robD(List<List<Integer>>  lists) {

        if(lists.size()==0) return 0;
        if(lists.size()==1) return lists.get(0)!=null?lists.get(0).get(0):0;
        if(lists.size()==2) return Math.max(lists.get(0)!=null?lists.get(0).get(0):0,(lists.get(1).get(0)!=null?lists.get(1).get(0):0)+(lists.get(1).get(1)!=null?lists.get(1).get(1):0));
//        int[][]  tmpInts = new int[lists.size()][lists.get(lists.size()-1).size()];
        int[][]  tmpInts = new int[lists.size()][];

        for(int i = 0 ; i<lists.size();i++){
            tmpInts[i] = new int[lists.get(i).size()];
        }

        // 初始化  最后那行
        for(int i =0; i<(int) Math.pow(2,tmpInts.length-1);i++){
//        for(int i =0; i<lists.get(0).size();i++){
            Integer value = lists.get(tmpInts.length-1).get(i);
            tmpInts[tmpInts.length-1][i] = value ==null ?0 : value;
        }


        // 初始化 倒数第二行
        for(int i =0; i<(int) Math.pow(2,tmpInts.length-2);i++){
//        for(int i =0; i<lists.get(0).size()/2;i++){
            tmpInts[tmpInts.length-2][i] = Math.max(lists.get(tmpInts.length-2).get(i)!=null?lists.get(tmpInts.length-2).get(i):0,tmpInts[tmpInts.length-1][2*i]+tmpInts[tmpInts.length-1][2*i+1]) ;
        }

        // 从后往前算
        //  这里的时间复杂度是O（n^2）  是不是慢在这里了
        for(int i = lists.size()-3 ; i>=0;i--){
            for(int j = 0 ; j<(int) Math.pow(2,i);j++){
                if(lists.get(i).get(j)==null) continue;
                if(tmpInts[i][j]!=0) continue;
                tmpInts[i][j] =
                        Math.max(tmpInts[i+1][2*j]+tmpInts[i+1][2*j+1],
                                (lists.get(i).get(j)!=null?lists.get(i).get(j):0)+tmpInts[i+2][j*4]+tmpInts[i+2][j*4+1]+tmpInts[i+2][j*4+2]+tmpInts[i+2][j*4+3]);
            }
        }

        return tmpInts[0][0] ;
    }


    // 时间复杂度 O（n）
    List<List<Integer>>   transfer(TreeNode root){

        List<List<Integer>> result  = new ArrayList<>();
        if(root == null) return result;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        List<Integer> tmpList = new ArrayList<>();
        tmpList.add(root.val);
        result.add(tmpList);
        while(!queue.isEmpty()){
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            boolean exit = true;
            for(int i = 0 ; i<size;i++) {
                TreeNode node = queue.poll();
                if(node == null) {
                    queue.add(null);
                    queue.add(null);
                    list.add(null);
                    list.add(null);
                }else {
                    if(exit) exit =false;
                    queue.add(node.left);
                    queue.add(node.right);

                    list.add(node.left==null?null:node.left.val);
                    list.add(node.right==null?null:node.right.val);
                }
            }
            if(exit) {
                result.remove(result.size()-1);
                return result ;
            }else{
                result.add(list);
            }


        }
        return result;
    }



    int robCount = 0;
    // 常规递归  击败  6
    //f(parentNode)  = Math.max(
    // f(parentNode.left) + f(parentNode.right),
    // parentNode.val+(f(parentNode.left.left)+parentNode.val+f(parentNode.left.right))+f(parentNode.right.left),parentNode.val+f(parentNode.right.right)
    // 还是有大量的重复计算呀 ，   时间复杂度是 O （2^n)
    public int rob(TreeNodeUtil.TreeNode root) {
        robCount++;
        if(root == null) return 0 ;
        return Math.max(
        rob(root.left)+rob(root.right),
                root.val+(root.left == null ?0:((rob(root.left.left))+(rob(root.left.right))))+
                        (root.right == null ?0:((rob(root.right.left))+(rob(root.right.right)))));
    }



}
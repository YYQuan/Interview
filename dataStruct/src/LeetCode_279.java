package src;

import javafx.util.Pair;

import java.util.*;

public class LeetCode_279 {

    public static void main(String[] args) {
        LeetCode_279 code = new LeetCode_279();

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
//        List<Integer> result =code.rightSideView(node1);

//        System.out.println(code.numSquares(13));
        System.out.println(code.numSquares2(56));
        System.out.println(code.numSquaresMyself(56));
        System.out.println(code.numSquaresMyselfD(56));

    }


    // 动态规划
    // 击败 82
    public int numSquaresMyselfD(int n) {
        if(n == 1 ) return 1;
        if(n == 2 ) return 2;
        int[] ints = new int[n+1];
        Arrays.fill(ints,-1);
        ints[1] = 1;
        ints[2] = 2;
        a:for(int i  = 3 ;i<n+1;i++){
            boolean isOne =  false;
            int min = Integer.MAX_VALUE;
            for(int j = 1;j*j<=i;j++){
                if(j*j == i){
                    ints[i] = 1;
                    continue  a;
                }
                min = Math.min(min,1+ints[i-j*j]);
            }
            ints[i] = min;
        }
        return ints[n];

    }

    // 普通递归
    // f(n) =  Math.min(result , 1+f(n-i^2));
    public int numSquaresMyself(int n) {

        if(n == 1 ) return 1;
        if(n == 2 ) return 2;

        int result = Integer.MAX_VALUE;

        for(int i = 1 ; i*i <= n ;i++){
            if(i*i == n ) return 1;
            result = Math.min( 1+numSquaresMyself(n-i*i),result);

        }
        return result;

    }




    // 解法 1 时间复杂度是 2^n   一个裂成了多个 复杂度就是幂函数
    public int numSquares(int n) {

        LinkedList<Pair<Integer,Integer>> pairs =new LinkedList<>();

        pairs.add(new Pair<>(n,0));

        while(!pairs.isEmpty()){
            Pair<Integer,Integer> pair = pairs.removeFirst();

            if(pair.getKey()==0){
                return pair.getValue();
            }

            for(int i =1;i*i<=pair.getKey();i++){
                pairs.add(new Pair<>(pair.getKey()-i*i,pair.getValue()+1));
            }

        }
        return -1;
    }

    // 尝试 在解法1的基础上 使用记忆化搜索
    //  击败20
    public int numSquares2(int n) {
        // 有很多重复进入的
        // 加一个数组 标识 重复进入的

        boolean[] isRead = new boolean[n+1];
        isRead[n] =true;
        LinkedList<Pair<Integer,Integer>> pairs = new LinkedList<>();

        pairs.add(new Pair<>(n,0));

        while(!pairs.isEmpty()){

            Pair<Integer,Integer> pair  = pairs.removeFirst();
//            System.out.println(pair.getKey()+"  "+pair.getValue());

            if(pair.getKey()==0){
                return pair.getValue();
            }

            for(int i = 1; i*i<=pair.getKey();i++){

                if(!isRead[pair.getKey()-i*i]){
                    pairs.add(new Pair(pair.getKey()-i*i,pair.getValue()+1));
                }
                isRead[pair.getKey()-i*i] = true;
            }


        }
        return -1;
//        throw new IllegalArgumentException();
    }


    public static class TreeNode {
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

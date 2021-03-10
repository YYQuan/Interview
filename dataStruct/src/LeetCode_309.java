package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_309 {

    public static void main(String[] args) {
        LeetCode_309 code = new LeetCode_309();

//        System.out.println(code.uniquePaths(2,2));
//        int[] ints = new int[]{1,2,3,1};
//        int[] ints = new int[]{2,7,9,3,1};
//        int[] ints = new int[]{2,3,2};
//        Integer[] ints = new Integer[]{3,2,3,null,3,null,1};
//        Integer[] ints = new Integer[]{3,2,4};
//        Integer[] ints = new Integer[]{1,2};
//        int[] ints = new int[]{1,2,3,0,2};
//        int[] ints = new int[]{1,2};
//        int[] ints = new int[]{1,2,4};
//        int[] ints = new int[]{2,1,2,1,0,1,2};
//        int[] ints = new int[]{2,1,0,1,2};
        int[] ints = new int[]{1,2,4,2,5,7,2,4,9,0};
//        int[] ints = new int[]{1,2,4,2,5};

        System.out.println(code.maxProfit(ints));
        System.out.println(code.maxProfitD(ints));

    }


    //  动态规划  击败 18
    //  其实动态规划 重要的点 是 要定义好状态
    //  一共有 五个状态  ： 买， 卖 ，不可操作（卖后的一天，啥操作都不行） ，已买 不操作（已经买了就不可买了），未买 不操作（ 后面可以买）
    //  三天是 一次交易最小间隔
    public int maxProfitD(int[] prices) {
        if(prices.length <= 1) return 0 ;
        int[][]  state = new int[prices.length][5];

        // 初始化 第一天
        state[0][0] = -prices[0];
        state[0][1] = 0;
        state[0][2] = 0;
        state[0][3] = -prices[0];
        state[0][4] = 0;

        // 初始化 第二天
        state[1][0] = -prices[1];
        state[1][1] = prices[1]-prices[0];
        state[1][2] = 0;
        state[1][3] =  -prices[0];
        state[1][4] =0;


        for(int i = 2 ; i<prices.length;i++){

            state[i][0] = Math.max(state[i-1][4],state[i-1][2])-prices[i];
            state[i][1] = Math.max(state[i-1][0],state[i-1][3])+prices[i];
            state[i][2] = state[i-1][1];
            state[i][3] = Math.max(state[i-1][0],state[i-1][3]);
            state[i][4] = Math.max(state[i-1][2],state[i-1][4]);
        }


        int result = 0;
        for(int i = 0 ;i<5;i++){
            result = Math.max(state[prices.length-1][i],result);
        }

        return  result;
    }




        // 常规递归  超时
    //核心 和关系 转化成 一个 二位数组
    //  买 （序号：0）
    //  卖 （序号：1）
    //  冷冻/ 无操作 （序号：2）
    //  二位数组  有一个路径规则 ： 买  -> 无操作... -> 卖 -> 冷冻 -> 无操作... -> 买
    //  也就是在这个二维数组中 找到一条路径 使得值最大

    // 加一个 是否已买入的 状态
    // 0 -> 1 /2
    // 1 -> 2
    // 2 -> 2/1/0   根据是否已买入的状态的做区分
    public int maxProfit(int[] prices) {
        if(prices.length == 0) return 0 ;
        int[][] path = transfer(prices);

        return maxProfit(path,0,2,0,false);
    }

    public int maxProfit(int[][] path ,int row/* 当前行*/ ,int index/* 上一个操作*/ ,int currentValue,boolean isWaitSell) {
        if(row == path.length-1) {
            if(isWaitSell) {
                return currentValue + path[row][1];
            }else{
                return currentValue;
            }
        }
        int result = Integer.MIN_VALUE ;
       if(index ==1 ){
            result = maxProfit(path, row + 1,2,currentValue+path[row][2],false);
        }else if(index ==2 ){
           if(isWaitSell){
               result = Math.max(
                       maxProfit(path, row + 1, 1, currentValue + path[row][1], false),
                       maxProfit(path, row + 1, 2, currentValue + path[row][2], true));

           }else {
               result = Math.max(
                       maxProfit(path, row + 1, 0, currentValue + path[row][0], true),
                       maxProfit(path, row + 1, 2, currentValue + path[row][2], false));
           }

       }else{
           result = Math.max(
                   maxProfit(path, row + 1, 1, currentValue + path[row][1], false),
                   maxProfit(path, row + 1, 2, currentValue + path[row][2], isWaitSell));
       }
        return result;
    }

    public int[][] transfer(int[] prices){
        int[][] result = new int[prices.length][3];

        for(int i = 0 ; i<prices.length;i++){
            int[] tmp =  new int[3];
            //买
            tmp[0] = -prices[i];
            //卖
            // 第一天不能卖
            tmp[1] = i==0? 0: prices[i];
            //冻/无操作
            tmp[2] = 0;

            result[i] = tmp;
        }

        return result;
    }

}
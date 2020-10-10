import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_189 {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        LeetCode_189 code = new LeetCode_189();
//        int[] ints  = new int[]{1,2,3,1};
        int[] ints  = new int[]{1,2};
//        int[] ints  = new int[]{5,2,3};
//        int[] ints  = new int[]{1,2,3,11,53,2,3,5,4,8,9,1,5,2,3};

        System.out.println(code.solution(ints));
        System.out.println(code.solution2(ints));
//        System.out.println(code.rob(ints));

    }


    int[]  tmpResult ; // 存放解的数组
    public int solution(int[] arr){

        tmpResult = new int[arr.length];
        Arrays.fill(tmpResult,-1);
        return solutionActual(arr,0);
    }

    public int  solutionActual(int[] arr,int start){
        int res  = -1;
//        if(arr== null||arr.length<1||start>arr.length-1) return sum ;
        if(arr== null||arr.length<1||start>arr.length-1) return 0 ;

        if(tmpResult[start] != -1)  return tmpResult[start];

        for(int i = start ; i< arr.length ;i++) {
//            res = Math.max(res,Math.max(arr[start]+solutionActual(arr,start+2,0),arr[start]+solutionActual(arr,start+3,0)));
//            res = Math.max(arr[start]+solutionActual(arr,start+2,0),arr[start]+solutionActual(arr,start+3,0));
            // 对于流程来说，  在一间房子里， 有两种选择， 1 跳一间房  start +2  2 跳 两间房 start +3  ( 跳四中间就可用多偷一间了)
            //res = Math.max(res,Math.max(arr[start]+solutionActual(arr,start+2)),arr[start]+solutionactual(arr.start+3));
            // 完整应该是这个样子的  但是 实际上  solutionActual(arr,start+2) 中是包括了 solutionActual(arr,start+3)的情况的
            // 所以solutionActual(arr,start+2) >= solutionActual(arr,start+3)
            // 因此可以简化成下面这个
            res = Math.max(res,Math.max(solutionActual(arr,start+1),arr[start]+solutionActual(arr,start+2)));
        }

        tmpResult[start] = res;
        return tmpResult[start];
    }
////////////////////////////////////////////////////////////////////////////

    /// 大佬的 记忆化搜索
    // memo[i] 表示考虑抢劫 nums[i...n) 所能获得的最大收益
    private int[] memo;

    public int rob(int[] nums) {
        memo = new int[nums.length];
        Arrays.fill(memo, -1);
        return tryRob(nums, 0);
    }

    // 考虑抢劫nums[index...nums.size())这个范围的所有房子
    private int tryRob(int[] nums, int index){

        if(index >= nums.length)
            return 0;

        if(memo[index] != -1)
            return memo[index];

        int res = 0;
        for(int i = index ; i < nums.length ; i ++)
            res = Math.max(res, nums[i] + tryRob(nums, i + 2));
        memo[index] = res;
        return res;
    }

///////////////////////
    // 动态规划

    // 要自底向上 来求解
    public  int solution2(int[] arr){
        int[]  tmpResult = new int[arr.length+1];
        Arrays.fill(tmpResult,-1);

        if(arr.length ==1 ) return arr[0];
        if(arr.length ==2 ) return Math.max(arr[0],arr[1]);

        tmpResult[0]  = arr[0];
        tmpResult[1]  = Math.max(arr[0],arr[1]);

        for(int i = 2 ; i<arr.length ; i++){
            tmpResult[i] = Math.max(tmpResult[i-1] ,arr[i] + tmpResult[i-2]);
        }


        return tmpResult[arr.length-1];
    }

}

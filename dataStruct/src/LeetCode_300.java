import java.util.Arrays;

public class LeetCode_300 {

    public static void main(String[] args) {
//        int[] ints =new int[] {10,9,2,5,3,7,101,18};
//        int[] ints =new int[] {10,11,2};
//        System.out.println(new LeetCode_300().solution2(
//                ints));
        int nums1[] = {10, 9, 2, 5, 3, 7, 101, 18};
        // 4
        int nums2[] = {4, 10, 4, 3, 8, 9};
        // 3
        int nums3[] = {2, 2};
        // 1
        int nums4[] = {1, 3, 6, 7, 9, 4, 10, 5, 6};
        // 6
        int nums5[] = {1, 3, 6, 7, 9, 4, 10, 5, 6,9};
        // 6


//
//        System.out.println((new  LeetCode_300().lengthOfLIS(nums1)));
//        System.out.println((new  LeetCode_300().lengthOfLIS(nums2)));
//        System.out.println((new  LeetCode_300().lengthOfLIS(nums3)));
//        System.out.println((new  LeetCode_300().lengthOfLIS(nums4)));
//        System.out.println((new  LeetCode_300().lengthOfLIS(nums5)));
//
//        System.out.println((new  LeetCode_300().solution2(nums1)));
//        System.out.println((new  LeetCode_300().solution2(nums2)));
//        System.out.println((new  LeetCode_300().solution2(nums3)));
//        System.out.println((new  LeetCode_300().solution2(nums4)));
//        System.out.println((new  LeetCode_300().solution2(nums5)));



//        System.out.println((new  LeetCode_300().solution(nums1)));
//        System.out.println((new  LeetCode_300().solution(nums2)));
//        System.out.println((new  LeetCode_300().solution(nums3)));
//        System.out.println((new  LeetCode_300().solution(nums4)));
//        System.out.println((new  LeetCode_300().solution(nums5)));

        System.out.println((new  LeetCode_300().solutionDync(nums1)));
        System.out.println((new  LeetCode_300().solutionDync(nums2)));
        System.out.println((new  LeetCode_300().solutionDync(nums3)));
        System.out.println((new  LeetCode_300().solutionDync(nums4)));
        System.out.println((new  LeetCode_300().solutionDync(nums5)));




    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //递归

    // 从后往前 时 要注意 要加入index   那么 index 的值要 index -1 最长队列中的最大值才行
    // 但是前面的没算出来就不知道index -1 的最长队列中的最大值是多少   所以得改成从前往后


    public  int   solution(int[] arr){
        if( arr == null || arr.length <= 0 ) return 0 ;


        solutionActual(arr,0 , 0 , 0);
        return result;
    }

    int result = 0 ;

    // 我的思路是 把全部情况的值都算出来， 然后取最大的值

    public void solutionActual(int[] arr,int index ,int currentMaxValue ,int currentMaxLength){

        if(index > arr.length-1) {
            result = Math.max(result,currentMaxLength);
            return ;
        }

        int currentValue = arr[index];

        if(currentValue>currentMaxValue){
            //加进去
            solutionActual(arr,index+1,currentValue,currentMaxLength+1);
            solutionActual(arr,index+1,currentMaxValue,currentMaxLength);
        }else{
            solutionActual(arr,index+1,currentMaxValue,currentMaxLength);

        }

    }


    // 这么递归 感觉没法改造成记忆化搜索

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 大佬的思路的递归(记忆化搜索)

    public int solution2(int[] arr){
        if(arr == null || arr.length <=0) return 0;

        int[] tmpResult = new int[arr.length];
        Arrays.fill(tmpResult,-1);
        // 先从左开始 逐个添加元素
        // 得先知道了前面的最大长度的值， 才能知道后面的
        for(int i = 0 ; i<arr.length ; i++){
            tmpResult[i] =  solutionActual2(arr,i,tmpResult);
        }
        // 注意  最大值不一定是tmpResult[tmpResult.length -1]
        int res = 1;
        for(int i = 0 ; i<tmpResult.length; i++){
            res = Math.max(res,tmpResult[i]);
        }
        return res ;
    }

    // 返回 arr 中 [ 0...index] 的 最大长度
    public int solutionActual2(int[] arr,int index,int[] indexLength){
        int res = 1;
        for(int i = 0 ; i<index ;i++){
            if(arr[index]>arr[i]){
                res = Math.max(res,1+indexLength[i]);
            }
        }
        return res;
    }


    ///////////////////
    // 大佬的记忆化搜索

    private int[] memo;

    public int lengthOfLIS(int[] nums) {

        if(nums.length == 0)
            return 0;

        memo = new int[nums.length];
        Arrays.fill(memo, -1);
        int res = 1;
        for(int i = 0 ; i < nums.length ; i ++)
            res = Math.max(res, getMaxLength(nums, i));

        return res;
    }

    // 以 nums[index] 为结尾的最长上升子序列的长度
    private int getMaxLength(int[] nums, int index){

        if(memo[index] != -1)
            return memo[index];

        int res = 1;
        for(int i = 0 ; i <= index-1 ; i ++)
            if(nums[index] > nums[i])
                res = Math.max(res, 1 + getMaxLength(nums, i));

        return memo[index] = res;
    }


    ////////////////////////////////////////
    //我的动态规划

    // 感觉和 记忆化搜索的代码类似
    public int  solutionDync(int[] arr){
        int res= 0 ;

        if(arr == null || arr.length <=0 )  return 0 ;

        int[] tmpResult = new int[arr.length];

        Arrays.fill(tmpResult,-1);
        tmpResult[0] = 1;

        for(int i = 1 ; i< arr.length ;i++){
            int resIndex = 1;
            for(int j = 0 ; j<i ; j++){
                if(arr[i] > arr[j]){
                    resIndex = Math.max(resIndex, 1+tmpResult[j]);
                }
            }
            tmpResult[i] = resIndex;
            res =Math.max(res, resIndex);
        }

        return  res;
    }

    // 和大神的代码差不多

}

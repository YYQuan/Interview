package src;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class LeetCode_697 {

    public static void main(String[] args) {
        LeetCode_697 code = new LeetCode_697();

//        int[] ints  = new int[]{1, 2, 2, 3, 1};
//        int[] ints  = new int[]{1,2,1};
//        int[] ints  = new int[]{1,2,2,3,1};
        int[] ints  = new int[]{3,3,4};

        int result = code.findShortestSubArray(ints);
        int result2 = code.findShortestSubArray2_2(ints);

//        System.out.println(Arrays.toString(ints));
        System.out.println(result);
        System.out.println(result2);




    }


    // 击败百分之5
    // 技术要点
    // 用数组来 存储 count 不使用hashmap
    // 遍历一遍 数组 得到 全部元素 出现的次数 以及  其度
    // 再遍历一遍数组 ， 得到每一个元素的 度 ，以及 其 最左 index 以及 最右 index  有了最左/右的index 就能知道 连续数组长度。
    // 时间复杂度 O （n^2）
    public int findShortestSubArray(int[] nums) {
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;

        // 得到最大值 最小值 方便划分存储 数组空间
        for(int i :nums){
            minValue = Math.min(minValue,i);
            maxValue = Math.max(maxValue,i);
        }

        int[] countNums = new int[maxValue-minValue+1] ;

        int maxDu = 0;

        for(int i :nums){
            maxDu = Math.max(maxDu,++countNums[i-minValue]);
        }

        int result =nums.length;

        int index = 0;
        while(index<nums.length){
            int start = 0;
            int end = nums.length-1;
            if(countNums[nums[index]-minValue]!=maxDu) {
                index++;
                continue;
            }

            int currentValue = nums[index];

            while(start<nums.length&&nums[start]!=currentValue){
                start++;
            }

            while(end>=0 &&nums[end]!=currentValue){
                end--;
            }
            index++;
            result = Math.min(result,end-start+1);
        }
        return result;
    }





    public int findShortestSubArray2(int[] nums) {
        int result = nums.length ;

        int du  = calculatorDu(nums,0,nums.length-1);

        int start = 0;
        int end =du-1;
        while(end>0&&end<nums.length){

            int currentDu = calculatorDu(nums,start,end);

            if(currentDu == du){

                while(duEqulas(nums,start+1,end,du)){
                    start++;
                }

                result = Math.min(result,end-start+1);

            }
            start = 0;
            end++;
        }

        return result;
    }

    boolean duEqulas(int[] nums ,int start ,int end ,int targetDu){

        if(end-start+1<targetDu)  return false;

        return calculatorDu(nums,start,end)==targetDu;
    }

    public int calculatorDu(int[] nums,int start ,int end){
        int result = 0 ;
        HashMap<Integer,Integer> map = new HashMap<>();
        for(int i=start;i<=end ;i++){
            if(map.containsKey(nums[i])){
                int count = map.get(nums[i]);
                map.put(nums[i],count+1);
                if(count+1>result){
                    result  = count+1;
                }
            }else{
                map.put(nums[i],1);
                if(result == 0 ) result =1;
            }
        }
        return result;
    }

    public int findShortestSubArray2_2(int[] nums) {
        if(nums==null||nums.length==0)  return 0 ;
        int target = calculator(nums,0,nums.length-1);
        if(target == 1)  return 1;
        int min = Integer.MAX_VALUE;
        for(int i  =0 ; i<nums.length;i++){
            for(int j = i+1 ;j<nums.length;j++){

                int count = calculator(nums,i,j);
                if(count == target){
                    min = Math.min(j-i+1,min);
                }

            }
        }

        return min;

    }

    public int calculator(int[] nums,int l ,int r ){
        HashMap<Integer,Integer>  map = new HashMap<>();

        for(int  i =l ;i<=r;i++){
            map.put(nums[i], map.getOrDefault(nums[i],0)+1);
        }

        int max = 0;
        for( int i :map.keySet()){
            max = Math.max(max, map.get(i));
        }

        return max ;
    }
}

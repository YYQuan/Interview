package src;

import java.lang.reflect.Array;
import java.util.*;

public class LeetCode_18 {

    public static void main(String[] args) {
        LeetCode_18 code = new LeetCode_18();
//        int[] ints  = new int[]{1, 0, -1, 0, -2, 2};
//        int[] ints  = new int[]{0, 0,0};
//        int[] ints  = new int[]{0, 0,0,0};
//        int[] ints  = new int[]{1,-2,-5,-4,-3,3,3,5};
        int[] ints  = new int[]{-1,0,-5,-2,-2,-4,0,1,-2};
//        int[] result = code.solution(ints,9);
//        int[] result = code.solution(ints,9);
//        int[] result = code.twoSum(ints,9);


//        List<List<Integer>> result = code.fourSum(ints,9);
//        List<List<Integer>> result = code.fourSum(ints,0);
//        List<List<Integer>> result = code.fourSum(ints,-11);
        List<List<Integer>> result = code.fourSum(ints,-9);

//        Arrays.toString(Arrays.toString(new List[]{result}));
        for(List<Integer> item : result) {

            System.out.println(Arrays.toString(item.toArray()));
        }

    }



    // 方案 1 击败 百分之 10
    public List<List<Integer>> fourSum(int[] nums, int target) {

        //  这个算法时间负责度明显大于O（nlogn） 所以排序并不会占主要开销
        quickSork(nums);
        List<List<Integer>> result = new ArrayList<>();

        for(int i  = 0 ;i<nums.length;i++){

//            if(nums[i]>target) break;

            List<List<Integer>>  threeSumList = threeSum(nums,target-nums[i],i+1,nums.length-1);

            queryItemAdd(nums, result, i, threeSumList);


        }

        return result;
    }


    public List<List<Integer>>  threeSum(int[] nums,int target,int start ,int end){
        List<List<Integer>> result = new ArrayList<>();

        for(int i = start;i<=end;i++){
//            if(nums[i]>target) break;
            List<List<Integer>>  twoSumList = twoSum(nums,target-nums[i],i+1,nums.length-1);

            queryItemAdd(nums, result, i, twoSumList);

        }

        return result;
    }

    private void queryItemAdd(int[] nums, List<List<Integer>> result, int i, List<List<Integer>> twoSumList) {
        if(twoSumList!=null&&twoSumList.size()>0) {
            for (List<Integer> twoSumItemList : twoSumList) {
                twoSumItemList.add(nums[i]);
                if(!containList(result,twoSumItemList)){
                    result.add(twoSumItemList);
                }

            }
        }
    }

    public List<List<Integer>>  twoSum(int[] nums ,int target ,int start ,int end){
        List<List<Integer>> result = new ArrayList<>();

        if(end-start<1) return result;

        for(int i = start,j = end;i<j;){
            List<Integer> item = new ArrayList<>();
            int sum = nums[i]+nums[j];
            if(sum == target){
                item.add(nums[i]);
                item.add(nums[j]);
                result.add(item);
                i++;
                j--;
            }else if(sum>target){
                j--;
            }else{
                i++;
            }


        }

        return result;
    }

    public boolean containList(List<List<Integer>> list ,List<Integer> target){

        for(List<Integer> l:list){
            if(isSame(target,l)){
                return true;
            }
        }

        return false;
    } ;

    public boolean isSame(List<Integer> list1,List<Integer> list2){
        if(list1.size()!=list2.size()){
            return false;
        }
        HashMap<Integer,Integer> map = new HashMap<>();

        for(Integer i:list1){

            if(map.containsKey(i)){
                map.put(i,map.get(i)+1);
            }else{
                map.put(i,1);
            }

        }

        for(Integer i : list2){
            if(map.containsKey(i)){
                if(map.get(i)<0) return false;

                map.put(i,map.get(i)-1);
            }else{
                return false;
            }

        }

        for(Integer v :map.values()){
            if(v!=0) return false;
        }

        return true;
    }





    private void quickSork(int[] nums){

        quickSork(nums,0,nums.length-1);
    }

    private  void quickSork(int[] nums ,int start,int end){
        if(start >= end) return ;


        int mid = findIndex(nums,start,end);
        quickSork(nums,start,mid-1);
        quickSork(nums,mid+1,end);
    }


    private int findIndex(int[] nums,int start,int end){
        int random = new Random().nextInt(end-start)+start;
        swap(nums,start,random);

        int target = nums[start];

        int i = start+1;
        int j = end;

        while(true){


            // i 找到第一个 大于等于target的index
            while(i<=end&& nums[i]<target){
                i++;
            }

            // j 从右往左 找到第一个小于等于target
            while(j>=start&&nums[j]>target){
                j--;
            }

            // 是否满足 交换条件
            // 是否满足 跳出条件
            if(i<j){
                swap(nums,i,j);
                i++;
                j--;
            }else{
                break;
            }



        }
        swap(nums,start,j);

        return  j;
    }


    void swap(int[] nums ,int l , int r){

        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;

    }
}

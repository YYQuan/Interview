package src;

import java.util.*;

public class LeetCode_16 {

    public static void main(String[] args) {
        LeetCode_16 code = new LeetCode_16();
//        int[] ints  = new int[]{1, 0, -1, 0, -2, 2};
//        int[] ints  = new int[]{0, 0,0};
//        int[] ints  = new int[]{0, 0,0,0};
//        int[] ints  = new int[]{1,-2,-5,-4,-3,3,3,5};
//        int[] ints  = new int[]{-1,0,-5,-2,-2,-4,0,1,-2};
//        int[] ints  = new int[]{-1,2,1,-4};
//        int[] ints  = new int[]{1,1,1,0};
        int[] ints  = new int[]{13,2,0,-14,-20,19,8,-5,-13,-3,20,15,20,5,13,14,-17,-7,12,-6,0,20,-19,-1,-15,-2,8,-2,-9,13,0,-3,-18,-9,-9,-19,17,-14,-19,-4,-16,2,0,9,5,-7,-4,20,18,9,0,12,-1,10,-17,-11,16,-13,-14,-3,0,2,-18,2,8,20,-15,3,-13,-12,-2,-19,11,11,-10,1,1,-10,-2,12,0,17,-19,-7,8,-19,-17,5,-5,-10,8,0,-12,4,19,2,0,12,14,-9,15,7,0,-16,-5,16,-12,0,2,-16,14,18,12,13,5,0,5,6};

//        int[] result = code.solution(ints,9);
//        int[] result = code.solution(ints,9);
//        int[] result = code.twoSum(ints,9);

//        int result = code.threeSumClosest(ints,1);
//        int result = code.threeSumClosest(ints,100);
        int result = code.threeSumClosest(ints,-59);
//        List<List<Integer>> result = code.quickSork(ints);

        System.out.println(Arrays.toString(ints));
        System.out.println(result);

//        for(List<Integer> item : result) {
//
//            System.out.println(Arrays.toString(item.toArray()));
//        }



    }


    //击败百分之十
    public int threeSumClosest(int[] nums, int target) {
        if(nums.length<=2) {
            throw new IllegalArgumentException();
        }
        quickSort(nums);

        int currentTargetCloest = Integer.MAX_VALUE;

        for(int i = 0;i<nums.length-2;i++){

            int twoSumClosest = twoSumClosest(nums,target-nums[i],i+1,nums.length-1);
            if(twoSumClosest == target-nums[i]){
                System.out.println(" currentTargetCloest target --> "+currentTargetCloest);

                return target;
            }else{
                if(currentTargetCloest ==Integer.MAX_VALUE){
                    currentTargetCloest = twoSumClosest+nums[i];
                    System.out.println(" currentTargetCloest init --> "+currentTargetCloest);
                }else{
                    int adsSum = Math.abs(target-nums[i] - twoSumClosest);
                    int adsCurrent = Math.abs(target- currentTargetCloest);
                    if (adsSum < adsCurrent) {
                        currentTargetCloest =twoSumClosest+nums[i];
                        System.out.println(" currentTargetCloest --> "+currentTargetCloest);
                    }
                }
            }
        }

        return  currentTargetCloest;

    }

    public int twoSumClosest(int[] nums ,int target ,int start ,int end){

        if(end-start<1) {
            throw new IllegalArgumentException();
        }

        int currentTargetCloest = Integer.MAX_VALUE;


        int i = start ;
        int j = end;
        System.out.println(" twoSumClosest TARGET target--> "+target);


        while(i<j) {
            int sum  = nums[i]+nums[j];
            if (sum == target) {
                System.out.println(" twoSumClosest TARGET --> "+currentTargetCloest);

                return target;
            } else if (sum > target) {
                j--;
            }else{
                i++;
            }

            if(currentTargetCloest==Integer.MAX_VALUE){
                currentTargetCloest = sum;
            }else {
                int adsSum = Math.abs(target - sum);
                int adsCurrent = Math.abs(target - currentTargetCloest);
                if (adsSum < adsCurrent) {
                    currentTargetCloest =sum;

                }
            }
        }

        System.out.println(" twoSumClosest --> "+currentTargetCloest);

        return currentTargetCloest;
    }


    public  void  quickSort(int[] nums){
        if(nums.length<=1) return ;

        quickSort(nums,0,nums.length-1);
    }


    // 排序[start, end]
    public void quickSort(int[] nums ,int start, int end ){

        if(end-start<=0) return;

        int middle = handleIndex(nums,start,end);
        quickSort(nums,start,middle-1);
        quickSort(nums,middle+1,end);

    }

    public int handleIndex(int[] nums,int start ,int end){
        int result = 0;

        int random = new Random().nextInt(end-start)+start;

        swap(nums,start,random);

        int base = nums[start];
        int i =start+1;
        int j = end;

        while(true){


            // i 找 第一个 大于等于base的index
            while(i<end&&nums[i]<base){
                i++;
            }

            // j找到第一个 小于等于base的index
            while(j>start&&nums[j]>base){
                j--;
            }


            if(i<j){
                swap(nums,i,j);
                i++;
                j--;
                System.out.println(" start --> "+start);
                System.out.println(" end --> "+end);
                System.out.println(" I --> "+i);
                System.out.println(" j --> "+j);

            }else{
                break;
            }


        }

        result = j;

        swap(nums,start,j);

        return result;
    }

    public void swap(int[] nums ,int l , int r ){
        int tmp  = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }

}

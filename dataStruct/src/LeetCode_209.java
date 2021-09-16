package src;

import java.util.HashSet;
import java.util.LinkedList;

public class LeetCode_209 {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        LeetCode_209 code = new LeetCode_209();
        int[] ints  =new  int[]{1,2,3,4,5};

//        code.solution(7,ints);

        System.out.println("result  "+ code.solution(7,ints));
        System.out.println("result  "+ code.minSubArrayLen(7,ints));
        System.out.println("result  "+ code.minSubArrayLen2(7,ints));

    }


    public int solution(int s ,int[] nums ){

        if(nums.length<=0) return 0 ;

        int l = 0 ,r= 0;// 满足>s 的 左右 index
        int minCurrentBound = Integer.MAX_VALUE ;
        int tmp = nums[0] ;// [l,r] 的和

        while(r<nums.length){
            //r 找到第一个 满足  和大于 s的index 直到超出范围
            while(r<nums.length&&tmp<s){
                r++;
                if(r<nums.length) {
                    tmp += nums[r];
                }
            }

            //l 找到第一个不满足和 大于 s的index
            while(tmp>=s){
                tmp -= nums[l];
                l++;

            }


            // 如果r <nums.length 就说明在范围内 是有 满足条件的。
            if(r<nums.length&&
                    minCurrentBound >(r-l+2)){

                // 主要是要范围  不是 下标 所以要多 +1
                minCurrentBound = r-l+2;

            }

        }

        return  minCurrentBound>= Integer.MAX_VALUE?0: minCurrentBound ;
    }


    // 思路一样 ，但是简化写边界条件
    public int solution2(int s ,int[] nums ){

        if(nums.length<=0) return 0 ;

        int l = 0 ,r= 0;// 满足>s 的 左右 index
        int minCurrentBound = 0;
        int tmp = nums[0] ;// [l,r] 的和

        while(r<nums.length){
            //r 找到第一个 满足  和大于 s的index 直到超出范围
            while(r<nums.length&&tmp<s){
                r++;
                if(r<nums.length) {
                    tmp += nums[r];
                }
            }

            //l 找到第一个不满足和 大于 s的index
            while(tmp>=s){

                // 这里来赋值  都是有效 长度,
                if(minCurrentBound==0||minCurrentBound>(r-l+1)){
                    minCurrentBound = r-l+1;
                }
                tmp -= nums[l];
                l++;

            }

        }

        return  minCurrentBound ;
    }


    public int minSubArrayLen(int target, int[] nums) {

        if(nums ==null) return 0;
        if(target<0)  return 0;

        int result = Integer.MAX_VALUE;
        for(int i = 0 ; i<nums.length;i++){
            int tmp = minSubArrayLen(target,nums,i,0);
            if(tmp >0){
                result = Math.min(result,tmp);
            }
        }

        return result ==Integer.MAX_VALUE ?0:result;

    }
    public int minSubArrayLen(int target, int[] nums,int index ,int count) {
        if(index >= nums.length ) return 0;
        int tmp = nums[index];
        if(tmp >= target){
            return count +1;
        }else {
            return minSubArrayLen(target-tmp, nums,index+1,count+1);
        }
    }



    public int minSubArrayLen2(int target, int[] nums) {

        if(nums==null) return 0 ;
        if(target <0) return 0 ;
        if(nums.length<2){
            return nums[0]>=target?1:0;
        }

        int l = 0 ;
        int r = 0;
        int result = Integer.MAX_VALUE;
        int tmp = 0;
        while(r<nums.length){

            while(r<nums.length&&tmp<target){
                tmp+=nums[r];
                r++;
            }

            if(tmp<target&&r==nums.length) return result;

            while(l<r&&tmp>=target){
                tmp-=nums[l];
                l++;
            }
            if(l == r) return 1;
            else   if(r<=nums.length){
                result = Math.min(result ,r-l+1);
            }
        }
        return result ==Integer.MAX_VALUE?0:result;
    }

}

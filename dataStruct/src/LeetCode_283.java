package src;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import java.util.Arrays;
import java.util.HashMap;

/**
 *  给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 */
public class LeetCode_283 {

    public static void main(String[] args) {
        LeetCode_283 code = new LeetCode_283();
        int[] ints  = new int[]{0,2,0,7,11,15};
        code.moveZeroes2(ints);

        System.out.println(Arrays.toString(ints));
    }


    //
    public void moveZeroes3(int[] nums) {
        if(nums == null) return;
        int k = 0;
        for(int i =0 ; i<nums.length;i++){

            if(nums[i]!=0){
                nums[k++] = nums[i];
            }
        }

        for(int i= k;i<nums.length;i++){
            nums[i] = 0 ;
        }


    }
    public void moveZeroes(int[] nums) {

        if(nums == null || nums.length<=0)  return ;
        // 非零的值一直往前方， 其他的补0 即可  顺序也是不会变的
        int k = 0 ;

        for(int  i = 0 ; i < nums.length; i++){
            if(nums[i]!=0 ){
                nums[k++] = nums[i];
            }
        }

        for(int i = k; i<nums.length;i++){
            nums[i] = 0;
        }
    }


    public void moveZeroes2(int[] nums) {


        int l = 0;
        int r = 0;
        while(r<nums.length-1){

            // 找到第一个 0
            while(l<(nums.length-1)&&nums[l]!=0){
                l++;
            }

            r = l +1;
            while(r<(nums.length-1)&&nums[r]==0){
                r++;
            }

            if(r== nums.length) break;
            swap(nums,l,r);

            l++;

        }
    }

    public void swap(int[] nums,int l ,int r){
        int tmp = nums[l];
        nums[l] =nums[r];
        nums[r] =tmp;

    }

}

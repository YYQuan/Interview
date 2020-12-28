package src;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import java.util.HashMap;

/**
 *  给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 */
public class LeetCode_283 {

    public static void main(String[] args) {
        LeetCode_283 code = new LeetCode_283();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);

    }


    //
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

}

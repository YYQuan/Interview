package src;

import java.util.HashMap;

public class LeetCode_27 {

    public static void main(String[] args) {
        LeetCode_27 code = new LeetCode_27();
        int[] ints  = new int[]{0,1,2,2,3,0,4,2};
        int result = code.removeElement(ints,2);
        System.out.println(result+"  ");
        for(int i =0 ;i<ints.length;i++){
            System.out.print(ints[i]+"  ");

        }

    }


    public int removeElement(int[] nums, int val) {
        if(nums==null||nums.length<0 )  return 0;
        int k = 0 ;

        for(int i = 0 ; i<nums.length;i++){
            if(nums[i]!=val){
                nums[k++] = nums[i];
            }
        }
        return k;
    }

}

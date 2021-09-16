package src;

public class LeetCode_80 {

    public static void main(String[] args) {
        LeetCode_80 code = new LeetCode_80();
//        int[] ints  = new int[]{0,0,1,1,1,1,2,3,3};
        int[] ints  = new int[]{1,1,1,2,2,3};
//        int result = code.removeElement(ints);
        int result = code.removeDuplicates2(ints);
        System.out.println(result+"  ");
        for(int i =0 ;i<ints.length;i++){
            System.out.print(ints[i]+"  ");

        }

    }


    // 注意 初始值
    public int removeElement(int[] nums) {
        if(nums.length<3) return nums.length;
        int k = 1 ;
        for(int i = 2;i<nums.length;i++){
            if(nums[k] != nums[i] ||nums[k] != nums[k-1]){
                nums[++k] = nums[i];
            }
        }

        return k+1;
    }


    public int removeDuplicates2(int[] nums) {
        int k = 1;
        for(int i = 2 ;i<nums.length;i++){

            if(nums[i] == nums[i-1] && nums[k-1]==nums[k]){
                continue;
            }else{
                nums[++k] = nums[i];
            }
        }
        return k;
    }
}

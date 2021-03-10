package src;

public class LeetCode_213 {

    public static void main(String[] args) {
        LeetCode_213 code = new LeetCode_213();

//        System.out.println(code.uniquePaths(2,2));
//        int[] ints = new int[]{1,2,3,1};
//        int[] ints = new int[]{2,7,9,3,1};
//        int[] ints = new int[]{2,3,2};
        int[] ints = new int[]{1,2,3,1};
        System.out.println(code.robD(ints));

    }

    // 击败  100
    // 用边界来区分
    public int robD(int[] nums) {

        if(nums.length==0) return 0;

        if(nums.length==1) return nums[0];

        int[] tmp = new int[nums.length];
        int[] tmp2 = new int[nums.length];



        tmp[0] = nums[0];
        tmp[1] = Math.max(nums[0],nums[1]);


        tmp2[0] = 0;
        tmp2[1] = nums[1];

        for(int i = 2 ; i<nums.length-1;i++){
            tmp[i] = Math.max(tmp[i-1],tmp[i-2]+nums[i]);
        }
        tmp[nums.length-1] = tmp[nums.length-2];

        for(int i = 2 ; i<nums.length;i++){
            tmp2[i] = Math.max(tmp2[i-1],tmp2[i-2]+nums[i]);
        }


        return Math.max(tmp[nums.length-1],tmp2[nums.length-1]);
    }

}
package src;

public class LeetCode_80 {

    public static void main(String[] args) {
        LeetCode_80 code = new LeetCode_80();
        int[] ints  = new int[]{0,0,1,1,1,1,2,3,3};
//        int result = code.removeElement(ints);
        int result2 = code.removeDuplicates(ints);
//        System.out.println(result+"  ");
        System.out.println(result2+"  ");
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

    // 上面的题目的判断读不懂
    // 用这种蠢一点的判断来处理 分成两步来操作， 先把要排除的标记出来 并且标记不能影响到后序的标记
    public int removeDuplicates(int[] nums) {

        if(nums == null ) return 0;
        if(nums.length<2) return nums.length;

        for(int i = 2 ;i<nums.length;i++){
            if(nums[i] == nums[i-1] &&nums[i-2] == nums[i-1]){
                int tmp = nums[i];
                while(i<nums.length&&nums[i] == tmp){
                    nums[i] =Integer.MIN_VALUE;
                    i++;
                }
            }

        }
        int k =2;
        for(int i =2 ;i<nums.length;i++){
            if(nums[i]!=Integer.MIN_VALUE){
                nums[k++] = nums[i];
            }
        }
        return k ;
    }



}

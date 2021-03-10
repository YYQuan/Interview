package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_90 {

    public static void main(String[] args) {
        LeetCode_90 code = new LeetCode_90();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
//        int[] ints = new int[]{10,1,2,7,6,1,5};
//        int[] ints = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] ints = new int[]{1,1,1,2};
//        int[] ints = new int[]{10,1,2,7,6,1,1,5};
//        int[] ints = new int[]{1,2,2};
        int[] ints = new int[]{4,4,4,1,4};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.subsetsWithDup(ints));

    }

    List<List<Integer>> list = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    // 去重 收端 ， 排序  加  list contain
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        subsets(nums,0);
        return list;
    }
    public void  subsets(int[] nums,int index) {
        if(path.size()<=nums.length){
            if(!list.contains(path)) {
                list.add(new ArrayList<>(path));
            }
        }else{
            return ;
        }

        for(int i = index ; i<nums.length;i++){

            path.add(nums[i]);
            subsets(nums,i+1);
            path.remove(path.size()-1);

        }


    }

}

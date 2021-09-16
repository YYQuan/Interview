package src;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_78 {

    public static void main(String[] args) {
        LeetCode_78 code = new LeetCode_78();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
//        int[] ints = new int[]{10,1,2,7,6,1,5};
//        int[] ints = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] ints = new int[]{1,1,1,2};
//        int[] ints = new int[]{10,1,2,7,6,1,1,5};
        int[] ints = new int[]{1,2,3};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.subsets(ints));
        System.out.println(code.subsets2(ints));

    }

    // 击败 82
    List<List<Integer>> list = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    public List<List<Integer>> subsets(int[] nums) {

        subsets(nums,0);
        return list;
    }
    public void  subsets(int[] nums,int index) {
        if(path.size()<=nums.length){
            list.add(new ArrayList<>(path));
        }else{
            return ;
        }

        for(int i = index ; i<nums.length;i++){
            path.add(nums[i]);
            subsets(nums,i+1);
            path.remove(path.size()-1);

        }


    }

    public List<List<Integer>> subsets2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        for(int i = 0;i<=nums.length;i++){
            solution(result,intList,0,0,nums,i);
        }
        return result;
    }

    void  solution(List<List<Integer>> result , List<Integer> intList ,int start ,int index ,int[] nums,int k){
        if(index>= k ){
            result.add(new ArrayList<>(intList));
            return;
        }

        for(int i = start;i<nums.length;i++){
            intList.add((Integer)nums[i]);
            solution(result,intList,i+1,index+1,nums,k);
            intList.remove((Integer)nums[i]);
        }
    }
}

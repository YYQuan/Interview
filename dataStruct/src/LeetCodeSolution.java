package src;

import java.util.*;

public class LeetCodeSolution
{
    public static void main(String[] args) {
        LeetCodeSolution code   = new LeetCodeSolution();
//        int[] ints  = new int[]{-2,0,0,0,2,2,2};
        int[] ints  = new int[]{-1,0,1,2,-1,-4};
        List<List<Integer>> result = code.threeSum(ints);
        System.out.println(result);


    }


    public  List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> result = new HashSet<>();
        if(nums == null || nums.length<3)    return new ArrayList<>(result);

        Arrays.sort(nums);

        for(int i = 0  ; i< nums.length-2;i++){



            int tmp = nums[i];

            for(int p = i +1,q= nums.length-1;p<q;){


                int sum2 = nums[p]+nums[q];
                if(sum2 == -tmp){
                    List<Integer>  tmpList = new ArrayList<>();
                    tmpList.add(tmp);
                    tmpList.add(nums[p]);
                    tmpList.add(nums[q]);
                    result.add(tmpList);
                    p++;
                }else if(sum2 > -tmp){
                    if(nums[q]==nums[q-1]){
                        q--;
                        q--;
                    }else{
                        q--;
                    }
                }else{

                    if(nums[p]==nums[p+1]){
                        p++;
                        p++;
                    }else{
                        p++;
                    }
                }
            }
        }

        return new ArrayList<>(result);
    }
}

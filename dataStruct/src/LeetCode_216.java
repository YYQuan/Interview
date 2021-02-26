package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_216 {

    public static void main(String[] args) {
        LeetCode_216 code = new LeetCode_216();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
//        int[] ints = new int[]{10,1,2,7,6,1,5};
//        int[] ints = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] ints = new int[]{1,1,1,2};
        int[] ints = new int[]{10,1,2,7,6,1,1,5};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.combinationSum3(3,7));

    }
    List<List<Integer>> list = new ArrayList<>();
    List<Integer> path = new ArrayList<>();

    public List<List<Integer>> combinationSum3(int k, int n) {

        combinationSum3(k,n,1);
        return list;
    }

    public void combinationSum3(int k, int n,int index) {
        if(path.size()>k){
            return ;
        }

        if(path.size() == k&&n == 0 ) {
            list.add(new ArrayList<>(path));
            return;
        }

        for(int i = index ; i<10;i++){
            path.add(i);
            combinationSum3(k,n-i,i+1);
            path.remove(path.size()-1);
        }
    }

}

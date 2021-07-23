package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_216 {

    public static void main(String[] args) {
        LeetCode_216 code = new LeetCode_216();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
//        int[] ints = new int[]{10,1,2,7,6,1,5};
//        int[] ints = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] ints = new int[]{1,1,1,2};
//        int[] ints = new int[]{10, 1, 2, 7, 6, 1, 1, 5};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.combinationSum3(5, 17));
        System.out.println(code.combinationSum3_2(5, 17));

    }

    List<List<Integer>> list = new ArrayList<>();
    List<Integer> path = new ArrayList<>();

    public List<List<Integer>> combinationSum3(int k, int n) {

        combinationSum3(k, n, 1);
        return list;
    }

    public void combinationSum3(int k, int n, int index) {
        if (path.size() > k) {
            return;
        }

        if (path.size() == k && n == 0) {
            list.add(new ArrayList<>(path));
            return;
        }

        for (int i = index; i < 10; i++) {
            path.add(i);
            combinationSum3(k, n - i, i + 1);
            path.remove(path.size() - 1);
        }
    }

    public List<List<Integer>> combinationSum3_2(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        LinkedList<Integer> list = new LinkedList<>();

        combinationSum3(result, list, k, n, 1);
        return result;

    }

    public void combinationSum3(List<List<Integer>> result, LinkedList<Integer> list, int k, int n, int index) {

        if(n<0) return ;
        if(n==0 &&list.size()== k ) {
            result.add(new ArrayList<>(list));
            return;
        }


        for(int i = index ;i<10;i++){
            if(n>=i) {
                list.add(i);
                combinationSum3(result, list, k, n - i, i + 1);
                list.removeLast();
            }

        }
    }
}

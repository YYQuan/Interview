package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LeetCode_40 {

    public static void main(String[] args) {
        LeetCode_40 code = new LeetCode_40();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
//        int[] ints = new int[]{10,1,2,7,6,1,5};
//        int[] ints = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//        int[] ints = new int[]{1,1,1,2};
        int[] ints = new int[]{10,1,2,7,6,1,1,5};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.combinationSum22(ints,8));

    }
    List<List<Integer>> list = new ArrayList<>();
    List<Integer> path = new ArrayList<>();

    // 去重
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        dfs(candidates,target,0);
        return list;
    }

    private void dfs(int[] candidates, int target,int index){
        if(target==0){
            list.add(new ArrayList<>(path));
            return;
        }
        for(int i=index;i<candidates.length;i++){
            if(candidates[i]<=target){
                if(i>index&&candidates[i]==candidates[i-1]){
                    continue;
                }
                path.add(candidates[i]);
                dfs(candidates,target-candidates[i],i+1);
                path.remove(path.size()-1);
            }
        }
    }




/*
        // 超时
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> result = new ArrayList<>();
        combinationSum2(candidates,new boolean[candidates.length],target,new ArrayList<>(),result);
        return result;
    }

    public void combinationSum2(int[] candidates,boolean[] isReads, int target,List<Integer> list ,List<List<Integer>> lists) {

        if(!isLegal(list,target)){
            return ;
        }
        if(isResult(list,target)){
//            if(!isContain(lists,list)) {
//
//            }
            lists.add(list);
            return;
        }


        boolean needExit =true;
        for(boolean  isread :isReads){
            if(!isread) {
                needExit = false;
                break;
            }
        }

        if(needExit) return ;

        int start = 0;
        if(list.size()>0){
            start = list.get(list.size()-1);
        }
        for(int i = 0 ; i<candidates.length;i++){

            boolean[] isReadsTmp = isReads.clone();
            List<Integer> listTmp = new ArrayList<>(list);
            if(candidates[i]<start)  continue;
            if(!isReadsTmp[i]){
                isReadsTmp[i] = true;
                listTmp.add(candidates[i]);
                combinationSum2(candidates,isReadsTmp,target,listTmp,lists);
            }
        }
    }



    boolean isResult(List<Integer> list, int target){
        int sum  = 0;

        for(int value : list){
            sum+=value;
        }
        return sum == target;
    }

    boolean isLegal(List<Integer> list, int target){
        int sum  = 0;

        for(int value : list){
            sum+=value;
            if(target<sum){
                return false;
            }
        }

        return true;
    }

*/


    public List<List<Integer>> combinationSum22(int[] candidates, int target) {
        List<List<Integer>>  result   = new ArrayList<>();

        Arrays.sort(candidates);

        solution2(candidates,target,0,result,new ArrayList<>());
        return result;

    }

    public void  solution2(int[] candidates ,int target ,int index,List<List<Integer>> result ,List<Integer> ints){

        if(target<0) return ;
        if(target==0){
            result.add(new ArrayList<>(ints));
            return ;
        }

        for(int i =  index; i< candidates.length;i++){

            if(i>index&&candidates[i]==candidates[i-1])  continue;

            ints.add(candidates[i]);
            solution2(candidates,target-candidates[i],i+1,result,ints);
            ints.remove((Object)candidates[i]);


        }
    }
}

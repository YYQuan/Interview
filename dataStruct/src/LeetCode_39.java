package src;

import java.util.*;

public class LeetCode_39 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_39 code = new LeetCode_39();
//        int[] ints = new int[]{2,3,6,7};
//        int[] ints = new int[]{7};
        int[] ints = new int[]{2,3,5};
//        System.out.println(code.combinationSum(ints,7));
//        System.out.println(code.combinationSum(ints,8));

//        System.out.println(code.combinationSum2(ints,8));
        System.out.println(code.combinationSum3(ints,8));

    }


    List<List<Integer>> lists = new ArrayList<>();
    List<Integer> list = new ArrayList<>();
    //
    // 击败 百分之 50
    // index  ：赋予去重的效果  去重都可以参考这个思路， 用过的就不再用
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
//        Arrays.sort(candidates);
        solution(candidates,target,0);
        return lists;
    }
    public void solution(int[] candidates, int target,int index) {

            if(target == 0){
                lists.add(new ArrayList<>(list));
                return ;
            }

            if(target < 0 ) return ;

            for(int i = index ;i<candidates.length;i++){
                list.add(candidates[i]);
                solution(candidates,target-candidates[i],i);
                list.remove(list.size()-1);
            }

    }


        // 击败 百分之5
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        combinationSum(candidates,target,new ArrayList<>(),result);
        return result;

    }

    public void  combinationSum(int[] candidates, int target,List<Integer> list,List<List<Integer>> lists) {

        if(isResult(list,target)){
            if(!isContain(lists,list)) {
                lists.add(list);
            }
            return;
        }
        if(!isLegal(list,target)){
            return ;
        }




        for(int value : candidates){
            List<Integer> tmpList = new ArrayList<>(list);
            tmpList.add(value);
            combinationSum(candidates, target, tmpList, lists);

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

    boolean isContain(List<List<Integer>> lists, List<Integer> list){
        for(List<Integer> listTmp : lists){
            if(isSame(listTmp,list)){
                return true;
            }
        }
        return false;
    }

    boolean isSame(List<Integer> l1, List<Integer> l2){

        HashMap<Integer,Integer> map1 = new HashMap<>();
        HashMap<Integer,Integer> map2 = new HashMap<>();

        for(int v: l1){
            map1.put(v,map1.getOrDefault(v,0)+1);
        }
        for(int v: l2){
            map2.put(v,map2.getOrDefault(v,0)+1);
        }


        for(int key :map1.keySet()){
            int count1 =map1.get(key);
            int count2 =map2.getOrDefault(key,-1);
            if(count1!=count2) return false;
        }

        return true;



    }



    public List<List<Integer>> combinationSum3(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        solution3(result ,null,candidates,target,0);
        return result;
    }

    public void  solution3(List<List<Integer>>  result , List<Integer> tmpInts ,int[] candidatas ,int target,int index){

        if(tmpInts == null){
            tmpInts = new ArrayList<>();

        }
        if(target <0 ){
            return ;
        }
        if(target == 0 ){
            if(!result.contains(tmpInts)) {
                result.add(tmpInts);
            }
            return ;
        }

        for(int i = index ;i<candidatas.length;i++){

            if(i>0&&candidatas[i]==candidatas[i-1]) continue;

            List<Integer> ints = new ArrayList<>(tmpInts);
             ints.add(candidatas[i]);

            solution3(result,ints ,candidatas,target-candidatas[i],i);
        }

    }
}

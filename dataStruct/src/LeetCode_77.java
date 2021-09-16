package src;

import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_77 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_77  code = new LeetCode_77();
        System.out.println(code.combine(3,3));
        System.out.println(code.combine2(3,3));

    }

    // 击败 5
    public List<List<Integer>> combine(int n, int k) {

        List<List<Integer>> result = new ArrayList<>();
        combine(n,k,new boolean[n+1],new ArrayList<>(),result);
        return result;
    }

    public void combine(int n, int k,boolean[] isReads,List<Integer> list , List<List<Integer>> lists) {

        if(list.size()== k){

            lists.add(list);
            return ;
         }

        int i = 1;
        // 有值的时候， 只往后找 ，这样就能避免排列不同引起的多余解
        if(list.size()>0){
            i = list.get(list.size()-1);
        }
         for( ; i<=n;i++){
             boolean[] tmpReads = isReads.clone();
             List<Integer> tmpList = new ArrayList<>(list);
             if(!tmpReads[i]){
                 tmpReads[i] = true;
                 tmpList.add(i);
                 combine(n,k,tmpReads,tmpList,lists);
             }
         }
    }


    public List<List<Integer>> combine2(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        solution(result,new ArrayList<>(),0,1,n,k);
        return result;
    }

    void  solution(List<List<Integer>> result,List<Integer> ints,int index ,int start,int n ,int k){
        if(index>=k){
            result.add(new ArrayList<>(ints));
            return;
        }

        for(int i =start;i<=n;i++){
            ints.add((Integer) i);
            solution(result,ints,index+1,i+1,n,k);
            ints.remove((Integer)i);
        }

    }
}

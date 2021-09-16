package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_131 {

    public static void main(String[] args) {
        LeetCode_131 code = new LeetCode_131();
        Integer[] ints = new Integer[]{236,104,701,null,227,null,911};

//        List<List<String>>  result = code.partition("aab");
//        List<List<String>>  result2 = code.partition2("aab");
        List<List<String>>  result2 = code.partition2("efe");

//        System.out.println(result);
        System.out.println(result2);
//        TreeNodeUtil.printOrderTree(result);
    }


    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        partition(s,new ArrayList<>(),result);
        return result;
    }

    // 击败 15
    public void partition(String s,List<String> list ,List<List<String>> lists) {
        List<String> copyList = new ArrayList<>(list);
        if(s.isEmpty()) {
            if(list.size()>0){
                lists.add(list);
            }
            return ;
        }
        if(s.length()==1) {
            copyList.add(s);
            lists.add(copyList);
            return;
        }

        for(int i = 1 ; i<=s.length();i++){
            List<String> tmpList = new ArrayList<>(list);
            String subStr = s.substring(0,i);

//            System.out.println("s "+subStr);
            if(isIllegat(subStr)){
                tmpList.add(subStr);
                partition(s.substring(i),tmpList,lists);
            }
        }


    }




    // 是否是回文
    public  boolean isIllegat(String s){
        for(int i = 0 ; i< (s.length()+1)/2;i++){

            if(s.charAt(i)!=s.charAt(s.length()-1-i)){
                return false;
            }
        }
        return  true;
    }


    public List<List<String>> partition2(String s) {
        List<List<String>>  result = new ArrayList<>();
        partition2(result, new ArrayList<>(),s,0);
        return result;
    }

    public void partition2(List<List<String>> result ,List<String> list ,String s,int index) {
        if(index>=s.length()) {
            result.add(new ArrayList<>(list));
            return;
        }
        for(int i = index; i<s.length();i++){
            int j = i+1;
            String subStr = s.substring(i,j);

            // 要注意  efe 中 ef  不是回文 但是efe 是回文
                while (j<=s.length()) {
                    if(isLegal(subStr)) {
                        list.add(subStr);
                        partition2(result, list, s, j);
                        list.remove(list.size() - 1);
                    }
                    j++;
                    if (j > s.length()) return;
                    subStr = s.substring(i, j);

            }

            return;
        }
    }

    boolean isLegal(String s){

        for(int i = 0 ; i<s.length()/2;i++){
            if(s.charAt(i)!=s.charAt(s.length()-1-i)){
                return false;
            }
        }
        return true;

    }
}

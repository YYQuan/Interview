package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_131 {

    public static void main(String[] args) {
        LeetCode_131 code = new LeetCode_131();
        Integer[] ints = new Integer[]{236,104,701,null,227,null,911};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

//        List<String> result = code.restoreIpAddresses("25525511135");
//        List<String> result = code.restoreIpAddresses("010010");
//        boolean result = code.isIllegat("01010");
        List<List<String>>  result = code.partition("aa");

        System.out.println(result);
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

            System.out.println("s "+subStr);
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

}

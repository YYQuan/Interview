package src;

import javafx.util.Pair;

import java.util.*;

public class LeetCode_126 {

    public static void main(String[] args) {
        LeetCode_126 code = new LeetCode_126();

//        String beginWord = "hit", endWord = "cog";
//        String[] wordList = new String[]{"hot","dot","dog","lot","log","cog"};

        String beginWord = "hit", endWord = "cog";
        String[] wordList = new String[]{"hot","dot","dog","lot","log","cog"};
//        "hot" "dog" ["hot","cog","dog","tot","hog","hop","pot","dot"]
//        String beginWord = "hot", endWord = "dog";
//        String[] wordList = new String[]{"hot","cog","dog","tot","hog","hop","pot","dot"};
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(wordList));

        List<List<String>>  result = code.findLadders(beginWord,endWord,list);

        for( List<String>  l  :result){
            System.out.println(l.toString());
        }

//        int result = code.ladderLength(beginWord,endWord,lists);

//        System.out.println(result);
//        System.out.println(code.numSquares2(13));

    }

    // 和 127 进阶 先挂起
//    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
//
//    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    // 常规递归 超时  可能涉及到图论 先放一下
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {

        List<List<String>> result = new ArrayList<>();
        List<String> list = new ArrayList<>();

        if(wordList== null) return result;
        if(!wordList.contains(endWord)) return result;

        list.add(beginWord);
        findLadders(result,list,beginWord,endWord,wordList);
        return result;

    }
    public void  findLadders(List<List<String>>  result,List<String>  list,String beginWord, String endWord, List<String> wordList) {


        if(list!=null &&list.size()>0){
            if(list.get(list.size()-1).equalsIgnoreCase(endWord)){

                if(result.size() ==0){
                    result.add(new ArrayList<>(list));
                }else{
                    if(result.get(0).size() > list.size()){
                        result.clear();
                        result.add(new ArrayList<>(list));
                    }else  if(result.get(0).size() < list.size()){

                    }else{
                        result.add(new ArrayList<>(list));
                    }
                }
                return ;

            }
        }


        String lStr = list.get(list.size() - 1);

        for(String s :wordList){

            if(!list.contains(s)){
               if(isIllegal(lStr,s)) {
                    list.add(s);
                    findLadders(result, list, beginWord, endWord, wordList);
                    list.remove(s);
                }
            }
        }
    }

    boolean isIllegal(String s1,String s2 ){

        if(s1.length() !=s2.length()) return false;
        int count = 0;
        for(int i = 0 ; i<s1.length();i++){

            if(s1.charAt(i)!=s2.charAt(i)){
                count++;
                if(count>1) return false;
            }
        }

        return true;
    }
}

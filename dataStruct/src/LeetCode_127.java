package src;

import javafx.util.Pair;

import java.util.*;

public class LeetCode_127 {

    public static void main(String[] args) {
        LeetCode_127 code = new LeetCode_127();

//        int[] ints1 = new int[]{1,2};
        int[] ints1 = new int[]{1,2,2,1};
//        int[] ints1 = new int[]{1,2,3,4};
//        int[] ints1 = new int[]{1,2,3,4,5};
//        int[] ints1 = new int[]{5,1,2,3,4,5,6,7,8};
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node2.left = node3;
        node2.right = node4;
        TreeNode node1 = new TreeNode(1);
        node1.right= node2;

//        String beginWord = "hit", endWord = "cog";
//        String[] wordList = new String[]{"hot","dot","dog","lot","log","cog"};

//        String beginWord = "hit", endWord = "cog";
//        String[] wordList = new String[]{"hot","dot","dog","lot","log"};
//        "hot" "dog" ["hot","cog","dog","tot","hog","hop","pot","dot"]
        String beginWord = "hot", endWord = "dog";
        String[] wordList = new String[]{"hot","cog","dog","tot","hog","hop","pot","dot"};


        List<String>  lists = new ArrayList<String>();
        for(String s :wordList){
            lists.add(s);
        }

        int result = code.ladderLength(beginWord,endWord,lists);

        System.out.println(result);
//        System.out.println(code.numSquares2(13));

    }

    // 超时  可能涉及到 双端 bfs   这部分 还没了解 先放一放
    // 但是用例都是跑完的了
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {

        int result = 0 ;

        if(!wordList.contains(endWord)) return result ;


        HashSet<String> set = new HashSet<>();
        LinkedList<Pair<String,Integer>> pairs = new LinkedList<>();
        pairs.add(new Pair<>(beginWord,1));
        while(!pairs.isEmpty()){
            Pair<String,Integer> pair= pairs.removeFirst();

            if(pair.getKey().equalsIgnoreCase(endWord)){
                return pair.getValue();
            }

            if(set.contains(pair.getKey())) {
                continue;
            }
            set.add(pair.getKey());

            wordList.remove(pair.getKey());
            LinkedList tmpList = new LinkedList<String>(wordList);
            List<String> strings = getSimpleWord(tmpList,pair.getKey());

            if(strings.isEmpty()) {
                continue;
            }

            for(String s : strings){
                pairs.add(new Pair<>(s,pair.getValue()+1));
            }


        }

        return result;

    }

    public List<String>  getSimpleWord(List<String> words,String word){
        List<String> list = new ArrayList<>(words);


        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String s = iterator.next();

            if(!isOnlyOneCharDiff(s,word)){
                iterator.remove();
            }else if(s.equalsIgnoreCase(word)){
                iterator.remove();
            }

        }

        return list;

    }

    public boolean isOnlyOneCharDiff(String w1,String w2){

        if(w1.length()!=w2.length()) return false;
        int count =0;

        for(int i = 0  ; i<w1.length();i++){
            if(w1.charAt(i)!=w2.charAt(i)) count++;
            if(count>1) return false;
        }

        return true;
    }


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

}

package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LeetCode_139 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_139 code = new LeetCode_139();
//        int[] ints  = new int[]{1,2,5};
//        int[] ints  = new int[]{2};
//        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 5,  1,5};
//        int[] ints = {14,9,8,4,3,2};
//        int[] ints = {1};
        int[] ints = {3};
//        String[] strs = new String[]{"leet", "code"};
//        String s =  "leetcode";

//        String s =  "catsandog";
//        String[] strs = new String[]{"cats", "dog", "sand", "and", "cat"};

//        String s =  "bb";
//        String[] strs = new String[]{"a","b","bbb","bbbb"};

//        String s =  "catsandog";
//        String[] strs = new String[]{"cats","dog","sand","and","cat"};

//        String s =  "ccacccbcab";
//        String[] strs = new String[]{"cc","bb","aa","bc","ac","ca","ba","cb"};

        String s = "applepenapple";
        String[] strs = new String[]{"apple", "pen"};


        System.out.println(code.wordBreak(s, Arrays.asList(strs)));
//        System.out.println(code.wordBreakD(s, Arrays.asList(strs)));
        System.out.println(code.wordBreak2(s, Arrays.asList(strs)));
        System.out.println(code.wordBreak3(s, Arrays.asList(strs)));

    }

    //
    // 击败 18
    // wordDict 可以 复用 ，那么 wirdDict 肯定就不能 作为 增加的维度
    // 但是 s 长度来作为增加的维度 咋处理呀 ,想不到 但是 demo里 就使用长度来作为 增加的维度的
    public boolean wordBreakD(String s, List<String> wordDict) {

        if (s.length() == 0) return false;
        if (wordDict.size() == 0) return false;
        HashSet<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length()];

        for (int i = 0; i < s.length(); i++) {
            // 检索 是否有匹配的字符
            for (int j = 0; j <= i; j++) {
                String subStr = s.substring(j, i + 1);
                if (set.contains(subStr)) {
//                    System.out.println("subStr "+subStr);
//                    System.out.println("j-subStr.length() "+(j-subStr.length()));
//                    System.out.println("dp[j-subStr.length()]  "+((j-subStr.length()>0)?dp[j-subStr.length()]:0));

                    if (j == 0 || dp[i - subStr.length()]) {
                        dp[i] = true;
                    }
                }


            }

        }

        return dp[s.length() - 1];
    }


    // 常规 递归  超时
    public boolean wordBreak(String s, List<String> wordDict) {
        if (s.length() == 0) return false;
        return check(s, wordDict);
    }

    public boolean check(String s, List<String> wordDict) {
//        System.out.println("s --> " +s);

        if (s.length() == 0) return true;

        for (String tmpS : wordDict) {
//            System.out.println("tmpS --> " +tmpS);

            if (s.startsWith(tmpS)) {
//                System.out.println("tmpS -->tmpS.startsWith(tmpS) " +tmpS.startsWith(tmpS));

                boolean result = check(s.substring(tmpS.length()), wordDict);
                if (result) return true;
            }

        }

        return false;

    }


    public boolean wordBreak2(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        if (wordDict == null || wordDict.size() == 0) return false;
        return wordBreak2(s, wordDict, 0);
    }

    public boolean wordBreak2(String s, List<String> wordDict, int index) {

        if (index >= s.length()) return true;

        for (int i = index + 1; i <= s.length(); i++) {

            String tmp = s.substring(index, i);
            if (wordDict.contains(tmp)) {
                if (wordBreak2(s, wordDict, i)) {
                    return true;
                }

            }

        }
        return false;

    }

    public boolean wordBreak3(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        if (wordDict == null || wordDict.size() == 0) return false;

        boolean[] dp = new boolean[s.length()];
        Arrays.fill(dp, false);

        for (int i = 0; i < s.length(); i++) {

            if (i != 0 && !dp[i-1]) continue;

            for (int j = i; j < s.length(); j++) {
                String str = s.substring(i, j + 1);
                if (wordDict.contains(str)&&!dp[j]) {
                    if (i == 0 || dp[i - 1]) {
                        dp[j] = true;
                    }
                }
            }
        }
        return dp[s.length() - 1];
    }
}

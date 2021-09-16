package src;

import java.util.HashSet;

public class LeetCode_242 {

    public static void main(String[] args) {
        LeetCode_242 code = new LeetCode_242();
        String s ="anagram";
        String t ="nagaraa";
        boolean result = code.isAnagram(s,t);
        boolean result2 = code.isAnagram2(s,t);
        System.out.println(result);
        System.out.println(result2);

    }

    public boolean isAnagram(String s, String t) {

        if(s.length()!=t.length()) return false;

        int[] countS = new int[26];
        int[] countT = new int[26];

        for(int i = 0 ; i<s.length();i++){
            countS[s.charAt(i)-'a'] ++;
        }

        for(int i = 0 ; i<t.length();i++){
            countT[t.charAt(i)-'a'] ++;
        }

        for(int i =  0 ; i<countS.length;i++){
            if(countS[i]!=countT[i]) return false;
        }

        return true;
    }

    public boolean isAnagram2(String s, String t) {
        if(s == null && t ==null)  return true;
        if(s ==null)  return false;
        int[]   s1Tmp = new int[26];
        int[]   s2Tmp = new int[26];


        if(s.length() != t.length()) return false;

        for(int i =0;i<s.length();i++){
            char c = s.charAt(i);
            s1Tmp[c -'a']++;
            char ct = t.charAt(i);
            s2Tmp[ct -'a']++;

        }
        for(int i = 0 ;i<26;i++){
            if(s1Tmp[i] != s2Tmp[i]) return false;
        }

        return true;

    }
}

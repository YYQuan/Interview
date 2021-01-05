package src;

import java.util.HashSet;

public class LeetCode_242 {

    public static void main(String[] args) {
        LeetCode_242 code = new LeetCode_242();
        String s ="anagram";
        String t ="nagaram";
        boolean result = code.isAnagram(s,t);
        System.out.println(result);

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

}

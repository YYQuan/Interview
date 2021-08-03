package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeetCode_438 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LeetCode_438 code = new LeetCode_438();
//        int[] ints  = new int[]{1,2,3};
        int[] ints = {1, 5, 11, 5};
//        int[] ints = {1, 2, 3, 5};
//        System.out.println(code.solution(ints));
//        System.out.println(code.solutionDync(ints));
//        String s = "cbaebabacd";
//        String p = "abc";
//        String s = "ababa";
//        String p = "ab";
        String s = "ababababab";
        String p = "aab";
//        String s = "baa";
//        String p = "aa";
        List result1 = code.findAnagrams(s,p);
        System.out.println(result1);
        List result3 = code.findAnagrams3(s,p);
        List result2 = code.findAnagrams2(s,p);

        System.out.println(result3);
        System.out.println(result2);
    }


    // 1.0 解法   用map 来记录  元素出现的次数会超时
    // 技巧重点， 不要用 map来记录  元素 出现的次数， 要用 int[] 来， 题中说到 只有26个小写字母 所以用index来表示 c -'a' 即可
    // 思路是 判断 每一个窗口下 的元素次数  是否相同
    // leetcode战绩  5%
    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> list = new ArrayList();
        if(s.length()<p.length()) {
          return list ;
        }

        int i = 0 ;
        int j = p.length()-1;
        // 不仅 字符要有 而且  次数也要对应上
        int[] charCount = new int[26];
        for(int q = 0 ; q < p.length();q++){
            int index = p.charAt(q)-'a';
            charCount[index]+=1;
        }

        while(j<s.length()){
            if(isContainAllElements(s,i,j,charCount)){
                list.add(i);
            }
            i++;
            j++;
        }


        return list;
    }



    public boolean isContainAllElements(String s,  int l , int r,int[] charCount){

        String subS = s.substring(l,r+1);


        int[] charSubSCount = new int[26];
        for(int i = 0 ; i < subS.length();i++){
            int index = subS.charAt(i)-'a';
            charSubSCount[index]+=1;
        }

        for(int i = 0 ; i<26;i++){

            if(charCount[i]!=0&&charCount[i]!=charSubSCount[i]){
                return false;
            }
        }
        return true;

    }


    // 解法2.0 思路是 找到[l,r] 中  p中有的元素个数相同的 r index  并且 [l,r] 的长度 等于 p的长度
    // 搞了很久发现 比思路要难处理很多。 没必要

    //解法3.0  思路和1 一样 但是 要利用  [l,r] 和[l+1,r+1]  只差 [l+1],[r+1]这两个元素的值 这个特性来处理
    //所以就要记录下，[l,r]的计数情况
    // leetcode战绩  60%
    public List<Integer> findAnagrams3(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if(s.length()<p.length()) return result;

        int[] pElementCount = new int[26];

        for(int i = 0;i<p.length();i++){
            pElementCount[p.charAt(i)-'a'] ++;
        }

        int[] currentElementCount = null;

        int l=0,r=p.length()-1;
        while(r<s.length()){

            if(currentElementCount==null){
                currentElementCount = new int[26];
                for(int i = l ; i<=r;i++){
                    currentElementCount[s.charAt(i)-'a']++;
                }
            }else{
                // 窗口向右移一格

                currentElementCount[s.charAt(l)-'a']--;

                l++;
                r++;

                if(r>=s.length()) break;

                currentElementCount[s.charAt(r)-'a']++;
            }


            if(equalArrayElementCount(currentElementCount,pElementCount)){
                result.add(l);
            }

        }

        return result;
    }

    public boolean equalArrayElementCount(int[] nums1, int[] nums2){

        if(nums1.length!=nums2.length) return false;

        for(int i = 0 ;i<nums1.length;i++){
            if(nums1[i]!=nums2[i]) return false;
        }
        return true;
    }

    public List<Integer> findAnagrams2(String s, String p) {

        List<Integer>  result = new ArrayList<>();
        if(p==null||p.length()==0||s==null||s.length()<p.length()) return result;

        int[] tmp  = new int[26];
        int[] tmpS2  = new int[26];

        for(char c  : p.toCharArray()){
            tmp[c-'a']++;
        }
        String s2  = s.substring(0,p.length());
        for(char c  : s2.toCharArray()){
            tmpS2[c-'a']++;
        }

        if(isLegal(tmp,tmpS2)){
            result.add(0);
        }

        for(int i = p.length();(i)<s.length();i++){

            tmpS2[s.charAt(i)-'a']++;
            tmpS2[s.charAt(i-p.length())-'a']--;
            if(isLegal(tmp,tmpS2)){
                result.add(i-p.length()+1);
            }

        }
        return result;
    }

    boolean isLegal(int[] ints1 ,int[] ints2){


        for(int i = 0 ;i<26;i++){
            if(ints1[i]!=ints2[i]) return false;
        }
        return true;
    }
}

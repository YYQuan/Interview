package src;

import java.util.HashMap;

public class LeetCode_205 {

    public static void main(String[] args) {
        LeetCode_205 code = new LeetCode_205();
        String pattern = "foo";
        String s = "bar";
        boolean result = code.isIsomorphic(pattern,s);
        System.out.println(result);

    }

    public boolean isIsomorphic(String s, String t) {

        if(s.length()!=t.length()) return false;

        HashMap<Character,Character> map = new HashMap<>();

        for(int i =0  ;i<t.length();i++){
            char sChar = s.charAt(i);
            if(!map.keySet().contains(sChar)){
                if(!map.containsValue(t.charAt(i))){
                    map.put(sChar,t.charAt(i));
                }else{
                    return false;
                }
            }else{
                char c = map.get(sChar);
                if(c != t.charAt(i)){
                    return false;
                }
            }


        }

        return true;
    }

}

package src;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class LeetCode_290 {

    public static void main(String[] args) {
        LeetCode_290 code = new LeetCode_290();
        String pattern = "abba";
        String s = "dog dog dog dog";
        boolean result = code.wordPattern(pattern,s);
        System.out.println(result);

    }

    public boolean wordPattern(String pattern, String s) {
        HashMap<Character,String> map = new HashMap<>() ;

        String[] strings = s.split(" ");

        // 元素个数要对得上
        if(pattern.length()!=strings.length) return false;

        for(int i = 0 ; i<pattern.length();i++){
            char c = pattern.charAt(i);
            if(map.containsKey(c)){
                String cStr = map.get(c);
                //  p中 对应的 字符串要一直
                if(!strings[i].equalsIgnoreCase(cStr)){
                    return false;
                }
            }else{
                // 字符串 只能对应一种
                if(!map.containsValue(strings[i])) {
                    map.put(c, strings[i]);
                }else{
                    return false;
                }
            }
        }


        return true;
    }
}

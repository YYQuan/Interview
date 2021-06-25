package src;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class LeetCode_49 {

    public static void main(String[] args) {
        LeetCode_49 code = new LeetCode_49();
        String[] strings = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};
//        String[] strings = new String[]{"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"};
//        String[] strings = new String[]{"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaaaaaaaab"};

  //      List<List<String>> result = code.groupAnagrams(strings);

//        for(List<String> ss: result){
//            ;
//            System.out.println(Arrays.toString(ss.toArray()));
//
//        }

        List<List<String>> result3 = code.groupAnagrams3(strings);
        for(List<String> ss: result3){
            ;
            System.out.println(Arrays.toString(ss.toArray()));

        }
//        System.out.println(Arrays.toString(ints));
//        System.out.println(result);




    }

    public List<List<String>> groupAnagrams(String[] strs) {

        Map<String, List<String>> collect = Arrays.stream(strs).collect(Collectors.groupingBy(s -> {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            return new String(chars);
        }));


        List<List<String>> result = new ArrayList<>();

        for(String s :strs){

            boolean isNewWord = true;
            Iterator<List<String>> iterable =result.iterator();
            while(iterable.hasNext()){
                List<String> listStr = iterable.next();
                if(listStr.size()>0){
                    if(isAnagrams2(listStr.get(0),s)){
                        isNewWord  = false;
                        listStr.add(s);
                    }

                }


            }
            if(isNewWord){
                List<String> listNewWord = new ArrayList<>();
                listNewWord.add(s);
                result.add(listNewWord);
            }


        }
        return result;
    }



    public boolean isAnagrams3(String s1,String s2){

        int[] charCount1 = new int[26];
        int[] charCount2 = new int[26];


        for(char c :s1.toCharArray()){
            charCount1[c-'a'] +=1;
        }
        for(char c :s2.toCharArray()){
            charCount2[c-'a'] +=1;
        }


        for(int i =0;i<26;i++){
            if(charCount1[i]!=charCount2[i]) return false;
        }


        return true;
    }

    public boolean isAnagrams2(String s1,String s2){

        String tmpS1 = new String(s1);
        String tmpS2 = new String(s2);

        String sortS1  = quickSort(tmpS1);
        String sirtS2 = quickSort(tmpS2);

        return (sortS1.equals(sirtS2));
    }
    public boolean isAnagrams(String s1,String s2){

        if(s1.length()!=s2.length()) return false;
        HashMap<Character,Integer> map =new HashMap<>();

        for(int i = 0 ; i<s1.length();i++){
            if(map.get(s1.charAt(i))==null){
                map.put(s1.charAt(i),1);
            }else{
                int count = map.get(s1.charAt(i));
                map.put(s1.charAt(i),count+1);
            }
        }


        for(int i =0 ;i<s2.length(); i++ ){
            if(map.get(s2.charAt(i))==null){
                return false;
            }else{
                int count = map.get(s2.charAt(i));
                if(count<=0)  return false;

                map.put(s2.charAt(i),count-1);
            }
        }

        for(Character c : map.keySet()){
            int count = map.get(c);
            if(count!=0) return false;
        }

        return true;
    }


    public String  quickSort(String s){
        if(s.length()<=1) return s;
        char[] chars = s.toCharArray();

        quickSort(chars,0,chars.length-1);

        return new String(chars);
    }

    public void  quickSort(char[] chars ,int start ,int end){
        if(end-start<=0) return ;


        int middle = handleIndex(chars,start,end);
        quickSort(chars,start,middle-1);
        quickSort(chars,middle+1,end);

    }

    public int handleIndex(char[] chars ,int start ,int end){
        int randomIndex = new Random().nextInt(end-start)+start;
        swap(chars,randomIndex,start);

        char base = chars[start];
        int i = start+1;
        int j = end;

        while(true){
            while(i<end&&chars[i]<base){
                i++;
            }

            while(j>start&&chars[j]>base){
                j--;
            }

            if(i<j){
                swap(chars,i,j);
                i++;
                j--;
            }else{
                break;
            }

        }


        swap(chars,start,j);

        return j;

    }


    public void swap(char[] chars ,int l,int r){
        char tmp = chars[l];
        chars[l] =chars[r];
        chars[r] = tmp;
    }


    public List<List<String>> groupAnagrams2(String[] strs) {
        HashMap<NodeStrKey,List<String>> map = new HashMap<>();

        for(String s :strs){
            NodeStrKey key = new NodeStrKey(s);

            if(map.containsKey(key)){
                map.get(key).add(s);
            }else{
                List<String> list = new ArrayList<String>();
                list.add(s);
                map.put(key,list);
            }

        }

        return new ArrayList( map.values());
    }


    // 质数不要用2  2这个质数  参数质数相乘的时候  2^32 int就变成0了
    static int[] nums = new int[]{3,5,7,11,13,17,23,29,31,37,41,43,47,59,61,67,71,73,79,83,97,101,103,107,109,113};
    //是否是异位词的判断的优化  设计一种 hash 算法 让 List<String> 的hash值 符合 区分条件
    // 然后用 hash map来处理。
    // 有大神用  26个质数 的乘积来做 hash算法
    //
    class  NodeStrKey{

        @NotNull
        String s ;

        public NodeStrKey(String s) {
            this.s = s;
        }


        // 用质数 相乘 来做 hash  但是 hash 只是 初步判断 是不能做绝对判断的条件的。
        // 一定要重写一下 equals函数。

        //如果长度相等 hash 也相等的话 基本就能equals了 但是数学怎么证明就不确定了
        // 而且这个 equals函数 在 字符长度太长的时候 是有bug的，但是对于 常规字符 就是ok的
        //
        @Override
        public boolean equals(Object o) {
            if(o instanceof  NodeStrKey){
                NodeStrKey that = (NodeStrKey)  o;
                if(that.s!=null&&that.s.length()!=this.s.length()) return false;
                return o.hashCode()== hashCode();
            }else{
                return false;
            }


        }

        @Override
        public int hashCode() {
//            long hashCode =  1;
            int hashCode =  1;
            for(char c :s.toCharArray()){
                hashCode*=nums[c-'a'];
            }

            //
            if(hashCode==0) hashCode =1;
            System.out.println("s "+s+"  hashCode :"+hashCode);
            System.out.println("s "+s+"  (int) hashCode :"+ (int)hashCode);
            return (int)hashCode;
        }
    }


    static int[] nums3 = new int[]{3,5,7,11,13,17,23,29,31,37,41,43,47,59,61,67,71,73,79,83,97,101,103,107,109,113};
    public List<List<String>> groupAnagrams3(String[] strs) {

        List<List<String>>  result = new ArrayList<>();
        List<String>  strsList = Arrays.stream(strs).collect(Collectors.toList());
        List<String>  list = new ArrayList<>();



        HashMap<Long,List<String>> map   = new HashMap<>();

        for(String s : strs){
            long l = caculator(s);
            List<String> list1 =map.getOrDefault(l,new ArrayList<>());
            list1.add(s);
            map.put(l,list1);

        }

        for(List<String> l : map.values()){
            result.add(l);
        }
        return result;


    }

    long caculator(String s){

        long tmp = 1;
        for(char c :s.toCharArray()){
            tmp = tmp*nums3[c-'a'];
        }
        return tmp;
    }


}

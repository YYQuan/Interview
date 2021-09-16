package src;

public class LeetCode_345 {

    public static void main(String[] args) {
        LeetCode_345 code = new LeetCode_345();
//        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
//        String s = "A man, a plan, a canal: Panama";
        String s = "hello";
//        char[] s = new char[]{'h','e','l','l','o'};

       ;
//        System.out.println( code.reverseString(s));
        System.out.println( code.reverseVowels(s));




    }


    public  String reverseString(String s){
        if(s.length()<2) return s;
        char[] chars = new char[s.length()];
        s.getChars(0,s.length(),chars,0);
        int i = 0;// 从左往右找到第一个元音
        int j = s.length()-1;// 从右往左 找到第一个元音

        while(i<j){
            while(i<s.length()-1 &&!belongAEIOU(s.charAt(i))){
                i++;

            }

            while(0<j &&!belongAEIOU(s.charAt(j))){
                j--;
            }

            if(i<j){
                swap(chars,i++,j--);
            }
        }

        String result = new String(chars);
        return result;
    }

    void swap(char[] chars ,int l , int r){
        char t = chars[l];
        chars[l] = chars[r];
        chars[r] = t;
    }

    boolean belongAEIOU(char c){

        return c == 'A'||c == 'E'||c == 'I'||c == 'O'||c == 'U'||
                c == 'a'||c == 'e'||c == 'i'||c == 'o'||c == 'u';



    }


    public String reverseVowels(String s) {
        if(s == null || s.length()==0)  return s;
        int l = 0;
        int r = s.length()-1;
        char[] chars = s.toCharArray();
        while(l<r){
            while(l<s.length()&&!belongAEIOU(s.charAt(l))){
                l++;
            }

            if(l>r)
            {
                return new String(chars);
            }

            while(r<s.length()&&!belongAEIOU(s.charAt(r))){
                r--;
            }

            swap2(chars,l++,r--);


        }
        return new String(chars);
    }

    private void swap2(char[] s ,int l ,int r ){
        char  c = s[l];
        s[l]  = s[r];
        s[r]  = c;
    }

}

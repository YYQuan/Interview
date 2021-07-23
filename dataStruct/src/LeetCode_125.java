package src;

public class LeetCode_125 {

    public static void main(String[] args) {
        LeetCode_125 code = new LeetCode_125();
        int[] ints  = new int[]{2,7,11,15};
//        int[] result = code.solution(ints,9);
//        String s = "A man, a plan, a canal: Panama";
//        String s = "ab_a";
        String s = " ";
        boolean result = code.solution(s);
        boolean result2 = code.isPalindrome(s);
        System.out.println(result);
        System.out.println(result2);





    }


    public  boolean solution(String s){
        if(s.length()<2) return true;

        int i = 0;
        int j = s.length()-1;
        String lowerS = s.toLowerCase();
        while(i<j){
            if(!belongNumbersOrLetter(lowerS.charAt(i))){
                i++;
            }else if(!belongNumbersOrLetter(lowerS.charAt(j))){
                j--;
            }else if(lowerS.charAt(j)==lowerS.charAt(i)){
                i++;
                j--;
            }else{
                return false;
            }
        }

        return true;
    }


    boolean belongNumbersOrLetter(char  c){
        char charZ= 'z';
        char charA= 'a';
        char char9 = '9';
        char char0 = '0';
        if( c>=charA && c<= charZ)  return true;
        if( c>=char0 && c<= char9)  return true;
        return false;
    }

    public boolean isPalindrome(String s) {

        if(s == null) return false;
        if(s.length() == 0) return true;
        String tmpS = s.toLowerCase();
        int l = 0 ;
        int r = tmpS.length()-1;
        while(l<r){

            while(l<r&&!belongNumbersOrLetter(tmpS.charAt(l))){
                l++;
            };
            while(l<r&&!belongNumbersOrLetter(tmpS.charAt(r))){
                r--;
            };

            if(l==r) return true;

            if(l<r){
                if(!((tmpS.charAt(l)+"").equalsIgnoreCase(tmpS.charAt(r)+""))){
                    return false;
                }
            }


            l++;
            r--;
        }
        return true;

    }

}

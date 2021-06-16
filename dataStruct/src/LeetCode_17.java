package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_17 {

    public static void main(String[] args) {
        LeetCode_17 code = new LeetCode_17();
        Integer[] ints = new Integer[]{236,104,701,null,227,null,911};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

        List<String> result = code.letterCombinations("23");
        List<String> result2 = code.letterCombinations2("23");

        System.out.println(result);
        System.out.println(result2);
//        TreeNodeUtil.printOrderTree(result);
    }

    // 击败 26
    public List<String> letterCombinations(String digits) {
        List<String> strs = new ArrayList<>();

        letterCombinations(digits,"",strs);
        return strs;
    }

    public void  letterCombinations(String digits,String s ,List<String> list){
        if(digits==null||digits.isEmpty()) {
            if(!s.isEmpty()) {
                list.add(s);
            }
            return ;
        }


        String subStr = digits.substring(1);
        char c  = digits.charAt(0);
        char[] chars = getChars(c);

        for(char ctmp :chars){
            letterCombinations(subStr,s+ctmp,list);
        }


    }


    char[] getChars(char value){
        char[] result = null;
        switch(value){

            case '2':
                result =  new char[]{'a','b','c'};
                break;
            case '3':
                result =  new char[]{'d','e','f'};
                break;

            case '4':
                result =  new char[]{'g','h','i'};
                break;
            case '5':
                result =  new char[]{'j','k','l'};
                break;

            case '6':
                result =  new char[]{'m','n','o'};
                break;
            case '7':
                result =  new char[]{'p','q','r','s'};
                break;
            case '8':
                result =  new char[]{'t','u','v'};
                break;
            case '9':
                result =  new char[]{'w','x','y','z'};

                break;
             default:
                break;
        }

        return result;

    }

    public List<String> letterCombinations2(String digits) {
        List<String > result = new ArrayList<>();


        handleLetter(digits,"",result);

        return result;
    }

    public  void   handleLetter(String digits ,String currentS,List<String> list){

        if(digits == null || digits.length()<=0){
            if(!currentS.isEmpty()) {
                list.add(currentS);
            }
            return ;
        }

        String sub = digits.substring(1);

        for( char c: getChars(digits.charAt(0))){
            handleLetter(sub,currentS+c,list);
        }


    }
}

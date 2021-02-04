package src;

import java.util.Stack;

public class LeetCode_20 {

    public static void main(String[] args) {
        LeetCode_20 code = new LeetCode_20();
//        String s = "()(";
//        String s = "()[]{}";
//        String s = "[";
        String s = "){";
        boolean result = code.isValid(s);
        System.out.println(result);

    }


    public boolean isValid(String s) {
        if(s==null||s.length()==1) return false;

        Stack<Character> stack = new Stack<>();

        for(int i = 0 ; i<s.length();i++){
            char c = s.charAt(i);
            if(isLeftSign(c)){
                stack.push(c);
            }else if(isRightSign(c)){

                if(stack.empty()) return false;
                char popChar = stack.pop();
                if(!isPair(popChar,c)){
                    return false;
                }

            }else{
                throw new IllegalArgumentException();
            }


        }
        return stack.empty();
    }

    private boolean isPair(char c,char c2){
        return  (c == '('&&c2 ==')')||(c =='['&&c2==']')||(c =='{'&&c2 == '}');
    }

    private boolean isLeftSign(char c){

        return (c =='('||c=='['||c=='{');
    }

    private boolean isRightSign(char c){
        return (c ==')'||c==']'||c=='}');
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

    }

}

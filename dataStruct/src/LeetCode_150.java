package src;

import java.util.LinkedList;
import java.util.Stack;

public class LeetCode_150 {

    public static void main(String[] args) {
        LeetCode_150 code = new LeetCode_150();
//        String s = "()(";
//        String s = "()[]{}";
//        String s = "[";
//        String[] s = new String[]{"2","1","+","3","*"};
//        String[] s = new String[]{"4","13","5","/","+"};
        String[] s = new String[]{"10","6","9","3","+","-11","*","/","*","17","+","5","+"};

        int result = code.evalRPN(s);
        int result2 = code.evalRPN2(s);
        System.out.println(result);
        System.out.println(result2);

    }

    // 击败 54
    public int evalRPN(String[] tokens) {

        Stack<Integer> stack = new Stack<>();

        for(int i =0 ; i<tokens.length;i++){
            String s = tokens[i];

            if(isSign(s.charAt(0))&&s.length()==1){
                if(stack.empty()) {
                    throw new IllegalArgumentException();
                }
                int operatorNumber1 =stack.pop();
//                System.out.println("pop 1 "+operatorNumber1);
//                System.out.println("stack  "+stack);

                if(stack.empty()) {
                    throw new IllegalArgumentException();
//                    return 0;
                }
                int operatorNumber2 =  stack.pop();
//                System.out.println("pop 2 "+operatorNumber2);
//                System.out.println("stack  "+stack);
//

                switch(s.charAt(0)){
                    case '+':
                        operatorNumber1 = operatorNumber1+operatorNumber2;
                        break;
                    case '-':
                        operatorNumber1 = operatorNumber2-operatorNumber1;
                        break;
                    case '*':
                        operatorNumber1 = operatorNumber1*operatorNumber2;
                        break;
                    case '/':
                        operatorNumber1 = operatorNumber2/operatorNumber1;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                stack.push(operatorNumber1);
//                System.out.println("push "+operatorNumber1);
//                System.out.println("stack  "+stack);


            }else{
                stack.push(Integer.parseInt(s));
//                System.out.println("push "+s);
//                System.out.println("stack  "+stack);


            }
        }


        return stack.pop();

    }


    boolean isSign(char c){

        return c =='+'||c =='-'||c =='*'||c =='/';
    }


    public int evalRPN2(String[] tokens) {
        if(tokens ==null||tokens.length<=0 ) return 0;
        LinkedList<String> queue  = new LinkedList<>();
        for(int i = 0 ; i<tokens.length;i++){
            String s =tokens[i];
            char c = s.charAt(0);
            if(s.length()==1 &&isSign(c)){

                if(queue.isEmpty()){
                    throw  new IllegalArgumentException();
                }
                String op1 =queue.poll();

                if(queue.isEmpty()){
                    throw  new IllegalArgumentException();
                }
                String op = queue.poll();


                int  opInt =Integer.parseInt(op);
                int  opInt1 =Integer.parseInt(op1);

                int newOp = 0;

                switch(c){
                    case '+':
                        newOp = opInt+opInt1;
                        break;
                    case '-':
                        newOp = opInt-opInt1;

                        break;
                    case '*':
                        newOp = opInt1*opInt;

                        break;

                    case '/':
                        newOp = opInt/opInt1;

                        break;
                    default:
                        throw  new IllegalArgumentException();

                }

                queue.push(newOp+"");

            }else{
                queue.push(s);
            }
        }

        if(queue.isEmpty()){
            throw  new IllegalArgumentException();
        }
        return Integer.parseInt(queue.poll());

    }


}

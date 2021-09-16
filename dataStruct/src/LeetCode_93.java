package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_93 {


    public static void main(String[] args) throws InterruptedException {
        LeetCode_93 code = new LeetCode_93();
        Integer[] ints = new Integer[]{236,104,701,null,227,null,911};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

//        List<String> result = code.restoreIpAddresses("25525511135");
//        List<String> result2 = code.restoreIpAddresses2("25525511135");
//        List<String> result2 = code.restoreIpAddresses2("010010");
//        List<String> result2 = code.restoreIpAddresses2("172162541");
//        List<String> result2 = code.restoreIpAddresses2("101023");
        List<String> result2 = code.restoreIpAddresses2("000256");

//        System.out.println(result);
        System.out.println(result2);
//        TreeNodeUtil.printOrderTree(result);
    }

//    public static void main(String[] args) {
//
//    }


    // 题意 把s  分割成死份 ，每份 都不能大于 255
    // 击败 6
    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        restoreIpAddresses(s,"",0,result);
        return result;
    }


    public void  restoreIpAddresses(String s ,String tmpStr,int count ,List<String> list){
        if(s.length()>12) return ;

        if(s.isEmpty()||s.length()<(4-count)){
            return;
        }

        if(count==3){
            if(s.length()>1&&s.charAt(0)=='0') return ;
            long value = Long.parseLong(s);
            if(value<=255){
                list.add(tmpStr+"."+value);
            }
            return;
        }
        for(int i = 1 ; i<3||i<s.length();i++){

            if(s.length()<i)  break;

            String subStr = s.substring(0,i);
            if(subStr.isEmpty())  break;

            int value = Integer.parseInt(subStr);
            if(value<=255){
                if(s.length()>i) {
                    restoreIpAddresses(s.substring(i), tmpStr +(tmpStr.isEmpty()?"":".") + value, count+1, list);
                }
            }else{

                break;
            }

            if(i==1&&value == 0 ){
                break;
            }
        }
    }


//    boolean isLegal(String s){
//
//        if(s.equalsIgnoreCase())
//
//    }


    public List<String> restoreIpAddresses2(String s) {
        List<String> result = new ArrayList<>();
        solution(result,new StringBuilder(),s,0,0);

        return result;
    }


    void  solution(List<String> result ,StringBuilder tmpStr, String s ,int index ,int count){
        if(count >= 4){
            if(index ==s.length()){
                result.add(tmpStr.toString());
            }
            return;
        }
        if(index>=s.length()) return ;

        char c = s.charAt(index);

        if(c == '0' ) {
            tmpStr.append(c);
            if(count <3) {
                tmpStr.append('.');
            }
            solution(result,tmpStr,s,index+1,count+1);
            if(count <3) {
                tmpStr.deleteCharAt(tmpStr.length() - 1);
            }
            tmpStr.deleteCharAt(tmpStr.length()-1);

        }else if(((index+2) <s.length())  &&(((c -'0')==1) ||
                (((c -'0')==2) &&
                        (0<=(s.charAt(index+1) -'0')&&
                                (((s.charAt(index+1) -'0')==5 )&& ((s.charAt(index+2) -'0')<6 ))
                                ||(s.charAt(index+1) -'0')<5 ))
        )){
            tmpStr.append(c);
            if(count <3) {
                tmpStr.append('.');
            }
            solution(result,tmpStr,s,index+1,count+1);
            if(count <3) {
                tmpStr.deleteCharAt(tmpStr.length() - 1);
            }
            tmpStr.deleteCharAt(tmpStr.length()-1);

            tmpStr.append(c);
            tmpStr.append(s.charAt(index+1));
            if(count <3) {
                tmpStr.append('.');
            }
            solution(result,tmpStr,s,index+2,count+1);
            if(count <3) {
                tmpStr.deleteCharAt(tmpStr.length() - 1);
            }
            tmpStr.deleteCharAt(tmpStr.length()-1);
            tmpStr.deleteCharAt(tmpStr.length()-1);


            tmpStr.append(c);
            tmpStr.append(s.charAt(index+1));
            tmpStr.append(s.charAt(index+2));
            if(count <3) {
                tmpStr.append('.');
            }

            solution(result,tmpStr,s,index+3,count+1);
            if(count <3) {
                tmpStr.deleteCharAt(tmpStr.length() - 1);
            }
            tmpStr.deleteCharAt(tmpStr.length()-1);
            tmpStr.deleteCharAt(tmpStr.length()-1);
            tmpStr.deleteCharAt(tmpStr.length()-1);


        }else {
            tmpStr.append(c);
            if(count <3) {
                tmpStr.append('.');
            }

            solution(result,tmpStr,s,index+1,count+1);
            if(count <3) {
                tmpStr.deleteCharAt(tmpStr.length() - 1);
            }
            tmpStr.deleteCharAt(tmpStr.length()-1);


            if(index+1>=s.length()) return;
            tmpStr.append(c);
            tmpStr.append(s.charAt(index+1));
            if(count <3) {
                tmpStr.append('.');
            }

            solution(result,tmpStr,s,index+2,count+1);
            if(count <3) {
                tmpStr.deleteCharAt(tmpStr.length() - 1);
            }
            tmpStr.deleteCharAt(tmpStr.length()-1);
            tmpStr.deleteCharAt(tmpStr.length()-1);


        }

    }
}

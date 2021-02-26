package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_93 {

    public static void main(String[] args) {
        LeetCode_93 code = new LeetCode_93();
        Integer[] ints = new Integer[]{236,104,701,null,227,null,911};

        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

//        List<String> result = code.restoreIpAddresses("25525511135");
        List<String> result = code.restoreIpAddresses("010010");

        System.out.println(result);
//        TreeNodeUtil.printOrderTree(result);
    }


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
}

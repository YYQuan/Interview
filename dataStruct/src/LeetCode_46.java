package src;

import src.TreeNodeUtil.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_46 {

    public static void main(String[] args) {
        LeetCode_46 code = new LeetCode_46();
//        int[] ints = new int[]{236,104,701,227,911};
        int[] ints = new int[]{1,2,3};

//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

//        List<String> result = code.restoreIpAddresses("25525511135");
//        List<String> result = code.restoreIpAddresses("010010");
//        boolean result = code.isIllegat("01010");
        List<List<Integer>>  result = code.permute2_2(ints);

        System.out.println(result);
//        TreeNodeUtil.printOrderTree(result);
    }

    // 击败百分之10
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        permute(nums,new boolean[nums.length],new ArrayList<>(),result);
        return result;
    }

    public  void  permute(int[] nums ,boolean[] isReads,List<Integer> list ,List<List<Integer>> lists){

        boolean isReadAll = true;
        for(int i = 0 ; i<isReads.length;i++){
            if(!isReads[i]){
                isReadAll = false;
                break;
            }
        }

        if(isReadAll){
            lists.add(list);
            return;
        }

        for(int i = 0 ; i<nums.length;i++){
            List<Integer> copyList = new ArrayList<>(list);
            boolean[] isReadsTmp = isReads.clone();
            if(!isReadsTmp[i]){
                isReadsTmp[i] = true;
                copyList.add(nums[i]);
                permute(nums,isReadsTmp,copyList,lists);
            }
        }

    }



    public List<List<Integer>> permute2_2(int[] nums) {
        LinkedList<List<Integer>> result = new LinkedList<>();
        LinkedList<Integer> ints = new LinkedList<Integer>();
        for(int i:nums) {
            ints.add(i);
        }
        permute2_2(ints,result,new LinkedList<>());
        return result;
    }
    public void permute2_2(LinkedList<Integer> nums,
                           List<List<Integer>> result,
                                          LinkedList<Integer> ints)

    {

        if(0==nums.size()){
            result.add(new ArrayList<>(ints));
            return ;
        }


        for(int i = 0 ;i<nums.size();i++){
            int tmp = nums.get(i);
            ints.add(tmp);
            nums.remove((int)i);
            permute2_2(nums,result,ints);
            // 注意 保持数据的顺序
            nums.add(i,tmp);
            ints.remove((Object)tmp);

        }

    }

}

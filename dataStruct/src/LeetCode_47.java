package src;

import java.util.ArrayList;
import java.util.List;

public class LeetCode_47 {

    public static void main(String[] args) {
        LeetCode_47 code = new LeetCode_47();
//        int[] ints = new int[]{236,104,701,227,911};
        int[] ints = new int[]{1,1,2};

//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

//        List<String> result = code.restoreIpAddresses("25525511135");
//        List<String> result = code.restoreIpAddresses("010010");
//        boolean result = code.isIllegat("01010");
        List<List<Integer>>  result = code.permuteUnique(ints);

        System.out.println(result);
//        TreeNodeUtil.printOrderTree(result);
    }


    // 击败 5
    public List<List<Integer>> permuteUnique(int[] nums) {
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
            if(!lists.contains(list)) {
                lists.add(list);
            }
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


}

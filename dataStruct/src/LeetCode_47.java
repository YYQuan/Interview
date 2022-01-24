package src;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LeetCode_47 {

    public static void main(String[] args) {
        LeetCode_47 code = new LeetCode_47();
//        int[] ints = new int[]{236,104,701,227,911};
        int[] ints = new int[]{1,1};
//        int[] ints = new int[]{2,2,1,1};

//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

//        List<String> result = code.restoreIpAddresses("25525511135");
//        List<String> result = code.restoreIpAddresses("010010");
//        boolean result = code.isIllegat("01010");
        List<List<Integer>>  result = code.permuteUnique2(ints);

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


    public List<List<Integer>> permuteUnique2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        List<Integer> intsList = Arrays.stream(nums).boxed().collect(Collectors.toList());
        solution2(intsList,result,new LinkedList<>());
        return result;
    }

    public void  solution2(List<Integer> nums , List<List<Integer>> result , LinkedList<Integer> ints){

        if(nums.size()==0){

            //Util.printDeep(ints.size(),Arrays.toString(ints.toArray()));
            result.add(new ArrayList<>(ints));
            return;
        }

        for(int i = 0 ; i<nums.size();i++){
            //Util.printDeep(ints.size(),Arrays.toString(ints.toArray()));

            if(i>0 &&nums.get(i)==nums.get(i-1)) continue;
            int tmp = nums.get(i);
            ints.addLast((Integer)tmp);
            nums.remove((int)i);
            solution2(nums,result,ints);
            nums.add(i,tmp);
            // 要注意 由于有相同元素， 所以的指定删除的元素， 否则会有顺序错乱的问题
            ints.removeLast();
            //Util.printDeep(ints.size(),Arrays.toString(ints.toArray()));

        }
    }
}

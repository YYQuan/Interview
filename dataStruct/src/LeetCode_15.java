package src;

import java.util.*;

public class LeetCode_15 {

    public static void main(String[] args) {
        LeetCode_15 code = new LeetCode_15();
//        int[] ints  = new int[]{2,7,11,15};
//        int[] ints  = new int[]{-1, 0, 1, 2, -1, -4};
//        int[] ints  = new int[]{0, 0, 0};
//        int[] ints  = new int[]{-1,0, 1, 0};
//        int[] ints  = new int[]{-2,0,0,0,0,0,2,2,2};
        int[] ints  = new int[]{1,1,-2};
//        int[] ints  = new int[]{-1,0,1,2,-1,-4};
//        int[] ints  = new int[]{-1,0,1,2,-1,-4};
//        int[] result = code.solution(ints,9);
        String s = "pwwkew";

//        code.quickSort(ints);


//        List<List<Integer>> result = code.threeSum(ints);
        List<List<Integer>> result2 = code.threeSum2(ints);

//        for(List<Integer> list:result){
//            for(int value:list) {
//                System.out.print(value + " ");
//            }
//        }
//        System.out.println(result);
        System.out.println(result2);
    }

    /**
     * 整体的时间复杂度是0（n^2）
     * 要点：  先排序 0(nlogn) ,
     * 怎么样去除相同元素
     *
     * 添加的时候把已经添加进去的给过滤掉
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {


        List<List<Integer>>  result = new ArrayList<>();
        if(nums.length<3) return result;

        // 先排个序  O(nlogn)
        quickSort(nums);

        for(int i  =0 ;i<nums.length;i++){
            //过滤掉重复的
            if(i>0&&nums[i]==nums[i-1]){
                continue;
            }
            //只需要把负值的 全部答案 即可
            if(nums[i]>0){
                break;
            }else if(nums[i]==0){
                // 三个0
                if((i+2)<nums.length&&nums[i+1]==0&&nums[i+2]==0){
                    List<Integer> list = new ArrayList<>();
                    list.add(0);
                    list.add(0);
                    list.add(0);
                    result.add(list);
                }
            }else{
                // 把包含 nums[i]的全部答案都拿到

                result.addAll(containValue(nums,i));
            }
        }

        return result;
    }

    // 双指针  时间复杂度是 0（n）
    public List<List<Integer>> containValue(int[] nums, int valueIndex){
        List<List<Integer>> result =  new ArrayList<>();

        int target = -nums[valueIndex];

        int i = valueIndex+1;
        int j = nums.length-1;
        while(i<j){
//            //过滤掉重复的
//            if(i>0&&nums[i]==nums[i-1]){
//                i++;
//                continue;
//            }



            int sum = nums[i]+nums[j];
            if(sum>target){
                j--;
            }else if(sum<target){
                i++;
            }else{
                List<Integer> list = new ArrayList<>();
                list.add(nums[i]);
                list.add(nums[j]);
                list.add(-target);
                result.add(list);

                // 避免重复 把这个i对应的nums[j]都过滤掉
                do{
                    i++;
                }while(i<j&&i<nums.length&&nums[i] == nums[i-1]);

                do{
                    j--;
                }while(i<j&&j<nums.length-2&&nums[j] == nums[j + 1]);

            }
        }


        return result;
    }


    public void quickSort(int[] nums){



        quickSort(nums,0,nums.length-1);

    }

    public void quickSort(int[] nums, int l , int r){

        if(r-l<1) return ;


        int index  = find(nums,l,r);
        quickSort(nums,l,index-1);
        quickSort(nums,index+1,r);
    }

    public int find(int[] nums ,int start ,int end){

        int random = new Random().nextInt(end-start)+start;
//        int random = 0;
//        System.out.println("start ,"+start+"  end :"+ end+ "  random "+random);
        swap(nums,start,random);
        int base = nums[start];

        int i = start +1;//(0,l) 小于base
        int j = end;//(r,nums.length-1] 大于base

        while(true){
            while(i<=end&&nums[i]<base){
                i++;
            }

            while(j>start&&nums[j]>base){
                j--;
            }

            if(i>=j){
                break;
            }

            swap(nums,i++,j--);
        }
        swap(nums, start, j);
        return j;
    }

    public void swap(int[] nums ,int l ,int r){
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }


    /**
     * 核心思路
     * 排序  + 双指针
     *
     * 不能用遍历加 set 来代替双指针。
     *
     * 为啥不能代替。 set   处理不了 出现的次数。 对于（0,0,0） 得特殊处理
     * 双指针的话，同样有遍历的效果， 对于 全 0的情况是不需要特殊处理的
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum2(int[] nums) {
        List<List<Integer>>  result = new ArrayList<>();

        if(nums==null||nums.length<3) return result;

        Arrays.sort(nums);

        A:for(int i  =0 ; i<nums.length;i++){
            int tmpI = nums[i];
            if(tmpI>0)  break;

            if(i>0){
                if(nums[i] ==nums[i-1]){

                    continue A;
                }
            }


           for(int l = i+1 ,r= nums.length-1; l<r ;){

               if(l>i+1&&(nums[l] == nums[l-1])){
                   l++;
                   continue ;
               }

               if(r<nums.length-1&&(nums[r]==nums[r+1])){
                   r--;
                   continue ;
               }
               int sum = nums[l]+nums[r]+nums[i];
               if(sum == 0){
                   List<Integer> ints = new ArrayList<>();
                   ints.add(nums[i]);
                   ints.add(nums[l]);
                   ints.add(nums[r]);
                   result.add(ints);
                   l++;
               }else if(sum <0){
                   l++;
               }else{
                   r--;
               }


           }

        }
        return result;
    }
}

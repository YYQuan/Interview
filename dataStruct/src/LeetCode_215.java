package src;

import java.util.Random;

public class LeetCode_215 {

    public static void main(String[] args) {
        LeetCode_215 code = new LeetCode_215();
//        int[] ints  = new int[]{2,0,2,1,1,0};
//        int[] nums1  = new int[]{1,2,3,0,0,0};
//        int[] nums2   = new int[]{2,5,6};

        int[] nums1  = new int[]{3,2,1,5,6,4};
        int[] nums2   = new int[]{1};

        int result = code.findKthLargest3(nums1,2);
//        System.out.println(result+"  ");
        System.out.println(result+"  ");
        for(int i =0 ;i<nums1.length;i++){
            System.out.print(nums1[i]+"  ");

        }

    }




    // 1.0 解法
    public int findKthLargest(int[] nums, int k) {
        quickSort(nums);
        return nums[nums.length-k];
    }

    //2.0解法  K次冒泡
    public int findKthLargest2(int[] nums ,int k ){

        for(int i = 0 ;i<k;i++){
            for(int j =0 ;j<nums.length-i-1;j++){
                if(nums[j]>nums[j+1]){
                    swap(nums,j,j+1);
                }
            }
        }

        return nums[nums.length -k];
    }

    //3.0 解法  利用快排的 m位的序号 是否为k来做剪枝
    public int findKthLargest3(int[] nums ,int k){
        return findKthLargest3(nums,0,nums.length-1,k);
    }
    public int findKthLargest3(int[] nums ,int l,int r ,int k){

        if(l>=r) return l;

        int m = handle(nums,l,r);

        if(m == (nums.length-k) ){
            return nums[m];
        }
        else if(m>(nums.length-k)) {
           return findKthLargest3(nums,l,m-1,k);
        }
        else if(m<(nums.length-k)) {
            return findKthLargest3(nums,m+1,r,k);
        }

        return -1;
    }


    public void swap(int[] nums,int i,int j){
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    public void quickSort(int[] nums ){

        quickSort(nums,0,nums.length-1);
    }

    public void  quickSort(int[] nums ,int l , int r ){

        if(l >=r ) return ;

        int m = handle(nums,l,r);
        quickSort(nums,l,m-1);
        quickSort(nums,m+1,r);

    }

    public int handle(int[] nums,int l , int r){

        if(l>=r)  return 0;

        //对于这题 的用例来说  这个 随机值 很重要。。
        int   randomIndex = new Random().nextInt(r)%(r-l);

        swap(nums,l,l+randomIndex);


        int base = nums[l];

        int i = l ; // (l,i] 小于base
        int j =l+1;

        while(j<=r){

            if(nums[j]<base){
                i++;
                swap(nums,i,j);
            }else{

            }
            j++;
        }

        swap(nums,i,l);
        return i;
    }





}

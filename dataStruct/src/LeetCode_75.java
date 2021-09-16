package src;

import java.util.Random;

public class LeetCode_75 {

    public static void main(String[] args) {
        LeetCode_75 code = new LeetCode_75();
        int[] ints  = new int[]{2,0,2,1,1,0};
//        int[] ints  = new int[]{1,2,0};

  //      code.sortColors(ints);
        code.sortColors2(ints);
//        System.out.println(result+"  ");
        for(int i =0 ;i<ints.length;i++){
            System.out.print(ints[i]+"  ");

        }

    }


    // 注意边界条件   边界条件 一定刚看清楚 ，严格按照边界条件来执行即可
    public void sortColors(int[] nums) {
        if(nums.length<=1)  return ;
        int left ;  // [0,left)  0
        int m ; //  [left,m)  1
        int r ; //   [r,nums.length)  2
        left  = 0 ;
        m  = 0 ;
        r = nums.length;

        for(int i = 0 ; m<r ;){
            if(nums[i] == 0){
                swap(nums,left,i);
                left++;
                m++;
                i++;
            }else if(nums[i] ==1){
                swap(nums,m++,i++);
            }else if( nums[i] == 2){
                swap(nums,i,--r);
            }
        }


    }

    void swap (int[] nums,int l , int r){
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }


    public void sortColors2(int[] nums) {
        sortColors2(nums,0,nums.length);
    }
    public void sortColors2(int[] nums,int l  ,int r ) {
        if(l >= r){
            return ;
        }

        int random = new Random().nextInt(r-l)+l;
        swap(nums,l,random);
        int tmp = nums[l];
        int p =l; //[l+1,p)<nums[]
        int q =r-1; //[q,r)>=nums[l]
        System.out.println(String.format(" l %d, i %d, r %d  random %d",l,q,r,random));
        while(p<q){

            while(p<r&&nums[p]<=tmp){
                p++;
            }

            while(q>l&&nums[q]>=tmp){
                q--;
            }


            if(p<q) {
                swap(nums, p, q);
            }

        }

        swap(nums,l,q);


        sortColors2(nums,l,q);
        sortColors2(nums,q+1,r);
    }
}

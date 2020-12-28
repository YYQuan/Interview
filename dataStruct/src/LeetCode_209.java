package src;

public class LeetCode_209 {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
        LeetCode_209 code = new LeetCode_209();
        int[] ints  =new  int[0];

//        code.solution(7,ints);

        System.out.println("result  "+ code.solution(15,ints));

    }


    public int solution(int s ,int[] nums ){

        if(nums.length<=0) return 0 ;

        int l = 0 ,r= 0;// 满足>s 的 左右 index
        int minCurrentBound = Integer.MAX_VALUE ;
        int tmp = nums[0] ;// [l,r] 的和

        while(r<nums.length){
            //r 找到第一个 满足  和大于 s的index 直到超出范围
            while(r<nums.length&&tmp<s){
                r++;
                if(r<nums.length) {
                    tmp += nums[r];
                }
            }

            //l 找到第一个不满足和 大于 s的index
            while(tmp>=s){
                tmp -= nums[l];
                l++;

            }


            // 如果r <nums.length 就说明在范围内 是有 满足条件的。
            if(r<nums.length&&
                    minCurrentBound >(r-l+2)){

                // 主要是要范围  不是 下标 所以要多 +1
                minCurrentBound = r-l+2;

            }

        }

        return  minCurrentBound>= Integer.MAX_VALUE?0: minCurrentBound ;
    }


    // 思路一样 ，但是简化写边界条件
    public int solution2(int s ,int[] nums ){

        if(nums.length<=0) return 0 ;

        int l = 0 ,r= 0;// 满足>s 的 左右 index
        int minCurrentBound = 0;
        int tmp = nums[0] ;// [l,r] 的和

        while(r<nums.length){
            //r 找到第一个 满足  和大于 s的index 直到超出范围
            while(r<nums.length&&tmp<s){
                r++;
                if(r<nums.length) {
                    tmp += nums[r];
                }
            }

            //l 找到第一个不满足和 大于 s的index
            while(tmp>=s){

                // 这里来赋值  都是有效 长度,
                if(minCurrentBound==0||minCurrentBound>(r-l+1)){
                    minCurrentBound = r-l+1;
                }
                tmp -= nums[l];
                l++;

            }

        }

        return  minCurrentBound ;
    }


}

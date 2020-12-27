package src;

public class LeetCode_88 {

    public static void main(String[] args) {
        LeetCode_88 code = new LeetCode_88();
//        int[] ints  = new int[]{2,0,2,1,1,0};
//        int[] nums1  = new int[]{1,2,3,0,0,0};
//        int[] nums2   = new int[]{2,5,6};

        int[] nums1  = new int[]{0};
        int[] nums2   = new int[]{1};

        code.merge2(nums1,0,nums2,1);
//        System.out.println(result+"  ");
        for(int i =0 ;i<nums1.length;i++){
            System.out.print(nums1[i]+"  ");

        }

    }




    // 1.0 解法
    public void merge1(int[] nums1, int m, int[] nums2, int n) {

        if(n<1) return ;

        int i = 0 ;// 当前从 nums1的第几个元素开始
        int j = 0 ;// 当前遍历到 nums2的第几个元素
        while(i<(m+j)&&j<n){
            if(nums1[i]<=nums2[j]){
                i++;
            }else{
                for(int index = m+j ; index>i;index--){
                    nums1[index] = nums1[index-1];
                }
                nums1[i++] = nums2[j++];
            }
        }

        for(;j<n;i++,j++){
            nums1[i] = nums2[j];
        }

    }

    // 2.0 解法
    public void merge2(int[] nums1, int m, int[] nums2, int n) {

        int r1 = m-1 ;
        int r2 = n-1 ;
        int middle = m+n-1;

        while(middle>=0){

            if(r1<0&&r2>=0){
                nums1[middle--] = nums2[r2--];
            }else if(r2<0&&r1>=0){
                nums1[middle--] = nums1[r1--];
            }else   if (nums1[r1] >= nums2[r2]) {
                nums1[middle--] = nums1[r1];
                r1--;
            }else {
                nums1[middle--] = nums2[r2];

                r2--;

            }


        }
    }

//    解法3
//    思路和2  是一样的 但是 利用了一个特点
//    如果nums2已经被比较完了的话，nums1是不需要做操作就是有序的了
    public void merge3(int[] nums1, int m, int[] nums2, int n) {
        int p =  m + n -1;
        m--;
        n--;

        //如果m < 0了 就说明
        while( m>=0 && n>=0){
            nums1[p--] =  nums1[m]>nums2[n]? nums1[m--]:nums2[n--];
        }

        while(n>=0){
            nums1[p--] = nums2[n--];
        }


    }


}

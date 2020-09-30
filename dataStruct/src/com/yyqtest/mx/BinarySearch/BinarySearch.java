package com.yyqtest.mx.BinarySearch;

public class BinarySearch {


    // 递归写法

    public static int  binarySearchRes(Comparable[] arr,Comparable c){
        if(arr == null || c == null || arr.length<=0)  return -1;
        return binarySearchResActual(arr,c,0,arr.length-1);
    }


    private  static int  binarySearchResActual(Comparable[] arr , Comparable c, int l , int r){
        if(l>r)  return -1;
        int result = -1;
        int mid = l + (r-l)/2;

        int  compareResult = c.compareTo(arr[mid]);
        if(compareResult == 0)
            result =  mid;
        else if(compareResult >0)
            result = binarySearchResActual(arr,c,mid+1,r);
        else
            result = binarySearchResActual(arr,c,l,mid -1);


        return result;
    }



    // 非递归写法
    public static int binarySearch(Comparable[] arr,Comparable c){

        if(arr == null || c == null || arr.length<=0 ) return -1;

        int result = -1;
        int l  = 0;
        int r = arr.length-1;
        int mid = (l+r)/2;

        while(l<=r){
            if(arr[mid].compareTo(c)<0){
                l = mid+1;

            }else if(arr[mid].compareTo(c)>0){
                r = mid-1;
            }else{
                result = mid;
                break;
            }
            mid = (l+r)/2;

        }


        return result;
    }




    public static int binarySearchTest(Comparable[] arr,Comparable c){
        if(arr == null || c ==null ||  arr.length<=0)  return -1;
        int result = -1;


        int l = 0;
        int r = arr.length -1;

        while(l<=r){

//            int mid = (l+r) /2;
            // 避免溢出的写法
            int  mid = l + (r-l)/2;

            int comperaResult = c.compareTo(arr[mid]);
            if(comperaResult>0){
                l = mid +1;
            }else if(comperaResult<0){
                r = mid -1;
            }else{
                result = mid;
                break;
            }

        }

        return  result;
    }

    // 测试非递归的二分查找算法
    public static void main(String[] args) {

//        int N = 1000000;
        int N = 20;
        Integer[] arr = new Integer[N];
        for(int i = 0 ; i < N ; i ++)
            arr[i] = new Integer(i+3);

        // 对于我们的待查找数组[0...N)
        // 对[0...N)区间的数值使用二分查找，最终结果应该就是数字本身
        // 对[N...2*N)区间的数值使用二分查找，因为这些数字不在arr中，结果为-1
//        for(int i = 0 ; i < 2*N ; i ++) {
//            int v = BinarySearch.binarySearch(arr, new Integer(i));
//            if (i < N)
//                assert v == i;
//            else
//                assert v == -1;
//        }

        int v = BinarySearch.binarySearch(arr, new Integer(11));
        int v2 = BinarySearch.binarySearchTest(arr, new Integer(11));
        int v3 = BinarySearch.binarySearchRes(arr, new Integer(11));
        System.out.println(v);
        System.out.println(v2);
        System.out.println(v3);
        return;
    }
}
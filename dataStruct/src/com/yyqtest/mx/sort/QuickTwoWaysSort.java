package com.yyqtest.mx.sort;

/**
 *
 */
public class QuickTwoWaysSort {

    // 双路快排就是把 等于的值  平分在两边。 减少左右不平衡的情况

    public static  void quickTwoWaysSort(Comparable[] arr){

        if(arr == null || arr.length <= 0 ) return ;

        quickTwoWaysSort(arr,0,arr.length-1);

    }


    //处理范围：[start ... end ]
    public static  void quickTwoWaysSort(Comparable[] arr,int start ,int end){

        if(start > end)  return;;

//        Comparable c = arr[0];
        int index =  handle(arr, start ,end);
        quickTwoWaysSort(arr,start,index-1);
        quickTwoWaysSort(arr,index+1,end);

    }


    //处理范围：[start ... end ]
    private static int handle(Comparable[] arr,int start ,int end) {


        Comparable c = arr[start];


        // 从左边找到  第一个 大于 c的 ， 从右边找到第一个小于c 的进行交换

        int i = start +1 ;
        int j = end ;
        while(true){

            while(i<=end && arr[i].compareTo(c)<0)
                i++;

            while(j>start&&arr[j].compareTo(c)>0)
                j--;

            if(i>=j){
                break;
            }

            swap(arr,i,j);
        }

        swap(arr,start,j);

        return j;

    }


    private static void swap(Comparable[] arr, int l , int r){
        Comparable tmp = arr[l];
        arr[l] = arr[r];
        arr[r]= tmp ;
    }

    public static void main(String[] args) {

        Integer[] arr = {10,9,8,7,6,1,11,5,4,3,2,1};
//        BubbleSort.sort(arr);
        QuickTwoWaysSort.quickTwoWaysSort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
    }




}

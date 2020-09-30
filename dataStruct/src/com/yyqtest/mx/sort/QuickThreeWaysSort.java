package com.yyqtest.mx.sort;

/**
 *
 */
public class QuickThreeWaysSort {

    // 双路快排就是把 等于的值  平分在两边。 减少左右不平衡的情况

    public static  void quickThreeWaysSort(Comparable[] arr){

        if(arr == null || arr.length <= 0 ) return ;

        quickThreeWaysSort(arr,0,arr.length-1);

    }


    //处理范围：[start ... end ]
    public static  void quickThreeWaysSort(Comparable[] arr,int start ,int end){

        if(start > end)  return;;

        Comparable c = arr[start];




        // 注意 初始值 也需要满足 范围的定义
        int lt = start; // (start ...lt ]  < c
        int rt = end +1;  //  [rt ...end] >c
        int i = start +1;// (lt... i) == c

        for(;i<rt ; ){

            if(arr[i].compareTo(c)<0){
                // < c
                swap(arr,i,lt+1);
                lt++;
                i++;
            }else if(arr[i].compareTo(c)>0){
                swap(arr,i,rt-1);
                rt--;
            }else{
                i++;
            }
        }


        swap(arr,start,lt);

        quickThreeWaysSort(arr,start,lt-1);
        quickThreeWaysSort(arr,rt,end);

    }



    private static void swap(Comparable[] arr, int l , int r){
        Comparable tmp = arr[l];
        arr[l] = arr[r];
        arr[r]= tmp ;
    }

    public static void main(String[] args) {

        Integer[] arr = {10,9,8,7,6,1,11,5,4,3,2,1};
//        BubbleSort.sort(arr);
        QuickThreeWaysSort.quickThreeWaysSort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
    }




}

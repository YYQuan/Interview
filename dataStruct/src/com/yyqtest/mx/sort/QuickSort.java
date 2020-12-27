package com.yyqtest.mx.sort;

import java.util.Arrays;

/**
 *
 */
public class QuickSort {


    public static  void quickSort(Comparable[] arr){

        if(arr == null || arr.length <= 0 ) return ;

        quickSort(arr,0,arr.length-1);

    }


    //处理范围：[start ... end ]
    public static  void quickSort(Comparable[] arr,int start ,int end){

        if(start > end)  return;;

//        Comparable c = arr[0];
        int index =  handle(arr, start ,end);
        quickSort(arr,start,index-1);
        quickSort(arr,index+1,end);

    }


    //处理范围：[start ... end ]
    private static int handle(Comparable[] arr,int start ,int end) {

        Comparable c = arr[start];

        int j = start;
        for(int i  =start ;i<= end;i++){
            if(arr[i].compareTo(c)<0){
                j++;
                swap(arr, i ,j);
            }
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

        int[] arr = {10,9,8,7,6,5,4,3,2,1};
//        BubbleSort.sort(arr);
        new QuickSort().quickSort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
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

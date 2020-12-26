package com.yyqtest.mx.sort;

import java.util.Random;

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
//        int index =  handle(arr, start ,end);
        int index =  handle2(arr, start ,end);
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


    public static int handle2(Comparable[] nums ,int l,int r){
        if(l>=r)     return l;
        int randomIndex =  new Random().nextInt(r-l)+l;
//        int randomIndex = Math.random((int)(r-l))+l;
        swap(nums,randomIndex,l);
        Comparable base = nums[l];
        // 找到 第一个大于base的值的 范围是(l,r]
        // 找到 第一个小于base的值的 范围是(l,r]
        // i，j 的左右位置关系只能决定他们需不需要交换
        int i =l+1;// 从左 找 第一个大于 base的值
        int j = r; // 从右找 第一个小于 base的值
        while(i<j){


            while(i<=r&&nums[i].compareTo(base)<=0){
                i++;
            }
            while(j>l+1&&nums[j].compareTo(base)>=0){
                j--;
            }

            if(i<j &&nums[i].compareTo(nums[j])>0){
                swap(nums,i,j);
            }

        }

        swap(nums,l,j);

        return j;
    }

    private static void swap(int[] arr, int l , int r){
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r]= tmp ;
    }


}

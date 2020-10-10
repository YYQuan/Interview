package com.yyqtest.mx.sort;

import java.util.Arrays;

/**
 *
 */
public class MergeSort {
    public static   void mergeSort(Comparable[] arr){
        if(arr == null || arr.length <= 0 )
            return ;

        int end = arr.length-1;

        mergeSort(arr,0,end);

    }

    //[start...end]范围的归并
    public static void mergeSort(Comparable[] arr,int start ,int end ){
        if(start>=end) return ;
        int mid = (start+end)/2;
        mergeSort(arr,start,mid);
        mergeSort(arr,mid+1,end);
        merge(arr,start,mid,end);
    }

    private static void merge(Comparable[] arr,int start , int mid ,int end ){
        Comparable[] aux = Arrays.copyOfRange(arr, start, end+1);

        int p = start ;
        int q = mid+1;
        System.out.println("<<<< start " + start + "  mid:"+mid +"   end :"+end);

        for(int i = start ; i <=end;i++){
            System.out.println("<<<< p " + p + "  q:"+q+"   i :"+i);
            if(p>mid){
                arr[i] = aux[q-start];
                q++;
            }else if(q>end){
                arr[i] = aux[p-start];
                p++;
            }else if( aux[p-start].compareTo(aux[q-start])< 0  ){
                arr[i] = aux[p-start];
                p++;
            }else{
                arr[i] = aux[q-start];
                q++;
            }
        }
    }

    public static void main(String[] args) {

        Integer[] arr = {10,9,8,7,6,5,4,3,2,1};
//        BubbleSort.sort(arr);
        MergeSort.mergeSort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
    }




}

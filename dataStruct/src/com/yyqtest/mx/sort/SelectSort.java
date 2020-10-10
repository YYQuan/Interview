package com.yyqtest.mx.sort;

/**
 *
 */
public class SelectSort {

    // 选择排序就是 每次拿到剩余范围中的最小的元素 放到最左侧

    public  static int[]  selectSort(int[ ] arr){
        if(arr == null ||arr.length<=0){
            throw new IllegalArgumentException();
        }


        for(int  i =0 ; i<arr.length ; i++){
            int min = i;
            // [i ... arr.lenth] 中的最小元素的序列
            for(int j = i  ; j<arr.length ;j++){
                if(arr[i] > arr[j]){
                    min = j;
                }

            }
            swapMySlef(arr,i,min);

        }


        return null;
    }

    private static void swapMySlef(int[] arr,int index1 ,int index2){
        int tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
        return;
    }

    public static void sort(int[] arr){

        int n = arr.length;
        for( int i = 0 ; i < n ; i ++ ){
            // 寻找[i, n)区间里的最小值的索引
            int minIndex = i;
            for( int j = i + 1 ; j < n ; j ++ )
                if( arr[j] < arr[minIndex] )
                    minIndex = j;

            swap( arr , i , minIndex);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {

        int[] arr = {10,9,8,7,6,5,4,3,2,1};
//        BubbleSort.sort(arr);
        SelectSort.selectSort(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
    }




}

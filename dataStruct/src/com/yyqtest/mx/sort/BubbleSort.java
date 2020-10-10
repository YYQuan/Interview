package com.yyqtest.mx.sort;

/**
 *
 */
public class BubbleSort {




    public  static int[]  bubbleSort(int[ ] arr){
        if(arr == null ||arr.length<=0){
            throw new IllegalArgumentException();
        }


        for(int i = arr.length ; i>0 ;i--){
            for(int j = 0 ; j< i-1 ;j++){
                if(arr[j]>arr[j+1]){
                    swapMySlef(arr,j,j+1);
                }
            }
        }

        int[] arrCopy = arr.clone();

        for(int i  = 1 ; i< arr.length ; i ++){
            a:for(int j  = i ; j > 0  ;j--){
                if(arrCopy[j]<arr[j-1]){
                    swapMySlef(arrCopy,j,j-1);
                }else{
                    break a;
                }
            }
        }

        return null;
    }

    private static void swapMySlef(int[] arr,int index1 ,int index2){
        int tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
        return;
    }





    public static void main(String[] args) {

        int[] arr = {10,9,8,7,6,5,4,3,2,1};
//        BubbleSort.sort(arr);
        BubbleSort.bubbleSort(arr);
//        BubbleSort.test(arr);
        for( int i = 0 ; i < arr.length ; i ++ ){
            System.out.print(arr[i]);
            System.out.print(' ');
        }
        System.out.println();
    }




}

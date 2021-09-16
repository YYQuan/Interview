package src;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class LeetCode_447 {

    public static void main(String[] args) {
        LeetCode_447 code = new LeetCode_447();
//        int[][] ints  = new int[][]{{0,0},{1,0},{2,0}};
        int[][] ints  = new int[][]{{1,1},{2,2},{3,3}};
//        int[] result = code.solution(ints,9);
//        int[] result = code.solution(ints,9);
//        int[] result = code.twoSum(ints,9);
        int result = code.numberOfBoomerangs(ints);
        int result2 = code.numberOfBoomerangs2(ints);
        System.out.println(result);
        System.out.println(result2);

    }

    // 百分之 9
    public int numberOfBoomerangs(int[][] points) {
        int  result = 0 ;
        int pointSize = points.length;
        for(int i = 0 ; i<pointSize;i++){
            // key ： 距离 value ：个数
            HashMap<Double,Integer> map = new HashMap<>();

            for(int j = 0;j<pointSize;j++){
                double des = calculatorDes(points[i],points[j]);

                if(map.containsKey(des)){
                    map.put(des,map.get(des)+1);
                }else{
                    map.put(des,1);
                }
            }

            for(double key :map.keySet()){
                int count = map.get(key);
                if(key>0&&count >1){
                    //A N 2
                    result +=(count*(count-1));
                }
            }
        }

        return result;
    }

    double calculatorDes(int[] ints1,int[] ints2){

        double result = 0 ;

        int x = Math.abs(ints1[0]-ints2[0]);
        int y = Math.abs(ints1[1]-ints2[1]);

        result =  Math.sqrt(x*x+y*y);
//        System.out.println(Arrays.toString(ints1));
//        System.out.println(Arrays.toString(ints2));
//        System.out.println("result "+result +"");
//        System.out.println();
        return result;
    }

    public int numberOfBoomerangs2(int[][] points) {

         if( points == null ||points.length<3) {
             return 0 ;
         }

         int result = 0 ;

         for(int i =0 ; i <points.length;i++){
             HashMap<Double,Integer> map = new HashMap<>();
             for(int j = 0 ;j<points.length;j++){
                 if(i == j ) continue;
                 double dis = calculatorDes(points[i],points[j]);
                 map.put(dis,map.getOrDefault(dis,0)+1);

             }
             for(int count :map.values()) {
                 result +=(count*(count-1));
             }
         }
         return result;
    }
}

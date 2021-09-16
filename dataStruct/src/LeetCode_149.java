package src;

import java.util.HashMap;
import java.util.HashSet;

public class LeetCode_149 {

    public static void main(String[] args) {
        LeetCode_149 code = new LeetCode_149();
//        int[] ints  = new int[]{2,7,11,15};
        int[][] ints = new int[][]{{1,1},{3,2},{5,3},{4,1},{2,3},{1,4}};
//        int[][] ints = new int[][]{{1,1},{1,1},{2,2},{2,2}};
//        int[][] ints = new int[][]{{1,1},{1,1},{1,1}};
//        int[][] ints = new int[][]{{0,0},{94911151,94911150},{94911152,94911151}};


//        int[] result = code.solution(ints,9);
//        int[] result = code.solution(ints,9);
//        int[] result = code.twoSum(ints,9);
        int result = code.maxPoints(ints);
        int result2 = code.maxPoints2(ints);
        System.out.println(result);
        System.out.println(result2);

    }

    //击败百分之 70
    //重点 不要 忽略了 同一个点的位置
    //重点 calculator 计算 斜率的时候 分子分母都乘上一个放大值， 这样可以扩大 分辨能力
    //记录下每一个点和其他点的  同直线的数目， 保留最大数量做比较
    public int maxPoints(int[][] points) {

        if(points.length<3) return points.length;

        int result = 0;

        for(int i = 0 ; i<points.length;i++){
            //key : 斜率  value: 计数
            HashMap<Double,Integer> map = new HashMap<>();
            int samePoint = 1;

            for(int j =0 ;j<points.length;j++){

                if(i == j)  continue;
                if(points[i][0] ==points[j][0]&&points[i][1]==points[j][1]){
                    samePoint++;
                }else {
                    double rate = calculator(points[i], points[j]);
                    map.put(rate, map.getOrDefault(rate, 0) + 1);
                }
            }
            result = Math.max(result,samePoint) ;
            for(Double rate:map.keySet()){
                result = Math.max(result,map.get(rate)+samePoint);
            }
        }


        return result;
    }


    double calculator(int[] point1 ,int[] point2){

        if((point1[0]-point2[0])==0) return Double.MAX_VALUE;

        return  ((double)(point1[1]-point2[1])*1000/(double)(point1[0]-point2[0]))*1000;

    }

    boolean pointInLine(int[] point1,int[] point2 , int[] target){


        double slope1 = (point1[1]-target[1])*100000/  (point1[0]-target[0])*100000;
        double slope2 = (point2[1]-target[1])*100000/(point2[0]-target[0])*100000;

        return slope1 == slope2;
    }



    public int maxPoints2(int[][] points) {
        if(points==null) return 0 ;
        if(points.length<3) return points.length;

        int result = 2;

        for(int i = 0 ;i<points.length;i++){

            HashMap<Double,Integer> map  = new HashMap<>();
            int samplePoint = 0;
            // 不能过滤重复的  要包含每一对
            for(int j = 0;j<points.length;j++){

                if(points[i][0] ==points[j][0]&&points[i][1] ==points[j][1]){
                    samplePoint++;
                }else{
                    double rate = calculator(points[i],points[j]);
                    map.put(rate ,map.getOrDefault(rate,0)+1);
                }
            }


            for(int count:  map.values()){
                result =Math.max(count+samplePoint,result);
            }

        }
        return result;
    }

}

package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode_120 {

    public static void main(String[] args) {

        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>(){
            {

            }

        };
        list.add(Arrays.asList( new Integer[]{2}));
        list.add(Arrays.asList( new Integer[]{3,4}));
        list.add(Arrays.asList( new Integer[]{6,5,7}));
        list.add(Arrays.asList( new Integer[]{4,1,8,3}));
        System.out.println(new LeetCode_120().minimumTotal(list));
        System.out.println(new LeetCode_120().minimumTotal2(list));
        System.out.println(new LeetCode_120().minimumTotal3(list));
//        System.out.println(new LeetCode_120().minimumTotal2(list));

    }


    // 动态规划
    public int minimumTotal(List<List<Integer>> triangle) {
        int[] ints = new int[triangle.get(triangle.size()-1).size()];
        Arrays.fill(ints,Integer.MAX_VALUE);
        for(int i = 0 ; i<triangle.size();i++){
            int[] intTmp = ints.clone();
            for(int j = 0 ; j<triangle.get(i).size();j++){
                int tmp = calculator2(intTmp,i,j,triangle.get(i).get(j));
                ints[j] = tmp;
            }
        }

        int result = Integer.MAX_VALUE;
        for(int i = 0 ; i<ints.length;i++){
            result = Math.min(result ,ints[i]);
        }

        return result;
    }

    public int calculator2(int[] tmpInts,int row ,int colum,int value){

        if(row==0){
            return value;
        }
        int v1 = (tmpInts[colum]==Integer.MAX_VALUE?Integer.MAX_VALUE:tmpInts[colum]+value);

        return Math.min(v1,
                colum>0?
                        (tmpInts[colum-1]==Integer.MAX_VALUE?
                                Integer.MAX_VALUE:
                                tmpInts[colum-1]+value)
                        :Integer.MAX_VALUE);

    }





    //  常规递归 超时
    // f(triangle.get(n),i) = Math.min(f(n-1,i),f(n-1,i+1)
    public int minimumTotal2(List<List<Integer>> triangle) {
        int result = Integer.MAX_VALUE;
        for(int i = 0 ;i<triangle.get(triangle.size()-1).size();i++){

            int  value = calculator(triangle,triangle.size()-1,i);
            result = Math.min(value,result);
        }

        return result;
    }
    public int calculator(List<List<Integer>> triangle,int row,int colum) {
//        System.out.println("calculator  row ,  colum "+ row +" "+ colum);

        if(row ==0){
            return triangle.get(row).get(colum);
        }

       return Math.min(
               colum<row?calculator(triangle,row-1,colum)+triangle.get(row).get(colum):
                            Integer.MAX_VALUE,
               colum>0?
               calculator(triangle,row-1,colum-1)+triangle.get(row).get(colum):
                       Integer.MAX_VALUE
               );

    }


    public int minimumTotal3(List<List<Integer>> triangle) {
        if(triangle == null||triangle.size()==0) return 0;
        if(triangle.get(triangle.size()-1) == null) return 0;
        int[] tmpSum = new int[triangle.get(triangle.size()-1).size()];
        Arrays.fill(tmpSum,0);

        tmpSum[0] = triangle.get(0).get(0);

        for(int i = 1; i<triangle.size();i++){
            // 从后往前就不会影响到后面的计算
            for(int j = i ; j>=0; j--){
                if(j == 0) {
                    tmpSum[0] += triangle.get(i).get(j);
                }else if(j ==i){
                    tmpSum[j] = tmpSum[j-1] +triangle.get(i).get(j);
                }else{
                    tmpSum[j] = Math.min(tmpSum[j],
                            tmpSum[j-1])+triangle.get(i).get(j);
                }
            }
        }
        int result = Integer.MAX_VALUE;
        for(int i :tmpSum){
            result = Math.min(result,i);
        }

        return result;

    }
    public int solution(List<List<Integer>> triangle,int count,int index ,int currentSum) {

        if(count == triangle.size()) return currentSum;

        List<Integer> list = triangle.get(count);
        int min = Integer.MAX_VALUE;
        for(int i = index ;i<list.size()&&i<(index+2);i++){
            min = Math.min(solution(triangle,count+1,i,currentSum+list.get(i)),min);
        }
        return min;
    }


}

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



}

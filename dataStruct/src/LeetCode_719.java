package src;

import java.lang.reflect.Array;
import java.util.*;
//这题 读题没读懂  略过
public class LeetCode_719 {

    public static void main(String[] args) {
        LeetCode_719 code = new LeetCode_719();
//        int[] ints  = new int[]{2,7,11,15};
//        int[] ints  = new int[]{1,3,1};
//        int result = code.smallestDistancePair(ints,1);

        int[] ints  = new int[]{2,2,0,1,1,0,0,1,2,0};
        int result = code.smallestDistancePair(ints,2);


//        int[] ints  = new int[]{1,6,1};
//        int[] ints  = new int[]{9,10,7,10,6,1,5,4,9,8};
//        int[] ints  = new int[]{62,100,4};
//        int[] result = code.solution(ints,9);
//        int[] result = code.solution(ints,9);
//        int[] result = code.twoSum(ints,9);
//        int result = code.smallestDistancePair(ints,18);
//        int[] ints  = new int[]{62,100,4};
//        int result = code.smallestDistancePair(ints,2);
        System.out.println(result);

    }


    public int smallestDistancePair(int[] nums, int k) {

        ArrayList<Integer[]> pairsList = getAllPairs(nums);


        Integer[][] pairs = new Integer[pairsList.size()][2];
//        Integer[][] pairs = getAllPairs(nums);

        int i  = 0 ;
        for(Integer[] integers : pairsList){
            pairs[i++] = integers;
        }

        for(Integer[] ints:pairs) {

            System.out.println(Arrays.toString(ints));
        }

        quickIndex(pairs,k-1);
//        System.out.println(quickIndex(pairs,k));

        System.out.println("排序后");

        for(Integer[] ints:pairs) {

            System.out.println(Arrays.toString(ints));
        }

        ArrayList<Integer> arrayList = new ArrayList<>();
        int lastValue = -1;
        for(Integer[] integers :pairs){
            int abs = Math.abs(integers[1] - integers[0]);
            if(abs == lastValue){
                continue;
            }else{
                lastValue = abs;
                arrayList.add(lastValue);
            }
        }

        if(k<=arrayList.size()) {
            return arrayList.get(k - 1);
        }else{
            return arrayList.get(arrayList.size()-1);
        }
//        return Math.abs(pairs[k-1][1] - pairs[k-1][0]);
//        return quickIndex(pairs,k-1);
    }

    // 对pairs 进行排序

    ArrayList<Integer[]> getAllPairs(int[] nums){
        ArrayList<Integer[]> result = new ArrayList<>();

//        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        HashMap<Integer, ValueCount> map = new HashMap<>();
//        ArrayList<ArrayList<Integer>> lists = new ArrayList<>();
        for(int  i = 0 ; i<nums.length;i++){
            if(map.containsKey(nums[i])){
                ValueCount value =  map.get(nums[i]);
//                value.sameKey++;
            }
            for(int j = 0;j<nums.length;j++){

                if(i==j) continue;

                if(map.containsKey(nums[i])){
                    ValueCount value =  map.get(nums[i]);
                    value.list.add(nums[j]);
                }else{
                    ValueCount value = new ValueCount();
                    value.list.add(nums[j]);
                    map.put(nums[i],value);
                }
            }
        }

        for(int key:map.keySet()){
            ValueCount value = map.get(key);

                for(int i = 0 ; i<value.sameKey;i++) {
                    for(int v:  value.list) {
                        Integer[] ints = new Integer[2];
                        ints[0] = key;
                        ints[1] = v;
                        result.add(ints);
                    }
                }
        }


        return result;
    }

    class ValueCount{

        int sameKey = 1;
//        ArrayList<Integer> list = new ArrayList<>();
        HashSet<Integer> list = new HashSet<>();


    }


    int quickIndex(Integer[][] nums,int k){
        return quickIndex(nums,0,nums.length-1,k);
    }

    int quickIndex(Integer[][] nums ,int start ,int end,int k){
        if(end-start<=0) return 0;

        int index = findIndex(nums,start,end);
        if(index == k ) {
            System.out.println("提前退出 index" +index);

//            return Math.abs(nums[index][1]-nums[index][0]);
        }
        int indexL = quickIndex(nums,start,index-1,k);
        int indexR = quickIndex(nums,index+1,end,k);
//        return Math.max(indexL,indexR);
        return 0;
    }


    int findIndex(Integer[][] nums, int start, int end ){

        int random = new Random().nextInt(end- start)+start;

        swap(nums,start,random);

        Integer[] base = nums[start];
        int i = start +1;
        int j = end;

        while(true){

            while(i<end&&compare(nums[i],base)<0){
                i++;
            }

            while(j>start&&compare(nums[j],base)>0){
                j--;
            }


            if(i<j){
                swap(nums,i,j);
                i++;
                j--;
            }else{
                break;
            }

        }


        swap(nums,start,j);
        return j;
    }

    int compare(Integer[] ints1 ,Integer[] ints2){


        int abs1 = Math.abs(ints1[1] -ints1[0]);
        int abs2 = Math.abs(ints2[1] -ints2[0]);


        return abs1 -abs2;


    }

    void swap(Integer[][] nums ,int l ,int r ){

        Integer[] tmp = nums[l];
        nums[l] = nums[r];
        nums[r] =tmp;
    }
}

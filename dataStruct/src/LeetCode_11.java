package src;

public class LeetCode_11 {

    public static void main(String[] args) {
        LeetCode_11 code = new LeetCode_11();
        int[] ints  = new int[]{1,8,6,2,5,4,8,3,7};
//        int[] result = code.solution(ints,9);
//        String s = "A man, a plan, a canal: Panama";
        String s = "ab_a";
//        int result = code.solution(ints);
        int result = code.solution2(ints);
        System.out.println(result);




    }



    // 解决办法 1.0
    public  int solution(int[] height){

        int result = 0;

        int currentRHeight = 0;//当前右侧挡板的高度
        int currentLHeight = 0;//当前左侧挡板的高度
        for(int i  = 0 ; i<height.length;i++){
            currentRHeight = 0 ;
            if(height[i]>currentLHeight) {
                currentLHeight = height[i];

                for (int j = height.length - 1; j > i; j--) {

                    if (height[j] > currentRHeight) {
                        currentRHeight = height[j];
                        int currentHeight = currentRHeight > currentLHeight ? currentLHeight : currentRHeight;
                        int capture = currentHeight * (j - i);
                        if (capture > result) {
                            result = capture;
                        }

                    }

                }
            }
        }

        return result;
    }


    // 解决办法 2.0
    // 思路：   一开始 选 两头 ；  然后 往里走  ，矮的那头 得变高才有可能 变大
    //  但是要注意 只要  让矮的那一根去找就行了 ， 如果一样高 那就一起去找
    public  int solution2(int[] height){


        int l = 0;
        int r = height.length-1;


        int currentRHeight=height[l] ;
        int currentLHeight=height[r] ;

        int currentHeight = Math.min(currentLHeight,currentRHeight);
        int result =   (r-l)*Math.min(currentLHeight,currentRHeight);

        while(l<r){

            while(l<height.length&&height[l]<currentHeight){
                l++;
            }

            while(r>0&&height[r]<currentHeight){
                r--;
            }

            if(l<r) {
                currentHeight = Math.min(height[l], height[r]);
//                int currentHeight = Math.min(height[l], height[r]);
                int capture = currentHeight *(r-l);
                result = capture>result?capture:result;
                if(currentHeight== height[l]) {
                    l++;
                }

                if(currentHeight== height[r]) {
                    r--;

                }

            }


        }
        return result;
    }


    }

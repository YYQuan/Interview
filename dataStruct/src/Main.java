package src;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        String  str = "u0_a27    24913 4997  7720   2704  __skb_recv 00000000 S logcat\n" +
                "u0_a27    30501 4997  4136   1836  __skb_recv 00000000 S logcat";
        String[] strs = str.split("\\s+");
        for (String s:strs) {
            System.out.println(s+"\n");

        }
    }

    public  static void  test(){

    }

    void insertSort(int[] ints){
//        if(ints==null||ints.length<=0)
//            return null;
//
//        for(int i = 1 ;i< ints.length;i++){
//            int value = ints[i];
//            int j = i-1;
//            for(;j>=0;j--){
//                if(ints[j]>value){
//                    ints[j+1]  = ints[j];
//                }else{
//                    ints[j+1]  = value;
//                    break;
//                }
//            }
//        }
    }
}

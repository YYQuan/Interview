package src;

public class Main {

    public static void main(String[] args) {
        Thread t1 = new Thread(new TestSyn());
        Thread t2 = new Thread(new TestSyn());
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(TestSyn.count);

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

    public static  class TestSyn implements Runnable {
        public static final Object lockHelper = new Object();

        public static long count = 0;

        public synchronized void increase() {
            for (int i = 0; i < 1000000; i++) {
                count++;
            }
        }

        @Override
        public void run() {
            increase();
        }

        public static void main(String[] args) throws InterruptedException {

            int  val = 1 ;
            val |=Integer.MIN_VALUE;
            if(val == -1 ){
                System.out.println("equal  -1 ");
            }
            if(val == -2147483647 ){
                System.out.println("equal  -1 ");
            }

            System.out.println(Integer.toBinaryString(val));

            System.out.println("%b"+val);
        }
    }
}

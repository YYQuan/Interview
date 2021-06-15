package src;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {




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


            File file = new File("D:\\Workspace");
            file.getAbsoluteFile();
            System.out.println(Integer.toBinaryString(val));

            System.out.println("%b"+val);
        }
    }

    public static  class ReadFile {
        public ReadFile() {

        }

        public static ArrayList<File> getFiles(String filepath){
            ArrayList<File> files = new ArrayList<File>();
            File file = new File(filepath);
            File[] tempLists = file.listFiles();
            for (int i = 0; i < tempLists.length; i ++) {
                if (tempLists[i].isFile()) {
                    files.add(tempLists[i]);
                }
            }

            for (int i = 0; i < files.size(); i++) {
                if(files.get(i).isFile()){
                    File fileTmp  = files.get(i);
//                    boolean isXml = fileTmp.toString().endsWith(".xml");
                    boolean isXml = true;
                    if(isXml){
                        String path = fileTmp.getPath();
                        String pathReplace = path.replace(
                                "D:\\Workspace\\Android\\mingxinPhone\\MingXin\\phonecall3\\app\\src\\main\\res\\drawable-zh-mdpi\\",
                                "D:\\Workspace\\Android\\mingxinPhone\\MingXin\\phonecall3\\app\\src\\main\\res\\drawable-zh-mdpi\\mx_");

                        File fileRename = new File(pathReplace);
//                        System.out.println(fileRename.toString());

                        fileTmp.renameTo(fileRename);
                    }

                }
            }
            return files;
        }

        public static void main(String[] args) {
            //添加文件路径
            getFiles("D:\\Workspace\\Android\\mingxinPhone\\MingXin\\phonecall3\\app\\src\\main\\res\\drawable-zh-mdpi");
        }
    }
}

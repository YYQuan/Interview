import java.io.UnsupportedEncodingException;

public class Test {

    public static void main(String[] args) {

//        char c = '中';
        byte[] bytes = new byte[0];
        try {
            bytes = "中".getBytes("utf-16");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(bytes.length);
        for (byte b : bytes) {
            System.out.print(Integer.toHexString(Byte.toUnsignedInt(b)));
            System.out.print("");
        }
    }
}

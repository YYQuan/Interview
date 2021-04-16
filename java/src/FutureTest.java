import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

    public static void main(String[] args) throws InterruptedException {
//        FutureTask<Integer> futureTask = new FutureTask((Callable<Integer>) () -> 123);
        FutureTask<Integer> futureTask = new FutureTask( () ->{

        },Integer.class);
        new Thread(futureTask).start();

        try {
            System.out.println(futureTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

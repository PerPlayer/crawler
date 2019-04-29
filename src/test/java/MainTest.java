import java.util.Date;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Pattern;

public class MainTest {

    static volatile SynchronousQueue<Date> queue = new SynchronousQueue<>();

    public static void main(String[] args) throws Exception {
        String regex = "^.*?(?<=cn)$";
        Pattern pattern = Pattern.compile(regex);
        String input = "sina.com.cn";
        System.out.println(pattern.matcher(input).matches());
    }

}

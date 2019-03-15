import com.crawler.crawler.model.Entry;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.regex.Pattern;

public class MainTest {

    public static void main(String[] args) throws NoSuchMethodException {
        String regex = "^.*?(?<=cn)$";
        Pattern pattern = Pattern.compile(regex);
        String input = "sina.com.cn";
        System.out.println(pattern.matcher(input).matches());
//        System.out.println(input.replaceAll(regex, ""));
    }
}

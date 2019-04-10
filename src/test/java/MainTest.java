import com.crawler.crawler.model.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

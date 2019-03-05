import com.crawler.crawler.model.Entry;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Random;

public class MainTest {

    public static void main(String[] args) throws NoSuchMethodException {
        Method[] methods = Entry.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        System.out.println(Entry.class.getMethod("setId", String.class).getName());
    }
}

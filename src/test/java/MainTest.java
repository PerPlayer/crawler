import com.crawler.crawler.model.Entry;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainTest {

    public static void main(String[] args) throws NoSuchMethodException, IOException {
        String regex = "^.*?(?<=cn)$";
        Pattern pattern = Pattern.compile(regex);
        String input = "sina.com.cn";
        System.out.println(pattern.matcher(input).matches());
    }

}

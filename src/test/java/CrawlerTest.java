import com.crawler.engine.Engine;
import com.google.common.collect.Lists;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crawler.engine.Engine.images;
import static com.crawler.engine.Engine.urls;
import static com.crawler.util.http.HttpUtil.client;
import static com.crawler.util.http.HttpUtil.get;

public class CrawlerTest {

    private static String url = "http://www.imobile.com.cn/";
    private static String filePath = "E://tmp";


    public static void main(String[] args) throws Exception{
        pullContent();
        exec(read());
//        test();
        System.out.println("Done...");
        System.exit(0);
    }

    private static void test(){
        Pattern pattern = Pattern.compile("\"((?:http|https):.*?[^a]{0,3}.*?)\"");
        Matcher matcher = pattern.matcher("df\"https:aaa.comdf\"sdf\"http:bbb.comlsdf");

        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    private static void exec(String text){
        long start = System.currentTimeMillis();
        Collection<String> urls = urls(text);;
        urls.forEach(url->System.out.println(url));
        System.out.println(urls.size());
        System.out.println(System.currentTimeMillis()-start);
    }

    private static void pullContent() throws Exception{
        CloseableHttpClient client = client();
        CloseableHttpResponse response = client.execute(get(url));
        InputStream inputStream = response.getEntity().getContent();
        byte[] bytes = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (inputStream.read(bytes) != -1) {
            sb.append(new String(bytes));
        }
        save(sb.toString());
        inputStream.close();
    }

    private static void save(String content) throws Exception{
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(content.getBytes("UTF-8"));
        fos.close();
    }

    private static String read() throws Exception{
        FileInputStream fis = new FileInputStream(filePath);
        StringBuffer sb = new StringBuffer();
        byte[] bytes = new byte[1024];
        while (fis.read(bytes) != -1) {
            sb.append(new String(bytes));
        }
        fis.close();
        return sb.toString();
    }
}

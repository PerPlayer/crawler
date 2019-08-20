import com.crawler.engine.Engine;
import com.crawler.engine.FileManager;
import com.google.common.collect.Lists;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crawler.engine.Engine.*;
import static com.crawler.util.http.HttpUtil.client;
import static com.crawler.util.http.HttpUtil.get;

public class CrawlerTest {

    private static final String URL = "http://www.imobile.com.cn/";
    private static final String FILE_PATH = "E://tmp/";
    private static final String FILE_NAME = "aaa.txt";


    public static void main(String[] args) throws Exception{
        pullContent();
//        exec(FileManager.readString(FILE_NAME));
        pullAll(FileManager.readString(FILE_NAME));
//        test();
        System.out.println("Done...");
        System.exit(0);
    }

    private static void test(){
        Pattern pattern = Pattern.compile("<div.*?(?:id|class)[ ='\"]+?article['\"]+?.*?>(.*?)</div>");
        Matcher matcher = pattern.matcher("df\"https:aaa.<div class=\"article\" id=\"artbody\">com<title>abc</title>d</div>f\"sdf\"http:bbb.comlsdf");

        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    private static void exec(String text){
        long start = System.currentTimeMillis();
        System.out.println(title(text));
        System.out.println(content("article", text));
//        Collection<String> urls = urls(text);;
//        urls.forEach(url->System.out.println(url));
//        System.out.println(urls.size());
        System.out.println(System.currentTimeMillis()-start);
    }

    private static void pullContent() throws Exception{
        CloseableHttpClient client = client();
        CloseableHttpResponse response = client.execute(get(URL));
        InputStream inputStream = response.getEntity().getContent();
        byte[] bytes = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (inputStream.read(bytes) != -1) {
            sb.append(new String(bytes));
        }
        FileManager.save(FILE_PATH, FILE_NAME, sb.toString().getBytes("UTF-8"));
        inputStream.close();
    }

    private static void pullAll(String text) throws Exception{
        String title = title(text);
        String path = FILE_PATH + title + "/";
//        String article = content("article", text);
//        FileManager.save(path, title + ".txt", article.getBytes("UTF-8"));
        Collection<String> images = images(text);
        CloseableHttpClient client = client();
        images.forEach(iurl->{
            System.out.println(iurl);
            CloseableHttpResponse response = null;
            String fileName = iurl.substring(iurl.lastIndexOf("/") + 1);
            try {
                response = client.execute(get(iurl));
                InputStream inputStream = response.getEntity().getContent();
                byte[] bytes = new byte[1024*4];
                while (inputStream.read(bytes) != -1) {
                    inputStream.read(bytes);
                    FileManager.save(path, fileName, bytes);
                }
                if (bytes[0] != 0) {
                    FileManager.save(path, fileName, bytes);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(images.size());
    }
}

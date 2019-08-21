import com.crawler.engine.Executor;
import com.crawler.engine.FileManager;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crawler.engine.Engine.*;
import static com.crawler.util.http.HttpUtil.client;
import static com.crawler.util.http.HttpUtil.get;

public class CrawlerTest {

    private static final String URL = "https://www.meitulu.com/item/18222.html";
    private static final String FILE_PATH = "E://tmp/";
    private static final String FILE_NAME = "tmp.txt";
    private static final String KEY = "article";

    public static void main(String[] args) throws Exception{
        Executor.executor().execute(URL, KEY);
//        pullContent();
//        exec(FileManager.readString(FILE_NAME));
//        pullAll(FileManager.readString(FILE_NAME));
//        test();19913392936
        System.out.println("Done...");
        System.exit(0);
    }

    private static void test(){
        Pattern pattern = Pattern.compile("<div.*?(?:id|class)[ ='\"]+?(?!)article['\"]+?.*?>(.*?)</div>");
        Matcher matcher = pattern.matcher("df\"https:aaa.<div class=\"Article\" id=\"artbody\">com<title>abc</title>d</div>f\"sdf\"http:bbb.comlsdf");
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    private static void exec(String text){
        long start = System.currentTimeMillis();
        System.out.println(title(text));
        System.out.println(content(KEY, text));
        Collection<String> urls = pages(text);
        urls.forEach(url->System.out.println(url));
        urls = images(text);
        urls.forEach(url->System.out.println(url));
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
        saveContent(path, title, text);
        saveImage(path, text);

    }

    private static void saveContent(String path, String title, String text){
        String article = content(KEY, text);
        if (article != null) {
            try {
                FileManager.save(path, title + ".txt", article.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveImage(String path, String text){
        Collection<String> images = images(text);
        CloseableHttpClient client = client();
        images.forEach(iurl->{
            System.out.println(iurl);
            CloseableHttpResponse response = null;
            String fileName = iurl.substring(iurl.lastIndexOf("/") + 1);
            fileName = fileName.replace(".", System.currentTimeMillis()+".");
            try {
                HttpGet request = get(iurl);
                request.setHeader(HttpHeaders.HOST, "mtl.ttsqgs.com");
                request.setHeader(HttpHeaders.REFERER, "https://www.meitulu.com/item/18222.html");
                response = client.execute(request);
                InputStream inputStream = response.getEntity().getContent();
                byte[] bytes = new byte[1024*4];
                int t = 0;
                while ((t = inputStream.read(bytes)) != -1) {
                    FileManager.save(path, fileName, Arrays.copyOfRange(bytes, 0, t));
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(images.size());
    }

}

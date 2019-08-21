package com.crawler.engine;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.crawler.engine.Engine.*;
import static com.crawler.util.http.HttpUtil.client;
import static com.crawler.util.http.HttpUtil.get;

public class Executor {

    private static String HOST = "http://localhost/";
    private static final String TMP_PATH = "E://tmp/tmp/";
    private static final String URL_PATH = "E://tmp/url/";
    private static final String IMG_PATH = "E://tmp/img/";
    private static final String FILE_PATH = "E://tmp/file/";
    private static final String FILE_NAME = "tmp";
    private static final HttpClient client = client();

    private static Executor executor = new Executor();

    public static Executor executor(){
        return executor;
    }

    public void execute(String url, String key){
        fetchHost(url);
        pullText(url);
        pullUrl();
        downloadImg();
        downloadUrl();


//        pullContent(key, text);
    }

    private void downloadUrl(){
        Path path = Paths.get(URL_PATH);
        try {
            Files.list(path).forEachOrdered(cpath-> {
                String text = getText(cpath);
                String[] urls = text.split(System.lineSeparator());
                IntStream.range(0, urls.length).forEach(i->{
                    String url = urls[i];
                    System.out.println("抓取: " + url);
//                    execute(url, null);
                    fetchHost(url);
                    pullText(url);
                    pullUrl();
                    downloadImg();
                });
                try {
                    Files.delete(cpath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadImg(){
        Path path = Paths.get(IMG_PATH);
        try {
            Files.list(path).forEachOrdered(cpath-> {
                String text = getText(cpath);
                if (text != null) {
                    String[] imgUrls = text.split(System.lineSeparator());
                    String filePath = FILE_PATH + cpath.getFileName() + "/";
                    IntStream.range(0, imgUrls.length).forEach(i->{
                        String imgUrl = imgUrls[i];
                        if(StringUtils.isBlank(imgUrl)) return;
                        HttpResponse response = null;
                        String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                        System.out.println("下载：" + fileName + " -> " + imgUrl);
                        fileName = fileName.replace(".", System.currentTimeMillis()+".");
                        try {
                            HttpGet request = get(imgUrl);
                            request.setHeader(HttpHeaders.HOST, "mtl.ttsqgs.com");
                            request.setHeader(HttpHeaders.REFERER, "https://www.meitulu.com/item/18222.html");
                            response = client.execute(request);
                            InputStream inputStream = response.getEntity().getContent();
                            byte[] bytes = new byte[1024*4];
                            int t = 0;
                            while ((t = inputStream.read(bytes)) != -1) {
                                FileManager.save(filePath, fileName, Arrays.copyOfRange(bytes, 0, t));
                            }
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                try {
                    Files.delete(cpath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pullText(String url) {
        try {
            HttpResponse response = client.execute(get(url));
            InputStream inputStream = response.getEntity().getContent();
            byte[] bytes = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while (inputStream.read(bytes) != -1) {
                sb.append(new String(bytes));
            }
            inputStream.close();
            saveFile(TMP_PATH, FILE_NAME + System.currentTimeMillis(), sb.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(String path, String fileName, byte[] data) throws IOException, InterruptedException {
        waitForSave(path);
        FileManager.save(path, fileName, data);
    }

    private void waitForSave(String path) throws IOException, InterruptedException {
        Path p = Paths.get(path);

        if (p.toFile().list().length>50) {
            while (true) {
                if (Files.size(p)<=50) {
                    break;
                }
                TimeUnit.MICROSECONDS.sleep(1L);
            }
        }
    }

    public void pullUrl(){
        Path path = Paths.get(TMP_PATH);
        try {
            Files.list(path).forEachOrdered(cpath->{
                if (Files.exists(cpath)) {
                    synchronized(cpath.getFileName()){
                        String text = getText(cpath);
                        String title = title(text);
                        Collection<String> pages = pages(text);
                        StringBuilder urls = new StringBuilder();
                        pages.forEach(page-> {
                            if (page.startsWith("//")) {
                                urls.append("http:").append(page);
                            } else if (page.startsWith("/")) {
                                urls.append(HOST).append(page);
                            } else {
                                urls.append(page);
                            }
                            urls.append(System.lineSeparator());
                        });
                        Collection<String> images = images(text);
                        StringBuilder imgs = new StringBuilder();
                        images.forEach(img->imgs.append(img).append(System.lineSeparator()));
                        try {
                            saveFile(URL_PATH, title, urls.toString().getBytes("UTF-8"));
                            saveFile(IMG_PATH, title, imgs.toString().getBytes("UTF-8"));
//                            saveFile(URL_PATH, FILE_NAME + System.currentTimeMillis(), urls.toString().getBytes("UTF-8"));
//                            saveFile(IMG_PATH, FILE_NAME + System.currentTimeMillis(), imgs.toString().getBytes("UTF-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getText(Path path) {
        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while ((b = fileChannel.read(byteBuffer)) != -1) {
                baos.write(byteBuffer.array(), 0, b);
                byteBuffer.flip();
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fetchHost(String url) {
        HOST = url.replaceAll(".*((?:http|https)://.*?/).*", "$1");
    }

    private void pullContent(String key, String text) {
        try {
            String content = content(key, text);
            FileManager.save(TMP_PATH, FILE_NAME + System.currentTimeMillis(), content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

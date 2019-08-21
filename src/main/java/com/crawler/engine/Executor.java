package com.crawler.engine;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

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
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static com.crawler.engine.Engine.*;
import static com.crawler.util.http.HttpUtil.client;
import static com.crawler.util.http.HttpUtil.get;

public class Executor {

    private static String HOST = "http://localhost";
    private static final String TMP_PATH = "E://tmp/tmp/";
    private static final String URL_PATH = "E://tmp/url/";
    private static final String IMG_PATH = "E://tmp/img/";
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
//        pullContent(key, text);
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
            saveFile(TMP_PATH, FILE_NAME + System.currentTimeMillis(), sb.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(String path, String fileName, byte[] data) throws IOException, InterruptedException {
        waitForSave(path);
        FileManager.save(TMP_PATH, fileName, data);
    }

    private void waitForSave(String path) throws IOException, InterruptedException {
        Path p = Paths.get(path);
        if (Files.size(p)>50) {
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
                        Collection<String> pages = pages(text);
                        StringBuilder urls = new StringBuilder();
                        pages.forEach(page->urls.append(page).append("\n"));
                        Collection<String> images = images(text);
                        StringBuilder imgs = new StringBuilder();
                        images.forEach(img->imgs.append(img).append("\n"));
                        try {
                            saveFile(URL_PATH, FILE_NAME + System.currentTimeMillis(), urls.toString().getBytes("UTF-8"));
                            saveFile(IMG_PATH, FILE_NAME + System.currentTimeMillis(), imgs.toString().getBytes("UTF-8"));
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
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (fileChannel.read(byteBuffer) != -1) {
                baos.write(byteBuffer.array());
                byteBuffer.flip();
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fetchHost(String url) {
        HOST = url.replace(".*((?:http:|https:|//).*?)/.*", "$1");
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

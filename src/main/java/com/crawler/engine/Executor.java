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
import java.net.URL;
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

    private static final Logger logger = Logger.getLogger(Executor.class);

    private static int TEXT_SIZE = 1024*16;
    private static int IMG_SIZE = 1024*64;
    private static String HOST = "http://localhost/";
    private static final String TMP_PATH = "E://tmp/tmp/";
    private static final String URL_PATH = "E://tmp/url/";
    private static final String IMG_PATH = "E://tmp/img/";
    private static final String FILE_PATH = "E://tmp/file/";
    private static final String FILE_NAME = "tmp";
    private static final HttpClient client = client();

    private static Executor executor = new Executor();

    {
        logger.info("******** 初始化目录 ********");
        initDir(TMP_PATH);
        initDir(URL_PATH);
        initDir(IMG_PATH);
        initDir(FILE_PATH);
        logger.info("******** 初始化结束 ********");
    }

    private void initDir(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try {
                logger.info("创建目录> {} ", filePath);
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.info("初始化化目录异常> {}", e.getMessage());
            }
        }
    }

    public static Executor executor(){
        return executor;
    }

    public void execute(String url, String key){
        pullText(TMP_PATH, url);

        fetchUrl();
        downloadImg();
        downloadUrl();

        downloadUrl();
    }

    private void downloadUrl(){
        logger.info("******** 开始抓取URL ********");
        Path path = Paths.get(URL_PATH);
        try {
            Files.list(path).forEachOrdered(cpath-> {
                String text = getText(cpath);
                String[] urls = text.split(System.lineSeparator());
                IntStream.range(0, urls.length).forEach(i->{
                    String url = urls[i];
                    logger.info("抓取> {}", url);
                    pullText(TMP_PATH + cpath.getFileName() + "/", url);
                });
                try {
                    Files.delete(cpath);
                } catch (IOException e) {
                    logger.info("删除> {} 异常> {}", cpath.toAbsolutePath().getFileName(), e);
                }
            });
        } catch (IOException e) {
            logger.info("抓取URL异常> {}", e);
        }
        logger.info("******** 抓取URL结束 ********");
    }

    private void downloadImg(){
        logger.info("******** 开始下载图片 ********");
        Path path = Paths.get(IMG_PATH);
        try {
            Files.list(path).forEachOrdered(cpath-> {
                logger.info("读取> {} 图片地址", cpath.getFileName());
                String text = getText(cpath);
                if (text != null) {
                    String[] imgUrls = text.split(System.lineSeparator());
                    String filePath = FILE_PATH + cpath.getFileName() + "/";
                    IntStream.range(0, imgUrls.length).forEach(i->{
                        String imgUrl = imgUrls[i];
                        if(StringUtils.isBlank(imgUrl)) return;
                        HttpResponse response = null;
                        String fileName = imgUrl.replaceAll(".*/(.*?\\.(?:jpg|png|jpeg|gif|bmp|tif|svg)).*", "$1");
                        logger.info("下载图片> {}", fileName);
                        fileName = fileName.replace(".", System.currentTimeMillis()+".");
                        download(filePath, imgUrl, fileName);
                    });
                }
                try {
                    logger.info("删除文件> {}", cpath.getFileName());
                    Files.delete(cpath);
                } catch (IOException e) {
                    logger.info("删除文件> {} 异常> {}", cpath.getFileName(), e.getMessage());
                }
            });
        } catch (Exception e) {
            logger.info("下载图片异常> {}", e);
        }
        logger.info("******** 下载图片结束 ********");
    }

    private void download(String filePath, String fileUrl, String fileName) {
        HttpResponse response;
        try {
            logger.info("开始请求> {}", fileUrl);
            HttpGet request = get(fileUrl);
//                            request.setHeader(HttpHeaders.HOST, "mtl.ttsqgs.com");
//                            request.setHeader(HttpHeaders.REFERER, "https://www.meitulu.com/item/18222.html");
            response = client.execute(request);
            logger.info("请求响应> {}", response.getStatusLine());
            InputStream inputStream = response.getEntity().getContent();
            byte[] bytes = new byte[IMG_SIZE];
            int t = 0;
            while ((t = inputStream.read(bytes)) != -1) {
                FileManager.save(filePath, fileName, Arrays.copyOfRange(bytes, 0, t));
            }
            inputStream.close();
        } catch (Exception e) {
            logger.info("下载文件> {} 异常> {}", fileUrl, e.getMessage());
        }
    }

    private void pullText(String path, String url) {
        fetchHost(url);
        logger.info("开始抓取文本> {}", url);
        download(path, url, FILE_NAME + System.currentTimeMillis());
    }

    private void saveFile(String path, String fileName, byte[] data) throws IOException, InterruptedException {
        waitForSave(path);
        FileManager.save(path, fileName, data);
    }

    private void waitForSave(String path) throws IOException, InterruptedException {
        Path p = Paths.get(path);
        int length = p.toFile().list().length;
        if (length >50) {
            logger.info("目录> {} 当前文件数量> {}, 线程> {} 进入等待", path, length, Thread.currentThread().getName());
            while (true) {
                if (Files.size(p)<=50) {
                    break;
                }
                TimeUnit.MICROSECONDS.sleep(1L);
            }
        }
    }

    public void fetchUrl(){
        logger.info("******** 开始提取URL ********");
        Path path = Paths.get(TMP_PATH);
        try {
            Files.list(path).forEachOrdered(cpath->{
                if (Files.exists(cpath)) {
                    logger.info("从> {} 提取URL地址", cpath.toAbsolutePath().getFileName());
                    synchronized(cpath.getFileName()){
                        String text = getText(cpath);
                        String title = title(text);
                        Collection<String> pages = pages(text);
                        logger.info("提取到URL地址> {}个", pages.size());
                        Collection<String> images = images(text);
                        logger.info("提取到图片地址> {}个", images.size());
                        try {
                            saveFile(URL_PATH, title, fillUrl(pages).getBytes("UTF-8"));
                            saveFile(IMG_PATH, title, fillUrl(images).getBytes("UTF-8"));
                        } catch (Exception e) {
                            logger.info("保存> {} URL地址异常> {}", title, e.getMessage());
                        }

                    }
                }
            });
        } catch (Exception e) {
            logger.info("提取> {} URL地址异常：{}", path.getFileName(), e.getMessage());
        }
        logger.info("******** 抓取URL结束 ********");
    }

    private String fillUrl(Collection<String> pages) {
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
        return urls.toString();
    }

    private static String getText(Path path) {
        try {
            logger.info("读取> {} 内容", path.toAbsolutePath().getFileName());
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            ByteBuffer byteBuffer = ByteBuffer.allocate(TEXT_SIZE);
            while ((b = fileChannel.read(byteBuffer)) != -1) {
                baos.write(byteBuffer.array(), 0, b);
                byteBuffer.flip();
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            logger.info("读取> {} 内容异常: {}", path.toAbsolutePath().getFileName(), e.getMessage());
        }
        return null;
    }

    private void fetchHost(String url) {
        HOST = url.replaceAll(".*((?:http|https)://.*?/).*", "$1");
        logger.info("提取Host> {}", HOST);
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

package com.crawler.engine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.crawler.engine.Engine.*;
import static com.crawler.util.http.HttpUtil.client;
import static com.crawler.util.http.HttpUtil.get;

public class Executor {

    private static final Logger logger = Logger.getLogger(Executor.class);

    private static int TEXT_SIZE = 1024*16;
    private static int IMG_SIZE = 1024*64;
    private static int LIMIT_SIZE = 1024*100;
    private static int THRESHOLD = 80;
    private static String HOST = "http://localhost/";
    private static final String TMP_PATH = "E://tmp/tmp/";
    private static final String URL_PATH = "E://tmp/url/";
    private static final String IMG_PATH = "E://tmp/img/";
    private static final String FILE_PATH = "E://tmp/file/";
    private static final String DATA_PATH = "E://tmp/data.tm";
    private static final String FILE_NAME = "tmp";
    private static final HttpClient client = client();

    private ExecutorService poolExecutor = Executors.newFixedThreadPool(4);

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

        try {
            poolExecutor.submit(()->{
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("-------------提取");
                    fetchUrl();
                    Path p = Paths.get(TMP_PATH);
                    Files.list(p).forEach(path -> {
                        if (Files.isDirectory(path.toAbsolutePath())) {
                            try {
                                Files.list(path).forEach(path1 -> {
                                    try {
                                        Files.move(path1, Paths.get(p.toString() + "/" + path1.getFileName()), StandardCopyOption.ATOMIC_MOVE);
                                        System.out.println(">> " + path1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
//                    clearDirectory(TMP_PATH);
                }
            });
            poolExecutor.submit(()->{
                downloadUrl();
            });
            poolExecutor.submit(()->{
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    downloadImg();
                    clearDirectory(FILE_PATH);
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        downloadUrl();
//        fetchUrl();
//        downloadImg();
//        clearDirectory(FILE_PATH);

    }

    private void downloadUrl(){
        Path path = Paths.get(URL_PATH);
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                logger.info("******** 开始抓取URL ********");
                Map<String, List<String>> priorityUrls = Maps.newHashMap();
                Map<String, List<String>> noPriorityUrls = Maps.newHashMap();
                Files.list(path).forEachOrdered(cpath-> {
                    String text = getText(cpath);
                    String[] urls = text.split(System.lineSeparator());
                    IntStream.range(0, urls.length).forEach(i->{
                        String url = urls[i];
                        boolean priority = isPriority(cpath.getFileName().toString(), url);
                        String key = cpath.getFileName().toString();
                        if (priority) {
                            List<String> priorityList = priorityUrls.get(key);
                            if (priorityList == null) {
                                priorityList = Lists.newArrayList();
                                priorityUrls.put(key, priorityList);
                            }
                            priorityList.add(url);
                        }else{
                            List<String> noPriorityList = noPriorityUrls.get(key);
                            if (noPriorityList == null) {
                                noPriorityList = Lists.newArrayList();
                                noPriorityUrls.put(key, Lists.newArrayList());
                            }
                            noPriorityList.add(url);
                        }
                    });
                    try {
                        Files.delete(cpath);
                    } catch (IOException e) {
                        logger.info("删除> {} 异常> {}", cpath.toAbsolutePath().getFileName(), e);
                    }
                });
                if (priorityUrls.size() > 0) {
                    priorityUrls.forEach((k, v) -> {
                        logger.info("优先抓取> {}", v);
                        v.forEach(url->{
                            pullText(TMP_PATH + k + "/", url);
                        });
                    });
                    priorityUrls.clear();
                    noPriorityUrls.clear();
                    continue;
                }
                noPriorityUrls.forEach((k, v) -> {
                    logger.info("抓取> {}", v);
                    v.forEach(url->{
                        pullText(TMP_PATH + k + "/", url);
                    });
                });
                logger.info("******** 抓取URL结束 ********");
            }
        } catch (Exception e) {
            logger.info("抓取URL异常> {}", e);
            e.printStackTrace();
        }
    }

    private void pullText(String path, String url) {
        fetchHost(url);
        logger.info("开始抓取文本> {}", url);
        String fileName = FILE_NAME + System.currentTimeMillis();
        logger.info("添加到映射文件");
        refreshData(fileName, url);
        download(path, url, fileName);
    }

    private boolean isPriority(String name, String url){
        try {
            Stream<Path> list = Files.list(Paths.get(FILE_PATH));
            return list.count()<0? true: Files.list(Paths.get(FILE_PATH)).anyMatch(p -> {
                HashMap<String, String> map = fillDataMap();
                String targetUrl = map.get(name);
                return matchDegree(targetUrl, url) >= THRESHOLD;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int matchDegree(String source, String target){
        if (StringUtils.isAnyBlank(source, target)) {
            return 0;
        }
        char[] sourceChars = source.toCharArray();
        char[] targetChars = target.toCharArray();
        for (int i = 0; i < sourceChars.length; i++) {
            if (sourceChars[i] != targetChars[i]) {
                return (int)(i*1f/sourceChars.length*100);
            }
        }
        return 100;
    }

    public void fetchUrl(){
        logger.info("******** 开始提取URL ********");
        Path path = Paths.get(TMP_PATH);
        try {
            Files.list(path).forEachOrdered(cpath->{
                if (Files.exists(cpath)&&!Files.isDirectory(cpath)) {
                    logger.info("从> {} 提取URL地址", cpath.toAbsolutePath().getFileName());
                    synchronized(cpath.getFileName()){
                        String text = getText(cpath);
                        String title = title(text);
                        Collection<String> pages = pages(text);
                        logger.info("提取到URL地址> {}个", pages.size());
                        Collection<String> images = images(text);
                        logger.info("提取到图片地址> {}个", images.size());
                        try {
                            if(title==null) return;
                            saveFile(URL_PATH, title, fillUrl(pages).getBytes("UTF-8"));
                            saveFile(IMG_PATH, title, fillUrl(images).getBytes("UTF-8"));
                            Files.delete(cpath);
                            logger.info("更新映射文件> {}> {}", title, cpath.getFileName());
                            changeData(title, cpath.getFileName().toString());
                        } catch (Exception e) {
                            logger.info("保存> {} URL地址异常> {}", title, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.info("提取> {} URL地址异常：{}", path.getFileName(), e.getMessage());
        }
        logger.info("******** 提取URL结束 ********");
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
                        download(filePath, imgUrl, fileName);
                    });
                }
                try {
                    logger.info("删除文件> {}", cpath.getFileName());
                    Files.delete(cpath);
                } catch (Exception e) {
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
            request.setHeader(HttpHeaders.REFERER, "https://www.meitulu.com/item/18222.html");
            response = client.execute(request);
            logger.info("请求响应> {}", response.getStatusLine());
            InputStream inputStream = response.getEntity().getContent();
            byte[] bytes = new byte[IMG_SIZE];
            int t = 0;
            while ((t = inputStream.read(bytes)) != -1) {
                FileManager.append(filePath, fileName, Arrays.copyOfRange(bytes, 0, t));
            }
            inputStream.close();
            Path path = Paths.get(filePath + fileName);
            long size = Files.size(path);
            if (fileName.matches("^.*(?:jpg|png|jpeg|gif|bmp|tif|svg)$") && size < LIMIT_SIZE) {
                logger.info("删除文件> {}", path);
                Files.delete(path);
                return;
            }
            logger.info("{} 下载完成 > {}kb", path, String.format("%.1f", size/1024f));
        } catch (Exception e) {
            logger.info("下载文件> {} 异常> {}", fileUrl, e.getMessage());
        }
    }

    private void saveFile(String path, String fileName, byte[] data) throws IOException, InterruptedException {
        waitForSave(path);
        FileManager.append(path, fileName, data);
    }

    private void waitForSave(String path) throws IOException, InterruptedException {
        Path p = Paths.get(path);
        int length = p.toFile().list().length;
        if (length >150) {
            logger.info("目录> {} 当前文件数量> {}, 线程> {} 进入等待", path, length, Thread.currentThread().getName());
            while (true) {
                if (Files.size(p)<=150) {
                    break;
                }
                TimeUnit.MICROSECONDS.sleep(1L);
            }
        }
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
            fileChannel.close();
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
            String title = title(text);
            String content = content(key, text);
            FileManager.append(FILE_PATH + title + "/", title, content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private synchronized void refreshData(String name, String url) {
        refreshMap(name, url, map->{
            if (map.containsKey(name)) {
                return;
            }
            map.put(name, url);
        });
    }

    private synchronized void changeData(String newName, String oldName){
        System.out.println();
        refreshMap(newName, oldName, map->{
            String url = map.get(oldName);
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            iterator.forEachRemaining(entry->{
                if (entry.getValue().endsWith(url)||entry.getKey().startsWith(FILE_NAME)) {
                    iterator.remove();
                }
            });
            map.put(newName, url);
        });
    }

    private void refreshMap(String name, String url, Consumer<Map<String, String>> fun) {
        HashMap<String, String> map = fillDataMap();
        fun.accept(map);
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v)->{
            sb.append(k).append("> ").append(v).append(System.lineSeparator());
        });
        try {
            FileManager.save("E://tmp/", "data.tm", sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> fillDataMap() {
        Path path = Paths.get(DATA_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String text = getText(path);
        HashMap<String, String> map = Maps.newHashMap();
        if(StringUtils.isNotBlank(text)) {
            String[] split = text.split(System.lineSeparator());
            Arrays.asList(split).forEach(str->{
                if (str.contains(">")) {
                    String[] sp = str.split(">");
                    map.put(sp[0], sp[1].trim());
                }
            });
        }
        return map;
    }

    private void clearDirectory(String path) {
        Path p = Paths.get(path);
        try {
            Files.list(p).forEachOrdered(pa->{
                if (Files.isDirectory(pa) && pa.toFile().list().length<1) {
                    try {
                        logger.info("删除空目录> {}", pa);
                        Files.delete(pa);
                    } catch (IOException e) {
                        logger.info("删除空目录> {} 异常> {}", pa, e);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

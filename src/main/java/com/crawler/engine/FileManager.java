package com.crawler.engine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class);

    private static final String PATH = "E://tmp/";

    public static void save(String fileName, String content){
        try {
            save(fileName, content.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String fileName, byte[] data) {
        save(PATH, fileName, data);
    }

    public static void save(String path, String fileName, byte[] data) {
        Path nf = prepare(path, fileName, data);
        try(FileChannel fileChannel = FileChannel.open(nf, StandardOpenOption.CREATE, StandardOpenOption.WRITE)){
            logger.info("保存文件> {} > {}kb", nf, String.format("%.1f", data.length/1024f));
            fileChannel.write(ByteBuffer.wrap(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void append(String path, String fileName, byte[] data) {
        Path nf = prepare(path, fileName, data);
        try(FileChannel fileChannel = FileChannel.open(nf, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)){
            logger.info("追加文件> {} > {}kb", nf, String.format("%.1f", data.length/1024f));
            fileChannel.write(ByteBuffer.wrap(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Path prepare(String path, String fileName, byte[] data) {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            logger.info("创建多级目录> {}", path);
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Paths.get(path + fileName.replace("/", "").trim());
    }

    public static String readString(String fileName){
        return new String(read(PATH, fileName));
    }

    public static byte[] read(String fileName){
        return read(PATH, fileName);
    }

    public static byte[] read(String path, String fileName) {
        Path p = Paths.get(path + fileName);
        try {
            byte[] bytes = Files.readAllBytes(p);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}

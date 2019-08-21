package com.crawler.engine;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManager {

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
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path nf = Paths.get(path + fileName);
        try(FileChannel fileChannel = FileChannel.open(nf, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)){
            fileChannel.write(ByteBuffer.wrap(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
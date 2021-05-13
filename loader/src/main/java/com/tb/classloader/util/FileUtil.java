package com.tb.classloader.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author bjvolcano
 * @Date 2021/5/7 2:42 下午
 * @Version 1.0
 */
public class FileUtil {

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    @SneakyThrows
    public static void writeFile(File file, String context) {
//        FileChannel out = new FileOutputStream(file).getChannel();
//        out.write(convertStringToByte(context));
//        out.close();
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(context.getBytes());
            out.flush();
        }

    }

    @SneakyThrows
    public static void writeFile(File file, byte[] context) {
//        FileChannel out = new FileOutputStream(file).getChannel();
//        out.write(convertStringToByte(context));
//        out.close();
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(context);
            out.flush();
        }

    }

    @SneakyThrows
    private static ByteBuffer convertStringToByte(String content) {
        return ByteBuffer.wrap(content.getBytes("utf-8"));
    }
}

package com.volcano.classloader.util;

import com.volcano.classloader.config.Encrypt;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

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

    public static Encrypt loadEncryptByConfig() {
        String path = FileUtil.class.getClass().getResource(Encrypt.ENCRYPT_FILE).getPath();
        Encrypt encrypt = new Encrypt();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            Yaml yaml = new Yaml();
            Map<String, String> args = yaml.load(reader);
            encrypt.setKey(args.get("key"));
            encrypt.setKeyUrl(args.get("keyUrl"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return encrypt;
    }
}

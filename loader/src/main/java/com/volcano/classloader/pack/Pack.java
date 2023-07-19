package com.volcano.classloader.pack;

import com.volcano.classloader.des.Use3DES;
import com.volcano.classloader.util.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 加密打包类
 *
 * @Author bjvolcano
 * @Date 2021/5/7 2:19 下午
 * @Version 1.0
 */
@Slf4j
@Data
public class Pack {


    /**
     * 加密class
     * 默认使用 DESede 方式加密
     *
     * @param modulePath 加密的模块目录
     * @param key        用于加密的key
     * @throws IOException
     */
    public void encryptClasses(String modulePath, String key) throws IOException {
        String source = modulePath + File.separator + "target/classes";
        String target = source;
        encryptClass(source, target, key);
        //在jar包中增加特殊表示标识文件
        File encryptFile = new File(target + File.separator + "encrypt");
        FileUtil.writeFile(encryptFile, "");
        log.info("add encrypt file to target path:{}", encryptFile.getPath());

    }

    /**
     * 加密class
     *
     * @param classPath       要加密class所在的path根目录
     * @param classTargetPath 加密后的class输出目录
     * @param key             加密的可以
     * @throws IOException
     */
    public void encryptClass(String classPath, String classTargetPath, String key) throws IOException {
        File classRoot = new File(classPath);
        File[] files = classRoot.listFiles();
        if (files == null) {
            log.error("no have classes files!");
            return;
        }

        for (File file : files) {
            String targetPath = classTargetPath + "/" + file.getName();
            File targetFile = new File(targetPath);
            if (file.isDirectory()) {
                if (!targetFile.exists())
                    targetFile.mkdirs();
                encryptClass(file.getPath(), targetPath, key);
            } else if (file.isFile()) {
                if (file.getName().endsWith(".class")) {
                    byte[] encrypt = Use3DES.encrypt(key.getBytes(), Files.readAllBytes(file.toPath()));
                    Files.write(targetFile.toPath(), encrypt);
                } else {
                    FileUtil.copyFileUsingFileChannels(file, targetFile);
                }
            }
        }
    }


}

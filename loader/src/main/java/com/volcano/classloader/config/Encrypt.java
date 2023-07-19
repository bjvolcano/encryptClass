package com.volcano.classloader.config;

import com.volcano.classloader.loader.EncryptClassLoader;
import com.volcano.util.HttpClientResult;
import com.volcano.util.HttpClientUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ho.yaml.Yaml;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author bjvolcano
 * @Date 2021/5/11 3:21 下午
 * @Version 1.0
 */
@Slf4j
@Data
public class Encrypt {

    public static final String ENCRYPT_FILE = "encrypt.yml";
    //@Value("${encrypt-classes.libPath:}")
    private String libPath;

    //@Value("${encrypt-classes.key}")
    private String key;

    //@Value("${encrypt-classes.keyUrl:}")
    private String keyUrl;

    private byte[] keyBytes;

    public static void load() {
        InputStream inputStream = null;
        try {
            inputStream = ClassUtils.getDefaultClassLoader().getResource("encrypt.yml").openStream();
            if (inputStream == null) {
                log.warn("no have encrypt.yml.do not init DesClassLoader!");
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Encrypt encrypt = Yaml.loadType(inputStream, Encrypt.class);
            encrypt.fillClassLoader();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void fillClassLoader() {
        if (StringUtils.isEmpty(key) && StringUtils.isEmpty(keyUrl)) {
            throw new Exception("please set key or keyurl!");
        }
        if (!StringUtils.isEmpty(key)) {
            keyBytes = key.trim().getBytes();
        } else {
            HttpClientResult httpClientResult = HttpClientUtils.doPost(keyUrl, null, null);
            if (httpClientResult.getCode() != 200) {
                throw new Exception("获取加密key错误！");
            } else {

                keyBytes = httpClientResult.getContent().getBytes();
            }
        }

        setClassLoader();
    }


    private void setClassLoader() {
        EncryptClassLoader instance = (EncryptClassLoader) EncryptClassLoader.getInstance(this.getClass().getClassLoader());
        if (instance == null) {
            return;
        }
        //Thread.currentThread().setContextClassLoader(instance);
        instance.setKey(keyBytes);
        if (!StringUtils.isEmpty(libPath)) {
            File libsPath = new File(libPath);
            if (libsPath.isDirectory()) {
                List<File> files = getFiles(libsPath, null);
                if (!CollectionUtils.isEmpty(files)) {
                    for (File file : files) {
                        instance.addUrls(new String[]{"file:" + file.getPath()});
                    }
                }
            }
        }
        instance.start();

    }

    private List<File> getFiles(File dir, List<File> files) {
        if (files == null) {
            files = new ArrayList();
        }
        File[] subs = dir.listFiles();
        for (File file : subs) {
            if (file.isDirectory()) {
                files.addAll(getFiles(file, files));
            } else if (file.getName().endsWith(".jar")) {
                files.add(file);
            }
        }
        return files;
    }

}

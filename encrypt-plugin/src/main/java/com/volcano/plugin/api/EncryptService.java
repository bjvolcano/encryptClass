package com.volcano.plugin.api;

import com.volcano.classloader.pack.Pack;
import com.volcano.classloader.util.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Author bjvolcano
 * @Date 2021/5/13 6:03 下午
 * @Version 1.0
 */
@Slf4j
public class EncryptService {

    private Pack pack = new Pack();

    @SneakyThrows
    public void encryptClasses(String encryptPath, String key) {
        try {
            log.info("encrypt class start....\n\n");
            pack.encryptClasses(encryptPath, key);
            log.info("encrypted class ok!\n\n");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SneakyThrows
    public void copyJar2TargetPath(String sourcePath, String targetPath) {
        if (targetPath != null && !targetPath.trim().equals("")) {

            log.info("copy jar to: " + targetPath);

            File jarPath = new File(sourcePath + File.separator + "target");
            File[] list = jarPath.listFiles((dir, name) -> {
                return name.endsWith(".jar");
            });

            if (list == null) {
                log.warn("no files,dot can copy！");
                return;
            }

            for (File source : list) {
                File dest = new File(targetPath + File.separator
                        + source.getName()
                        .replace(".jar", "-encrypt.jar"));
                if (dest.exists()) {
                    dest.delete();
                }
                log.info("copy file [" + source.getPath() + "] to " + targetPath);
                FileUtil.copyFileUsingFileChannels(source, dest);
            }
        }
    }
}

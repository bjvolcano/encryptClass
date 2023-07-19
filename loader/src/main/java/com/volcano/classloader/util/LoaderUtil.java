package com.volcano.classloader.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ho.yaml.Yaml;
import org.springframework.boot.loader.jar.JarFile;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import sun.net.www.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;

/**
 * @Author bjvolcano
 * @Date 2021/5/13 5:51 下午
 * @Version 1.0
 */
@Slf4j
public class LoaderUtil {
    public static File[] addJarLibUrls(File[] files) {

        URL path = ClassUtils.getDefaultClassLoader().getResource("");
        boolean isJarRun = path.getPath().contains(".jar");
        if (isJarRun) {
            String jarPath = path.getPath().replace("!/BOOT-INF/classes!/", "").replace("file:", "");

            try {
                JarFile jarFile = new JarFile(new File(jarPath));
                ZipEntry classPathFile = jarFile.getEntry("BOOT-INF/classpath.idx");
                InputStream inputStream = jarFile.getInputStream(classPathFile);
                if (inputStream != null) {
                    List<File> jarList = new ArrayList();
                    for (File f : files) {
                        jarList.add(f);
                    }

                    List<String> classPaths = Yaml.loadType(inputStream, ArrayList.class);
                    if (!CollectionUtils.isEmpty(classPaths)) {
                        for (String libPath : classPaths) {
                            jarList.add(new File(jarPath + "!" + File.separator + libPath));
                        }
                    }
                    return jarList.toArray(new File[jarList.size()]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return files;
    }

    @SneakyThrows
    public static void processParentClassLoaderUrls(URL[] encryptUrls, URL[] sourceUrls, ClassLoader classLoader) {
        if (encryptUrls != null && encryptUrls.length > 0 && sourceUrls != null && sourceUrls.length > 0) {


            List<URL> encryptUrlsList = new ArrayList(Arrays.asList(encryptUrls));
            List<URL> sourceUrlsList = new ArrayList(Arrays.asList(sourceUrls));
            sourceUrlsList.removeAll(encryptUrlsList);

            log.info("****************************");
            for (URL url : encryptUrls) {
                log.info("has encrypt, path: " + url);
            }
            log.info("****************************");

//            Field ucpField = classLoader.getClass().getDeclaredField("ucp");
//            ucpField.setAccessible(true);
//            URLClassPath oldUcp = (URLClassPath) ucpField.get(classLoader);
//
//            Field accFiled = oldUcp.getClass().getDeclaredField("acc");
//            accFiled.setAccessible(true);
//            AccessControlContext acc = (AccessControlContext) accFiled.get(oldUcp);
//
//            URLClassPath ucp = new URLClassPath(newUrls, acc);
//            ucpField.set(classLoader, ucp);
//
//            Field parentUcp = classLoader.getClass().getSuperclass().getDeclaredField("ucp");
//            parentUcp.setAccessible(true);
//            parentUcp.set(classLoader, ucp);


        }
    }


    public static URL getFileURL(File var0) {
        try {
            var0 = var0.getCanonicalFile();
        } catch (IOException var3) {
        }

        try {
            return ParseUtil.fileToEncodedURL(var0);
        } catch (MalformedURLException var2) {
            throw new InternalError(var2);
        }
    }

    public static File[] getClassPath(String var0) {
        File[] var1;
        if (var0 != null) {
            int var2 = 0;
            int var3 = 1;
            boolean var4 = false;

            int var5;
            int var7;
            for (var5 = 0; (var7 = var0.indexOf(File.pathSeparator, var5)) != -1; var5 = var7 + 1) {
                ++var3;
            }

            var1 = new File[var3];
            var4 = false;

            for (var5 = 0; (var7 = var0.indexOf(File.pathSeparator, var5)) != -1; var5 = var7 + 1) {
                if (var7 - var5 > 0) {
                    var1[var2++] = new File(var0.substring(var5, var7));
                } else {
                    var1[var2++] = new File(".");
                }
            }

            if (var5 < var0.length()) {
                var1[var2++] = new File(var0.substring(var5));
            } else {
                var1[var2++] = new File(".");
            }

            if (var2 != var3) {
                File[] var6 = new File[var2];
                System.arraycopy(var1, 0, var6, 0, var2);
                var1 = var6;
            }
        } else {
            var1 = new File[0];
        }

        return var1;
    }


    @SneakyThrows
    public static boolean isEncrypted(File file) {
        //jar
        String path = file.getPath();
        if (path.contains(".jar") || path.contains(".zip")) {
            String[] paths = null;
            if (path.indexOf("!" + File.separator) != -1) {
                paths = path.split("!/");
            }

            JarFile jarFile = null;
            if (paths != null) {
                jarFile = new JarFile(new File(paths[0]));
                ZipEntry innerJar = jarFile.getEntry(paths[1]);
                if (innerJar != null) {
                    //找到jar包，👇找encrypt
                    JarFile nestedJarFile = jarFile.getNestedJarFile(innerJar);
                    ZipEntry encrypt = nestedJarFile.getEntry("encrypt");
                    if (encrypt != null) {
                        return true;
                    }
                }
            } else {
                jarFile = new JarFile(file);
                JarEntry jarEntry = jarFile.getJarEntry("encrypt");
                if (jarEntry != null) {
                    return true;
                }
            }
        } else {
            //目录
            File encryptFile = new File(file.getPath() + File.separator + "encrypt");
            //不存在，则代表该目录下的class未编码，不需要解码，本classloader不处理，交由父加载器来加载
            if (encryptFile.exists()) {
                return true;
            }
        }

        return false;
    }
}

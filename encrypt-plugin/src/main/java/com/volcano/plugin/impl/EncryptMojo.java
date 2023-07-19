package com.volcano.plugin.impl;

import com.volcano.util.HttpClientResult;
import com.volcano.util.HttpClientUtils;
import lombok.SneakyThrows;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 编码classes
 *
 * @goal en
 */

@Mojo(name = "encrypt", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, executionStrategy = "always")
public class EncryptMojo extends BaseMojo {

    @Parameter(property = "keyUrl", defaultValue = "")
    private String keyUrl;
    @Parameter(property = "key", defaultValue = "")
    private String key;



    @Override
    @SneakyThrows
    public void execute() {
        super.execute();
        String encryptKey = getEncryptKey();
        if (encryptKey == null || encryptKey.trim().equals("")) {
            return;
        }
        encryptService.encryptClasses(basedir.getPath(), key);
    }

    @SneakyThrows
    private String getEncryptKey() {
        if (key != null && !key.trim().equals(""))
            return key;
        if (keyUrl.equals("")) {
            log.warn("key or keyUrl must set!");
            return null;
        }
        HttpClientResult httpClientResult = HttpClientUtils.doPost(keyUrl, null, null);
        if (httpClientResult.getCode() != 200) {
            log.warn("获取加密key错误！");
            return null;
        } else {
            return key = httpClientResult.getContent();
        }
    }


}
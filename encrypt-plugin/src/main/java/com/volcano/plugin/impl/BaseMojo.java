package com.volcano.plugin.impl;

import com.volcano.plugin.api.EncryptService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * @Author bjvolcano
 * @Date 2021/5/10 6:06 下午
 * @Version 1.0
 */
public abstract class BaseMojo extends AbstractMojo {
    @Parameter(property = "project.basedir", required = true, readonly = true)
    protected File basedir;

    protected Log log;

    protected EncryptService encryptService = new EncryptService();

    @Override
    public void execute() {
        if (log == null) {
            log = getLog();
        }
    }
}

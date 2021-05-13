package com.tb.plugin.impl;

import com.tb.classloader.util.FileUtil;
import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author bjvolcano
 * @Date 2021/5/10 6:05 下午
 * @Version 1.0
 */
@Mojo(name = "copy", threadSafe = true, defaultPhase = LifecyclePhase.PACKAGE, executionStrategy = "always")
public class CopyJarMojo extends BaseMojo {
    @Parameter(property = "jarTargetPath", defaultValue = "")
    private String jarTargetPath;

    @SneakyThrows
    @Override
    public void execute() {
        if (jarTargetPath != null && !jarTargetPath.trim().equals("")) {
            super.execute();
            encryptService.copyJar2TargetPath(basedir.getPath(),
                    jarTargetPath);
        }
    }
}

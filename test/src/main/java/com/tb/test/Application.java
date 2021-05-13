package com.tb.test;

import com.tb.classloader.config.Encrypt;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author bjvolcano
 * @Date 2021/5/8 9:35 上午
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.tb.classloader.config", "com.tb.test"})
public class Application {

    static {
        Encrypt.load();
    }
    @SneakyThrows
    public static void main(String[] args) {


        SpringApplication.run(Application.class, args);
    }

}

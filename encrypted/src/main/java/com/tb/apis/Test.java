package com.tb.apis;

import com.tb.interfaces.ITest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author bjvolcano
 * @Date 2021/5/7 6:56 下午
 * @Version 1.0
 */
@Component
public class Test implements ITest {
    @Override
    public void test() {
        System.out.println("--------------------");
    }

    @PostConstruct
    public void init() {

        System.out.println(this.getClass().getClassLoader() + " test inited");
    }
}

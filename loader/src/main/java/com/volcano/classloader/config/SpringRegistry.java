package com.volcano.classloader.config;

import com.volcano.classloader.loader.EncryptClassLoader;
import com.volcano.classloader.util.FileUtil;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author bjvolcano
 * @Date 2021/5/11 1:39 下午
 * @Version 1.0
 */
@Component
@Data
public class SpringRegistry implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Encrypt encrypt = FileUtil.loadEncryptByConfig();
        EncryptClassLoader instance = EncryptClassLoader.getInstance(encrypt, this.getClass().getClassLoader());
        if (instance == null) {
            return;
        }

        instance.setBeanFactory((DefaultListableBeanFactory) beanFactory);
        if (!(beanFactory.getBeanClassLoader() instanceof EncryptClassLoader)) {
            beanFactory.setBeanClassLoader(instance);
        }

        instance.registryBeans();
    }
}

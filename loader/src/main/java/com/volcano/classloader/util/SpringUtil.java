package com.volcano.classloader.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;

/**
 * @Author bjvolcano
 * @Date 2021/5/13 3:31 下午
 * @Version 1.0
 */
public class SpringUtil {
    public static BeanDefinition buildBeanDefinition(Class cla) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cla);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        //设置当前bean定义对象是单利的
        Scope scope = (Scope) cla.getAnnotation(Scope.class);
        String scopeString = ConfigurableBeanFactory.SCOPE_SINGLETON;
        if (scope != null) {
            scopeString = scope.value();
        }
        beanDefinition.setScope(scopeString);
        Lazy lazy = (Lazy) cla.getAnnotation(Lazy.class);
        if (lazy != null) {
            beanDefinition.setLazyInit(true);
        }

        return beanDefinition;
    }

    /**
     * 方法描述 判断class对象是否带有spring的注解
     *
     * @param cla jar中的每一个class
     * @return true 是spring bean   false 不是spring bean
     * @method isSpringBeanClass
     */
    public static boolean isSpringBeanClass(Class<?> cla) {
        if (cla == null) {
            return false;
        }
        //是否是接口
        if (cla.isInterface()) {
            return false;
        }

        //是否是抽象类
        if (Modifier.isAbstract(cla.getModifiers())) {
            return false;
        }

        if (cla.getAnnotation(Component.class) != null) {
            return true;
        }

        if (cla.getAnnotation(Repository.class) != null) {
            return true;
        }

        if (cla.getAnnotation(Service.class) != null) {
            return true;
        }

        return false;
    }
}

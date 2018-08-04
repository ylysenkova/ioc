package com.lysenkova.ioc.context;

import com.lysenkova.ioc.entity.Bean;

public interface BeanPostProcessor {
    default Object postProcessBeforeInitialization(Bean bean, String beanName) {
       return bean;
    }
    default Object postProcessAfterInitialization(Bean bean, String beanName) {
        return bean;
    }
}

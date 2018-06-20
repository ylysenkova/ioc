package com.lysenkova.ioc.testentities;

import com.lysenkova.ioc.applicationcontext.BeanPostProcessor;
import com.lysenkova.ioc.entity.Bean;

public class BeanPostProcessorTestEntity implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Bean bean, String beanName) {
        bean.setId("changedBean");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Bean bean, String beanName) {
        bean.setId("changedBean");
        return bean;
    }
}
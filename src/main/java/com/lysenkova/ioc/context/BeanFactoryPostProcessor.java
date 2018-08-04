package com.lysenkova.ioc.context;


import com.lysenkova.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(List<BeanDefinition> beanDefinitions);
}

package com.lysenkova.ioc.beanparser;

import com.lysenkova.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions();
}

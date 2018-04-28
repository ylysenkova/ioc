package com.lysenkova.initbean.beanparser;

import com.lysenkova.initbean.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions();
}

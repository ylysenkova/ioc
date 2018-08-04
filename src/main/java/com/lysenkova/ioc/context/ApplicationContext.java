package com.lysenkova.ioc.context;

import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.entity.BeanDefinition;

import java.util.List;

public interface ApplicationContext {
    <T> T getBean(Class<T> clazz);
    <T> T getBean(String name, Class<T> clazz);
    Object getBean(String name);
    List<String> getBeanNames();
    List<BeanDefinition> getBeanDefinitions();
    void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader);
}

package com.lysenkova.ioc.applicationcontext;


public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ApplicationContext beanFactory);
}

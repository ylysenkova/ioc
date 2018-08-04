package com.lysenkova.ioc.context;

import com.lysenkova.ioc.entity.BeanDefinition;

import java.util.ArrayList;
import java.util.List;

public class BeanFactoryPostProcessorInvoker {
    private List<BeanDefinition> beanDefinitions;
    private List<BeanDefinition> factoryBeanDefinitions;

    public BeanFactoryPostProcessorInvoker(List<BeanDefinition> beanDefinitions, List<BeanDefinition> factoryBeanDefinitions) {
        this.beanDefinitions = beanDefinitions;
        this.factoryBeanDefinitions = factoryBeanDefinitions;
    }

    public void invokePostProcessBeanFactoryMethod() {
        List<BeanFactoryPostProcessor> factories = createFactoryBeans();
        for (BeanFactoryPostProcessor factory : factories) {
            factory.postProcessBeanFactory(beanDefinitions);
        }
    }

    private List<BeanFactoryPostProcessor> createFactoryBeans() {
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
        for (BeanDefinition factoryBeanDefinition : factoryBeanDefinitions) {
            try {
                Class<?> factoryClazz = Class.forName(factoryBeanDefinition.getBeanClassName());
                BeanFactoryPostProcessor beanFactoryPostProcessor = (BeanFactoryPostProcessor) factoryClazz.newInstance();
                beanFactoryPostProcessors.add(beanFactoryPostProcessor);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException("Can not invoke postProcessBeanFactory method", e);
            }
        }
        return beanFactoryPostProcessors;
    }

}

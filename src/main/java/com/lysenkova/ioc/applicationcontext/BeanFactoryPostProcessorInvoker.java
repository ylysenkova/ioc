package com.lysenkova.ioc.applicationcontext;

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
            if (factories != null) {
            for (BeanFactoryPostProcessor factory : factories) {
                factory.postProcessBeanFactory(beanDefinitions);
            }
        }
    }

    private List<BeanFactoryPostProcessor> createFactoryBeans() {
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
        for (BeanDefinition factoryBeanDefinition : factoryBeanDefinitions) {
            try {
                beanFactoryPostProcessors.add((BeanFactoryPostProcessor) Class.forName(factoryBeanDefinition.getBeanClassName()).newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException("Can not invoke postProcessBeanFactory method", e);
            }
        }
        return beanFactoryPostProcessors;
    }

}

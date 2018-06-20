package com.lysenkova.ioc.applicationcontext;

import com.lysenkova.ioc.entity.BeanDefinition;

import java.util.List;

public class BeanFactoryPostProcessorInvoker {
    private ApplicationContext applicationContext;

    public BeanFactoryPostProcessorInvoker(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void invokePostProcessBeanFactoryMethod(BeanDefinition beanDefinition, List<BeanDefinition> factoryBeanDefinitions) {
        boolean isNotFactoryBeanDefinition = isNotFactoryPostProcessorBean(beanDefinition, factoryBeanDefinitions);
        for (BeanDefinition factoryBeanDefinition : factoryBeanDefinitions) {
            if (isNotFactoryBeanDefinition) {
                try {
                    ((BeanFactoryPostProcessor) Class.forName(factoryBeanDefinition.getBeanClassName()).newInstance()).postProcessBeanFactory(applicationContext);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    throw new RuntimeException("Can not invoke postProcessBeanFactory method", e);
                }
            }
        }
    }

    private boolean isNotFactoryPostProcessorBean(BeanDefinition beanDefinition, List<BeanDefinition> factoryBeanDefinitions) {
        if (!factoryBeanDefinitions.contains(beanDefinition)) {
            return true;
        }
        return false;
    }
}
